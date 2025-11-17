package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.ConservatorSeizureRequest;
import com.helpysoft.mima_api.dto.ConservatorSeizureResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ConservatorSeizureService {
    ConservatorSeizureResponse create(ConservatorSeizureRequest request);
    ConservatorSeizureResponse update(UUID trackingId, ConservatorSeizureRequest request);
    ConservatorSeizureResponse findByTrackingId(UUID trackingId);
    List<ConservatorSeizureResponse> findByCommercialShip(UUID shipTrackingId);
    List<ConservatorSeizureResponse> findActiveSeizures();
    List<ConservatorSeizureResponse> findByStatus(String status);
    List<ConservatorSeizureResponse> findBySeizureDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ConservatorSeizureResponse> findBySeizingAuthority(String seizingAuthority);
    List<ConservatorSeizureResponse> findBySeizureType(String seizureType);
    List<ConservatorSeizureResponse> findAll();
    void delete(UUID trackingId);
}
