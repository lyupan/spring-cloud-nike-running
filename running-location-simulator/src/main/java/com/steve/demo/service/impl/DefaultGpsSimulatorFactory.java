package com.steve.demo.service.impl;

import com.steve.demo.domain.GpsSimulatorRequest;
import com.steve.demo.service.GpsSimulatorFactory;
import com.steve.demo.task.LocationSimulator;

public class DefaultGpsSimulatorFactory implements GpsSimulatorFactory {
    @Override
    public LocationSimulator prepareGpsSimulator(GpsSimulatorRequest gpsSimulatorRequest) {
        LocationSimulator locationSimulator = new LocationSimulator(gpsSimulatorRequest);

        return null;
    }
}
