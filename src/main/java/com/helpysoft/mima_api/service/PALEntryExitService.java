package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.PALEntryExitRequest;
import com.helpysoft.mima_api.dto.PALEntryExitResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PALEntryExitService {
    PALEntryExitResponse create(PALEntryExitRequest request);
    PALEntryExitResponse update(UUID trackingId, PALEntryExitRequest request);
    PALEntryExitResponse findByTrackingId(UUID trackingId);
    List<PALEntryExitResponse> findByCommercialShip(UUID shipTrackingId);
    List<PALEntryExitResponse> findShipsCurrentlyInPAL();
    List<PALEntryExitResponse> findByEntryDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<PALEntryExitResponse> findByExitDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<PALEntryExitResponse> findByAnchorageZone(String anchorageZone);
    List<PALEntryExitResponse> findByBerthNumber(String berthNumber);
    List<PALEntryExitResponse> findAll();
    void delete(UUID trackingId);
}
