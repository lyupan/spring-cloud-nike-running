package com.steve.demo.service;

import com.steve.demo.domain.CurrentPosition;

public interface PositionService {
    void processPositionInfo(long id, CurrentPosition currentPosition, boolean sendPositionToDistributionService);
}
