package com.steve;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steve.model.CurrentPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;

import java.io.IOException;

@EnableBinding(Sink.class)
@Slf4j
public class RunningLocationSink {

    @Autowired
    private ObjectMapper objectMapper;

    @ServiceActivator(inputChannel = Sink.INPUT)
    public void updateLocation(String input) {
        log.info("location input updater: " + input);
        try {
            CurrentPosition position = objectMapper.readValue(input, CurrentPosition.class);
            log.info("Current position " + position.getRunningId());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
