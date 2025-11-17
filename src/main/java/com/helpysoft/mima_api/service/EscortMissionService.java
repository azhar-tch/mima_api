package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.EscortMissionRequest;
import com.helpysoft.mima_api.dto.EscortMissionResponse;
import com.helpysoft.mima_api.entity.MissionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EscortMissionService {
    EscortMissionResponse create(EscortMissionRequest request);
    EscortMissionResponse update(UUID trackingId, EscortMissionRequest request);
    EscortMissionResponse findByTrackingId(UUID trackingId);
    EscortMissionResponse findByMissionNumber(String missionNumber);
    List<EscortMissionResponse> findByStatus(MissionStatus status);
    List<EscortMissionResponse> findByPeriod(LocalDateTime startDate, LocalDateTime endDate);
    List<EscortMissionResponse> findByCommercialShip(UUID shipTrackingId);
    List<EscortMissionResponse> findBySecurityAgency(UUID agencyTrackingId);
    List<EscortMissionResponse> findByNavalVessel(UUID vesselTrackingId);
    List<EscortMissionResponse> findByCommander(UUID commanderTrackingId);
    List<EscortMissionResponse> findAll();
    void delete(UUID trackingId);
}
