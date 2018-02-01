package com.steve.demo.rest;

import com.steve.demo.domain.GpsSimulatorRequest;
import com.steve.demo.domain.SimulatorInitLocations;
import com.steve.demo.service.GpsSimulatorFactory;
import com.steve.demo.service.PathService;
import com.steve.demo.task.LocationSimulator;
import com.steve.demo.task.LocationSimulatorInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api")
public class LocationSimulatorRestApi {
    @Autowired
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    private GpsSimulatorFactory gpsSimulatorFactory;

    @Autowired
    private PathService pathService;

    private Map<Long, LocationSimulatorInstance> taskFutures = new HashMap<>();

    @RequestMapping("/simulation")
    public List<LocationSimulatorInstance> simulation() {
        List<LocationSimulatorInstance> locationSimulatorInstanceList = new LinkedList<>();
        SimulatorInitLocations initLocations = pathService.loadSimulatorInitLocations();


        for (GpsSimulatorRequest gpsSimulatorRequest : initLocations.getGpsSimulatorRequests()) {
            LocationSimulator locationSimulator = gpsSimulatorFactory.prepareGpsSimulator(gpsSimulatorRequest);
            Future<?> future = taskExecutor.submit(locationSimulator);
            LocationSimulatorInstance locationSimulatorInstance = new LocationSimulatorInstance(locationSimulator.getId(), locationSimulator, future);
            locationSimulatorInstanceList.add(locationSimulatorInstance);
            taskFutures.put(locationSimulator.getId(), locationSimulatorInstance);
        }
        return locationSimulatorInstanceList;
    }

    @RequestMapping("/cancel")
    public int cancel() {
        int numberOfCancelledTasks = 0;
        for (Map.Entry<Long, LocationSimulatorInstance> entry : taskFutures.entrySet()) {
            LocationSimulatorInstance instance = entry.getValue();
            instance.getLocationSimulator().cancel();
            boolean wasCancelled = instance.getLocationSimulatorTask().cancel(true);
            if (wasCancelled)
                numberOfCancelledTasks++;
        }
        return numberOfCancelledTasks;
    }
}
