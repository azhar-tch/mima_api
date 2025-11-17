package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.ServicePositionRequest;
import com.helpysoft.mima_api.dto.ServicePositionResponse;

import java.util.List;
import java.util.UUID;

public interface ServicePositionService {
    ServicePositionResponse create(ServicePositionRequest request);
    ServicePositionResponse update(UUID trackingId, ServicePositionRequest request);
    ServicePositionResponse findByTrackingId(UUID trackingId);
    ServicePositionResponse findByPositionName(String positionName);
    List<ServicePositionResponse> findByPositionType(String positionType);
    List<ServicePositionResponse> findByLocation(String location);
    List<ServicePositionResponse> findByUnit(String unit);
    List<ServicePositionResponse> searchByName(String positionName);
    List<ServicePositionResponse> findAll();
    void delete(UUID trackingId);
}
