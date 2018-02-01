package com.steve.demo.service.impl;

import com.steve.demo.domain.GpsSimulatorRequest;
import com.steve.demo.domain.Leg;
import com.steve.demo.domain.Point;
import com.steve.demo.service.GpsSimulatorFactory;
import com.steve.demo.service.PositionService;
import com.steve.demo.support.NavUtils;
import com.steve.demo.task.LocationSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DefaultGpsSimulatorFactory implements GpsSimulatorFactory {

    @Autowired
    private PositionService positionService;

    private final AtomicLong instanceCounter = new AtomicLong();

    @Override
    public LocationSimulator prepareGpsSimulator(GpsSimulatorRequest gpsSimulatorRequest) {

        LocationSimulator locationSimulator = new LocationSimulator(gpsSimulatorRequest);
        locationSimulator.setPositionService(positionService);
        locationSimulator.setId(this.instanceCounter.incrementAndGet());

        List<Point> points = NavUtils.decodePolyline(gpsSimulatorRequest.getPolyline());
        locationSimulator.setStartPoint(points.iterator().next());
        return prepareGpsSimulator(locationSimulator, points);
    }

    public LocationSimulator prepareGpsSimulator(LocationSimulator locationSimulator, List<Point> points) {
        locationSimulator.setPositionInfo(null);

        final List<Leg> legs = createLegsList(points);
        locationSimulator.setLegs(legs);
        locationSimulator.setStartPosition();
        return locationSimulator;
    }

    private List<Leg> createLegsList(List<Point> points) {
        final List<Leg> legs = new ArrayList<Leg>();
        for (int i = 0; i < (points.size() - 1); i++) {
            Leg leg = new Leg();
            leg.setId(i);
            leg.setStartPosition(points.get(i));
            leg.setEndPosition(points.get(i + 1));
            Double length = NavUtils.getDistance(points.get(i), points.get(i + 1));
            leg.setLength(length);
            Double heading = NavUtils.getBearing(points.get(i), points.get(i + 1));
            leg.setHeading(heading);
            legs.add(leg);
        }
        return legs;
    }
}
