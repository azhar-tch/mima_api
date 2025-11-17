package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.MissionParticipationsRequest;
import com.helpysoft.mima_api.dto.MissionParticipationsResponse;

import java.util.List;
import java.util.UUID;

public interface MissionParticipationsService {
    MissionParticipationsResponse create(MissionParticipationsRequest request);
    MissionParticipationsResponse update(UUID trackingId, MissionParticipationsRequest request);
    MissionParticipationsResponse findByTrackingId(UUID trackingId);
    List<MissionParticipationsResponse> findByMission(UUID missionTrackingId);
    List<MissionParticipationsResponse> findByAgent(UUID agentTrackingId);
    List<MissionParticipationsResponse> findAll();
    void delete(UUID trackingId);
}
