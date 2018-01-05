package com.steve.demo.task;

import com.steve.demo.domain.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocationSimulator implements Runnable{

    private long id;
//    private PositionService positionService;
    private AtomicBoolean cancel = new AtomicBoolean();
    private double speedInMps;
    private boolean shouldMove;
    private boolean exportPositionsToMessaging = true;
    private int reportInterval = 500;
//    private PositionInfo positionInfo = null;
    private List<Leg> legs;
    private RunnerStatus runnerStatus = RunnerStatus.NONE;
    private String runningId;
    private int secondsToError = 45;
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

    @Override
    public void run() {

    }
}
