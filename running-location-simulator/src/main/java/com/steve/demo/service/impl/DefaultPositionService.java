package com.steve.demo.service.impl;

import com.steve.demo.domain.CurrentPosition;
import com.steve.demo.service.PositionService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DefaultPositionService implements PositionService {

    @Value("${com.steve.running.location.distribution}")
    private String runningLocationDistribution;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void processPositionInfo(long id, CurrentPosition currentPosition, boolean sendPositionToDistributionService) {

        if (sendPositionToDistributionService) {
            log.info(String.format("Thread %d is calling PositionDistributionService REST API", Thread.currentThread().getId()));
            restTemplate.postForLocation(runningLocationDistribution, currentPosition);
        }

    }
}
