package com.steve.demo.domain;

import lombok.Data;

@Data
public class GpsSimulatorRequest {
    private String runningId;
    private double speed;
    private boolean move = true;
    private boolean exportPositionsToMessaging = true;
    private int reportInterval = 500;
    private int secondsToError = 0;
    private RunnerStatus runnerStatus = RunnerStatus.NONE;
    private String polyline;
    private String fromAddress;
    private String toAddress;

    private MedicalInfo medicalInfo;

    @Override
    public String toString() {
        return "GpsSimulationRequest [runningId=" + runningId + ", speed=" + speed + ", move=" + move + ", exportPositionsToMessaging=" +
                exportPositionsToMessaging + ", reportInterval=" + reportInterval + "]";
    }
}
