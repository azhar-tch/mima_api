package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.STSOperationRequest;
import com.helpysoft.mima_api.dto.STSOperationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface STSOperationService {
    STSOperationResponse create(STSOperationRequest request);
    STSOperationResponse update(UUID trackingId, STSOperationRequest request);
    STSOperationResponse findByTrackingId(UUID trackingId);
    STSOperationResponse findByOperationNumber(String operationNumber);
    List<STSOperationResponse> findByMotherVessel(UUID vesselTrackingId);
    List<STSOperationResponse> findByReceivingVessel(UUID vesselTrackingId);
    List<STSOperationResponse> findBySupervisingVessel(UUID vesselTrackingId);
    List<STSOperationResponse> findOngoingOperations();
    List<STSOperationResponse> findByStatus(String status);
    List<STSOperationResponse> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<STSOperationResponse> findByCargoType(String cargoType);
    List<STSOperationResponse> findOperationsWithPollution();
    List<STSOperationResponse> findByMaritimeZone(String maritimeZone);
    List<STSOperationResponse> findAll();
    void delete(UUID trackingId);
}
