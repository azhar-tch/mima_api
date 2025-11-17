package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AgentServicePositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentServicePositionHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgentServicePositionHistoryService {
    AgentServicePositionHistoryResponse create(AgentServicePositionHistoryRequest request);
    AgentServicePositionHistoryResponse update(UUID trackingId, AgentServicePositionHistoryRequest request);
    AgentServicePositionHistoryResponse findByTrackingId(UUID trackingId);
    List<AgentServicePositionHistoryResponse> findByAgentTrackingId(UUID agentTrackingId);
    List<AgentServicePositionHistoryResponse> findByServicePositionTrackingId(UUID positionTrackingId);
    List<AgentServicePositionHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    AgentServicePositionHistoryResponse findCurrentPositionByAgentTrackingId(UUID agentTrackingId);
    List<AgentServicePositionHistoryResponse> findAll();
    void delete(UUID trackingId);
}
