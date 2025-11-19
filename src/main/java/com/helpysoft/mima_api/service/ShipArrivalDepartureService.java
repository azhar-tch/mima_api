package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.ShipArrivalDepartureRequest;
import com.helpysoft.mima_api.dto.ShipArrivalDepartureResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShipArrivalDepartureService {
    ShipArrivalDepartureResponse create(ShipArrivalDepartureRequest request);
    ShipArrivalDepartureResponse update(UUID trackingId, ShipArrivalDepartureRequest request);
    ShipArrivalDepartureResponse findByTrackingId(UUID trackingId);
    List<ShipArrivalDepartureResponse> findByCommercialShip(UUID shipTrackingId);
    List<ShipArrivalDepartureResponse> findShipsCurrentlyInPort();
    List<ShipArrivalDepartureResponse> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ShipArrivalDepartureResponse> findByDepartureDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ShipArrivalDepartureResponse> findByPortOfOrigin(String portOfOrigin);
    List<ShipArrivalDepartureResponse> findByNextDestination(String nextDestination);
    List<ShipArrivalDepartureResponse> findAll();
    List<ShipArrivalDepartureResponse> searchShipArrivalDepartures(String searchTerm);
    void delete(UUID trackingId);
}
