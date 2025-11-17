package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.RulesRequest;
import com.helpysoft.mima_api.dto.RulesResponse;

import java.util.List;
import java.util.UUID;

public interface RulesService {
    RulesResponse create(RulesRequest request);
    RulesResponse update(UUID trackingId, RulesRequest request);
    RulesResponse findByTrackingId(UUID trackingId);
    List<RulesResponse> findAll();
    void delete(UUID trackingId);
}
