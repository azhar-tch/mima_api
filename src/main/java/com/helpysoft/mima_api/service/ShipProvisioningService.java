package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.ShipProvisioningRequest;
import com.helpysoft.mima_api.dto.ShipProvisioningResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShipProvisioningService {
    ShipProvisioningResponse create(ShipProvisioningRequest request);
    ShipProvisioningResponse update(UUID trackingId, ShipProvisioningRequest request);
    ShipProvisioningResponse findByTrackingId(UUID trackingId);
    List<ShipProvisioningResponse> findByCommercialShip(UUID shipTrackingId);
    List<ShipProvisioningResponse> findByProvisioningDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ShipProvisioningResponse> findByProvisioningType(String provisioningType);
    List<ShipProvisioningResponse> findBySupplier(String supplierName);
    List<ShipProvisioningResponse> findProvisioningsWithDelay();
    List<ShipProvisioningResponse> findByProductType(String productType);
    List<ShipProvisioningResponse> findAll();
    List<ShipProvisioningResponse> searchShipProvisionings(String searchTerm);
    void delete(UUID trackingId);
}
