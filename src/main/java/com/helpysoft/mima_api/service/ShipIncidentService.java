package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.ShipIncidentRequest;
import com.helpysoft.mima_api.dto.ShipIncidentResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShipIncidentService {
    ShipIncidentResponse create(ShipIncidentRequest request);
    ShipIncidentResponse update(UUID trackingId, ShipIncidentRequest request);
    ShipIncidentResponse findByTrackingId(UUID trackingId);
    List<ShipIncidentResponse> findByCommercialShip(UUID shipTrackingId);
    List<ShipIncidentResponse> findOngoingIncidents();
    List<ShipIncidentResponse> findByStatus(String status);
    List<ShipIncidentResponse> findByIncidentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ShipIncidentResponse> findByEventType(String eventType);
    List<ShipIncidentResponse> findBySeverity(String severity);
    List<ShipIncidentResponse> findIncidentsWithPollution();
    List<ShipIncidentResponse> findByMaritimeZone(String maritimeZone);
    List<ShipIncidentResponse> findAll();
    List<ShipIncidentResponse> searchShipIncidents(String searchTerm);
    void delete(UUID trackingId);
}
