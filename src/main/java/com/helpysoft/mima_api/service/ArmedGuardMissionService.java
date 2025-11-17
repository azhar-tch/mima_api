package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.ArmedGuardMissionRequest;
import com.helpysoft.mima_api.dto.ArmedGuardMissionResponse;
import com.helpysoft.mima_api.entity.MissionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ArmedGuardMissionService {
    ArmedGuardMissionResponse create(ArmedGuardMissionRequest request);
    ArmedGuardMissionResponse update(UUID trackingId, ArmedGuardMissionRequest request);
    ArmedGuardMissionResponse findByTrackingId(UUID trackingId);
    ArmedGuardMissionResponse findByMissionNumber(String missionNumber);
    List<ArmedGuardMissionResponse> findByStatus(MissionStatus status);
    List<ArmedGuardMissionResponse> findByPeriod(LocalDateTime startDate, LocalDateTime endDate);
    List<ArmedGuardMissionResponse> findByCommercialShip(UUID shipTrackingId);
    List<ArmedGuardMissionResponse> findBySecurityAgency(UUID agencyTrackingId);
    List<ArmedGuardMissionResponse> findAll();
    void delete(UUID trackingId);
}
