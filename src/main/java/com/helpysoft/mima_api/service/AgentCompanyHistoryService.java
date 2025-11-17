package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AgentCompanyHistoryRequest;
import com.helpysoft.mima_api.dto.AgentCompanyHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgentCompanyHistoryService {
    AgentCompanyHistoryResponse create(AgentCompanyHistoryRequest request);
    AgentCompanyHistoryResponse update(UUID trackingId, AgentCompanyHistoryRequest request);
    AgentCompanyHistoryResponse findByTrackingId(UUID trackingId);
    List<AgentCompanyHistoryResponse> findByAgentTrackingId(UUID agentTrackingId);
    List<AgentCompanyHistoryResponse> findByCompanyTrackingId(UUID companyTrackingId);
    List<AgentCompanyHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    AgentCompanyHistoryResponse findCurrentCompanyByAgentTrackingId(UUID agentTrackingId);
    List<AgentCompanyHistoryResponse> findCurrentMembersByCompanyTrackingId(UUID companyTrackingId);
    List<AgentCompanyHistoryResponse> findAll();
    void delete(UUID trackingId);
}
