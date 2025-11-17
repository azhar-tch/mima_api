package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AwardRequest;
import com.helpysoft.mima_api.dto.AwardResponse;

import java.util.List;
import java.util.UUID;

public interface AwardService {
    AwardResponse create(AwardRequest request);
    AwardResponse update(UUID trackingId, AwardRequest request);
    AwardResponse findByTrackingId(UUID trackingId);
    AwardResponse findByAwardName(String awardName);
    List<AwardResponse> findByAwardType(String awardType);
    List<AwardResponse> searchByName(String awardName);
    List<AwardResponse> findAll();
    void delete(UUID trackingId);
}
