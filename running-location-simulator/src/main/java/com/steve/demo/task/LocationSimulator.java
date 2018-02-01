package com.steve.demo.task;

import com.steve.demo.domain.*;
import com.steve.demo.service.PositionService;
import com.steve.demo.support.NavUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

// Location Simulator is not a Spring managed bean. Therefore cannot DI
// PositionService which is a Spring bean.
public class LocationSimulator implements Runnable{

    @Getter
    @Setter
    private long id;

    @Setter
    private PositionService positionService;
    private AtomicBoolean cancel = new AtomicBoolean();
    private double speedInMps;
    private boolean shouldMove;
    private boolean exportPositionsToMessaging = true;
    private int reportInterval = 500;

    @Getter
    @Setter
    private PositionInfo positionInfo = null;

    @Getter
    @Setter
    private List<Leg> legs;
    private RunnerStatus runnerStatus = RunnerStatus.NONE;
    private String runningId;
    private int secondsToError = 45;

    @Setter
    private Point startPoint;
    private Date executionStartTime;
    private String fromAddress;
    private String toAddress;

    private MedicalInfo medicalInfo;

    public LocationSimulator(GpsSimulatorRequest gpsSimulatorRequest) {
        this.shouldMove = gpsSimulatorRequest.isMove();
        this.exportPositionsToMessaging = gpsSimulatorRequest.isExportPositionsToMessaging();
        this.setSpeed(gpsSimulatorRequest.getSpeed());
        this.reportInterval = gpsSimulatorRequest.getReportInterval();
        this.secondsToError = gpsSimulatorRequest.getSecondsToError();
        this.runningId = gpsSimulatorRequest.getRunningId();
        this.runnerStatus = gpsSimulatorRequest.getRunnerStatus();
        this.medicalInfo = gpsSimulatorRequest.getMedicalInfo();
    }

    public void setSpeed(double speed) {
        this.speedInMps = speed;
    }

    public long getId() {
        return id;
    }

    public void cancel() {
        this.cancel.set(true);
    }

    @Override
    public void run() {

        try {
            if (cancel.get()) {
                destroy();
                return ;
            }
            while (!Thread.interrupted()) {
                long startTime = new Date().getTime();
                if (positionInfo != null) {
                    if (shouldMove) {
                        moveRunningLocation();
                        positionInfo.setSpeed(speedInMps);
                    } else {
                        positionInfo.setSpeed(0);
                    }
                }
                positionInfo.setRunnerStatus(runnerStatus);
                MedicalInfo medicalInfoToUse = null;
                CurrentPosition currentPosition = new CurrentPosition(positionInfo.getRunningId(),
                        new Point(positionInfo.getPoint().getLatitude(), positionInfo.getPoint().getLongitude()),
                        positionInfo.getRunnerStatus(),
                        positionInfo.getSpeed(),
                        positionInfo.getLeg().getHeading(), medicalInfoToUse);

                positionService.processPositionInfo(id, currentPosition, exportPositionsToMessaging);
                sleep(startTime);
            }
        } catch (InterruptedException e) {
            destroy();
            return ;
        }
        destroy();
    }

    private void sleep(long startTime) throws InterruptedException{
        long endTime = new Date().getTime();
        long elapsedTime = endTime - startTime;
        long sleepTime = reportInterval - elapsedTime > 0 ? reportInterval - elapsedTime : 0;
        Thread.sleep(sleepTime);
    }

    public void setStartPosition() {
        positionInfo = new PositionInfo();
        positionInfo.setRunningId(this.runningId);
        Leg leg = legs.get(0);
        positionInfo.setLeg(leg);
        positionInfo.setPoint(leg.getStartPosition());
        positionInfo.setDistaceFromStart(0.0);
    }

    private void moveRunningLocation() {
        Double distance = speedInMps * reportInterval / 1000.0;
        Double distanceFromStart = positionInfo.getDistaceFromStart() + distance;
        Double excess = 0.0;
        for (int i = positionInfo.getLeg().getId(); i < legs.size(); i++) {
            Leg currentLeg = legs.get(i);
            excess = distanceFromStart > currentLeg.getLength() ? distanceFromStart - currentLeg.getLength() : 0.0;
            if (Double.doubleToRawLongBits(excess) == 0) {
                positionInfo.setLeg(currentLeg);
                positionInfo.setDistaceFromStart(distanceFromStart);
                Point newPosition = NavUtils.getPosition(currentLeg.getStartPosition(), distanceFromStart, currentLeg.getHeading());
                positionInfo.setPoint(newPosition);
            }
        }


    }

    private void destroy() {
        positionInfo = null;
    }
}
