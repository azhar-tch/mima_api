package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.dto.HistoriesResponse;
import com.helpysoft.mima_api.entity.ActionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface HistoriesService {
    HistoriesResponse create(HistoriesRequest request);
    HistoriesResponse findByTrackingId(UUID trackingId);
    List<HistoriesResponse> findByAgent(UUID agentTrackingId);
    List<HistoriesResponse> findByEntityTrackingId(UUID entityTrackingId);
    List<HistoriesResponse> findByActionType(ActionType actionType);
    List<HistoriesResponse> findByEntityName(String entityName);
    List<HistoriesResponse> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<HistoriesResponse> findAll();
}
