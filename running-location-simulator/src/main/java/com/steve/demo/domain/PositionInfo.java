package com.steve.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PositionInfo {
    private String runningId;
    private Point point;
    private RunnerStatus runnerStatus = RunnerStatus.NONE;
    private Leg leg;
    private Double distaceFromStart;
    private double speed;
}
