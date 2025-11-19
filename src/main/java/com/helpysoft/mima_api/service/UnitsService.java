package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.UnitsRequest;
import com.helpysoft.mima_api.dto.UnitsResponse;
import com.helpysoft.mima_api.entity.UnitType;

import java.util.List;
import java.util.UUID;

public interface UnitsService {
    UnitsResponse create(UnitsRequest request);
    UnitsResponse update(UUID trackingId, UnitsRequest request);
    UnitsResponse findByTrackingId(UUID trackingId);
    List<UnitsResponse> findByType(UnitType type);
    List<UnitsResponse> findAll();
    List<UnitsResponse> searchUnits(String searchTerm);
    void delete(UUID trackingId);
}
