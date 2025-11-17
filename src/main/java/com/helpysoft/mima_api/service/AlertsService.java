package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AlertsRequest;
import com.helpysoft.mima_api.dto.AlertsResponse;
import com.helpysoft.mima_api.entity.AlertStatus;

import java.util.List;
import java.util.UUID;

public interface AlertsService {
    AlertsResponse create(AlertsRequest request);
    AlertsResponse update(UUID trackingId, AlertsRequest request);
    AlertsResponse findByTrackingId(UUID trackingId);
    List<AlertsResponse> findByAgent(UUID agentTrackingId);
    List<AlertsResponse> findByStatus(AlertStatus status);
    List<AlertsResponse> findAll();
    void delete(UUID trackingId);
}
