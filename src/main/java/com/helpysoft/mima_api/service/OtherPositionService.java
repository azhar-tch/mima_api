package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.OtherPositionRequest;
import com.helpysoft.mima_api.dto.OtherPositionResponse;

import java.util.List;
import java.util.UUID;

public interface OtherPositionService {
    OtherPositionResponse create(OtherPositionRequest request);
    OtherPositionResponse update(UUID trackingId, OtherPositionRequest request);
    OtherPositionResponse findByTrackingId(UUID trackingId);
    OtherPositionResponse findByPositionName(String positionName);
    List<OtherPositionResponse> findByPositionType(String positionType);
    List<OtherPositionResponse> searchByName(String positionName);
    List<OtherPositionResponse> findAll();
    void delete(UUID trackingId);
}
