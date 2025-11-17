package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AgentGradeHistoryRequest;
import com.helpysoft.mima_api.dto.AgentGradeHistoryResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AgentGradeHistoryService {
    AgentGradeHistoryResponse create(AgentGradeHistoryRequest request);
    AgentGradeHistoryResponse update(UUID trackingId, AgentGradeHistoryRequest request);
    AgentGradeHistoryResponse findByTrackingId(UUID trackingId);
    List<AgentGradeHistoryResponse> findByAgentTrackingId(UUID agentTrackingId);
    List<AgentGradeHistoryResponse> findByGradeTrackingId(UUID gradeTrackingId);
    List<AgentGradeHistoryResponse> findByPromotionDateBetween(LocalDate startDate, LocalDate endDate);
    AgentGradeHistoryResponse findLatestGradeByAgentTrackingId(UUID agentTrackingId);
    List<AgentGradeHistoryResponse> findAll();
    void delete(UUID trackingId);
}
