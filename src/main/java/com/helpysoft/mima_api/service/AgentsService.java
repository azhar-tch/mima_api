package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AgentsRequest;
import com.helpysoft.mima_api.dto.AgentsResponse;
import com.helpysoft.mima_api.entity.MarinerStatus;

import java.util.List;
import java.util.UUID;

public interface AgentsService {
    AgentsResponse create(AgentsRequest request);
    AgentsResponse update(UUID trackingId, AgentsRequest request);
    AgentsResponse findByTrackingId(UUID trackingId);
    List<AgentsResponse> findByStatus(MarinerStatus status);
    List<AgentsResponse> findByUnit(UUID unitTrackingId);
    List<AgentsResponse> findAll();
    List<AgentsResponse> searchAgents(String searchTerm);
    void delete(UUID trackingId);
}
