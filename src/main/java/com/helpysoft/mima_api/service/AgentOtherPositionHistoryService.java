package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AgentOtherPositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentOtherPositionHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgentOtherPositionHistoryService {
    AgentOtherPositionHistoryResponse create(AgentOtherPositionHistoryRequest request);
    AgentOtherPositionHistoryResponse update(UUID trackingId, AgentOtherPositionHistoryRequest request);
    AgentOtherPositionHistoryResponse findByTrackingId(UUID trackingId);
    List<AgentOtherPositionHistoryResponse> findByAgentTrackingId(UUID agentTrackingId);
    List<AgentOtherPositionHistoryResponse> findByOtherPositionTrackingId(UUID positionTrackingId);
    List<AgentOtherPositionHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<AgentOtherPositionHistoryResponse> findOngoingPositionsByAgentTrackingId(UUID agentTrackingId);
    List<AgentOtherPositionHistoryResponse> findAll();
    void delete(UUID trackingId);
}
