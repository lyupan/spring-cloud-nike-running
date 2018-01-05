package com.steve.demo.service;

import com.steve.demo.domain.GpsSimulatorRequest;
import com.steve.demo.task.LocationSimulator;

public interface GpsSimulatorFactory {
    LocationSimulator prepareGpsSimulator(GpsSimulatorRequest gpsSimulatorRequest);
}
