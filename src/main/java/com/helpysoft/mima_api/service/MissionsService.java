package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.MissionsRequest;
import com.helpysoft.mima_api.dto.MissionsResponse;
import com.helpysoft.mima_api.entity.MissionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MissionsService {
    MissionsResponse create(MissionsRequest request);
    MissionsResponse update(UUID trackingId, MissionsRequest request);
    MissionsResponse findByTrackingId(UUID trackingId);
    List<MissionsResponse> findByStatus(MissionStatus status);
    List<MissionsResponse> findByUnit(UUID unitTrackingId);
    List<MissionsResponse> findByPlannedStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<MissionsResponse> findAll();
    void delete(UUID trackingId);
}
