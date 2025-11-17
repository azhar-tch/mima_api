package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.ManagementRulesRequest;
import com.helpysoft.mima_api.dto.ManagementRulesResponse;

import java.util.List;
import java.util.UUID;

public interface ManagementRulesService {
    ManagementRulesResponse create(ManagementRulesRequest request);
    ManagementRulesResponse update(UUID trackingId, ManagementRulesRequest request);
    ManagementRulesResponse findByTrackingId(UUID trackingId);
    List<ManagementRulesResponse> findAll();
    void delete(UUID trackingId);
}
