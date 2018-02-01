package com.steve.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steve.demo.domain.SimulatorInitLocations;
import com.steve.demo.service.PathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class DefaultPathService implements PathService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public SimulatorInitLocations loadSimulatorInitLocations() {
        InputStream inputStream = this.getClass().getResourceAsStream("/init-locations.json");
        try {
           return objectMapper.readValue(inputStream, SimulatorInitLocations.class);
        } catch (IOException e) {
            new IllegalStateException(e);
        }
        return null;
    }
}
