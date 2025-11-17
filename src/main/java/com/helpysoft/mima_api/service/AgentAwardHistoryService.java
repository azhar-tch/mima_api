package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AgentAwardHistoryRequest;
import com.helpysoft.mima_api.dto.AgentAwardHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgentAwardHistoryService {
    AgentAwardHistoryResponse create(AgentAwardHistoryRequest request);
    AgentAwardHistoryResponse update(UUID trackingId, AgentAwardHistoryRequest request);
    AgentAwardHistoryResponse findByTrackingId(UUID trackingId);
    List<AgentAwardHistoryResponse> findByAgentTrackingId(UUID agentTrackingId);
    List<AgentAwardHistoryResponse> findByAwardTrackingId(UUID awardTrackingId);
    List<AgentAwardHistoryResponse> findByAwardDateBetween(LocalDate startDate, LocalDate endDate);
    Long countByAgentTrackingId(UUID agentTrackingId);
    List<AgentAwardHistoryResponse> findAll();
    void delete(UUID trackingId);
}
