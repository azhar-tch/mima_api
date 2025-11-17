package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AgentTrainingHistoryRequest;
import com.helpysoft.mima_api.dto.AgentTrainingHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgentTrainingHistoryService {
    AgentTrainingHistoryResponse create(AgentTrainingHistoryRequest request);
    AgentTrainingHistoryResponse update(UUID trackingId, AgentTrainingHistoryRequest request);
    AgentTrainingHistoryResponse findByTrackingId(UUID trackingId);
    List<AgentTrainingHistoryResponse> findByAgentTrackingId(UUID agentTrackingId);
    List<AgentTrainingHistoryResponse> findByTrainingTrackingId(UUID trainingTrackingId);
    List<AgentTrainingHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<AgentTrainingHistoryResponse> findOngoingTrainingsByAgentTrackingId(UUID agentTrackingId);
    List<AgentTrainingHistoryResponse> findAll();
    void delete(UUID trackingId);
}
