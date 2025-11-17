package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.DutiesRequest;
import com.helpysoft.mima_api.dto.DutiesResponse;
import com.helpysoft.mima_api.entity.DutyStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DutiesService {
    DutiesResponse create(DutiesRequest request);
    DutiesResponse update(UUID trackingId, DutiesRequest request);
    DutiesResponse findByTrackingId(UUID trackingId);
    List<DutiesResponse> findByAgent(UUID agentTrackingId);
    List<DutiesResponse> findByUnit(UUID unitTrackingId);
    List<DutiesResponse> findByStatus(DutyStatus status);
    List<DutiesResponse> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<DutiesResponse> findAll();
    void delete(UUID trackingId);
}
