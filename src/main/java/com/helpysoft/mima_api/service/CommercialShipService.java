package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.CommercialShipRequest;
import com.helpysoft.mima_api.dto.CommercialShipResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CommercialShipService {
    CommercialShipResponse create(CommercialShipRequest request);
    CommercialShipResponse update(UUID trackingId, CommercialShipRequest request);
    CommercialShipResponse findByTrackingId(UUID trackingId);
    CommercialShipResponse findByImoNumber(String imoNumber);
    List<CommercialShipResponse> findByShipType(String shipType);
    List<CommercialShipResponse> findByStatus(String status);
    List<CommercialShipResponse> findByFlag(String flag);
    List<CommercialShipResponse> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<CommercialShipResponse> findAll();
    List<CommercialShipResponse> searchCommercialShips(String searchTerm);
    void delete(UUID trackingId);
}
