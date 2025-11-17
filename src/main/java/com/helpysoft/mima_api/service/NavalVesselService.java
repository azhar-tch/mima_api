package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.NavalVesselRequest;
import com.helpysoft.mima_api.dto.NavalVesselResponse;
import com.helpysoft.mima_api.entity.NavalVesselStatus;
import com.helpysoft.mima_api.entity.NavalVesselType;

import java.util.List;
import java.util.UUID;

public interface NavalVesselService {
    NavalVesselResponse create(NavalVesselRequest request);
    NavalVesselResponse update(UUID trackingId, NavalVesselRequest request);
    NavalVesselResponse findByTrackingId(UUID trackingId);
    NavalVesselResponse findByVesselNumber(String vesselNumber);
    List<NavalVesselResponse> findByVesselType(NavalVesselType vesselType);
    List<NavalVesselResponse> findByOperationalStatus(NavalVesselStatus status);
    List<NavalVesselResponse> findAvailableVessels();
    List<NavalVesselResponse> findPatrolVessels();
    List<NavalVesselResponse> findAll();
    void delete(UUID trackingId);
}
