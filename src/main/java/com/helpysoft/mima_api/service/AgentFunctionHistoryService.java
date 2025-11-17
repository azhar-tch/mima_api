package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AgentFunctionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentFunctionHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgentFunctionHistoryService {
    AgentFunctionHistoryResponse create(AgentFunctionHistoryRequest request);
    AgentFunctionHistoryResponse update(UUID trackingId, AgentFunctionHistoryRequest request);
    AgentFunctionHistoryResponse findByTrackingId(UUID trackingId);
    List<AgentFunctionHistoryResponse> findByAgentTrackingId(UUID agentTrackingId);
    List<AgentFunctionHistoryResponse> findByFunctionTrackingId(UUID functionTrackingId);
    List<AgentFunctionHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    AgentFunctionHistoryResponse findCurrentFunctionByAgentTrackingId(UUID agentTrackingId);
    List<AgentFunctionHistoryResponse> findAll();
    void delete(UUID trackingId);
}
