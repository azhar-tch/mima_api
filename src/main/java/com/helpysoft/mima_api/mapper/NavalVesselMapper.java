package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.NavalVesselRequest;
import com.helpysoft.mima_api.dto.NavalVesselResponse;
import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.NavalVesselStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NavalVesselMapper {

    public NavalVessels toEntity(NavalVesselRequest request) {
        NavalVessels vessel = new NavalVessels();
        vessel.setTrackingId(UUID.randomUUID());
        vessel.setVesselNumber(request.getVesselNumber());
        vessel.setVesselType(request.getVesselType());
        vessel.setVesselName(request.getVesselName());
        vessel.setHullNumber(request.getHullNumber());
        vessel.setYearCommissioned(request.getYearCommissioned());
        vessel.setDateCommissioned(request.getDateCommissioned());
        vessel.setDateDecommissioned(request.getDateDecommissioned());
        vessel.setLength(request.getLength());
        vessel.setWidth(request.getWidth());
        vessel.setDraft(request.getDraft());
        vessel.setDisplacement(request.getDisplacement());
        vessel.setMaxSpeed(request.getMaxSpeed());
        vessel.setCrewCapacity(request.getCrewCapacity());
        vessel.setFuelCapacity(request.getFuelCapacity());
        vessel.setRange(request.getRange());
        vessel.setArmament(request.getArmament());
        vessel.setElectronics(request.getElectronics());
        vessel.setEngineType(request.getEngineType());
        vessel.setEnginePower(request.getEnginePower());
        vessel.setHomePort(request.getHomePort());
        vessel.setOperationalStatus(request.getOperationalStatus() != null ?
                request.getOperationalStatus() : NavalVesselStatus.OPERATIONAL);
        vessel.setCurrentLocation(request.getCurrentLocation());
        vessel.setCurrentMission(request.getCurrentMission());
        vessel.setLastMaintenanceDate(request.getLastMaintenanceDate());
        vessel.setNextMaintenanceDate(request.getNextMaintenanceDate());
        vessel.setTotalOperationalHours(request.getTotalOperationalHours());
        vessel.setObservations(request.getObservations());
        vessel.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        return vessel;
    }

    public NavalVesselResponse toResponse(NavalVessels vessel) {
        NavalVesselResponse response = new NavalVesselResponse();
        response.setTrackingId(vessel.getTrackingId());
        response.setVesselNumber(vessel.getVesselNumber());
        response.setVesselType(vessel.getVesselType());
        response.setVesselName(vessel.getVesselName());
        response.setHullNumber(vessel.getHullNumber());
        response.setYearCommissioned(vessel.getYearCommissioned());
        response.setDateCommissioned(vessel.getDateCommissioned());
        response.setDateDecommissioned(vessel.getDateDecommissioned());
        response.setLength(vessel.getLength());
        response.setWidth(vessel.getWidth());
        response.setDraft(vessel.getDraft());
        response.setDisplacement(vessel.getDisplacement());
        response.setMaxSpeed(vessel.getMaxSpeed());
        response.setCrewCapacity(vessel.getCrewCapacity());
        response.setFuelCapacity(vessel.getFuelCapacity());
        response.setRange(vessel.getRange());
        response.setArmament(vessel.getArmament());
        response.setElectronics(vessel.getElectronics());
        response.setEngineType(vessel.getEngineType());
        response.setEnginePower(vessel.getEnginePower());
        response.setHomePort(vessel.getHomePort());
        response.setOperationalStatus(vessel.getOperationalStatus());
        response.setCurrentLocation(vessel.getCurrentLocation());
        response.setCurrentMission(vessel.getCurrentMission());
        response.setLastMaintenanceDate(vessel.getLastMaintenanceDate());
        response.setNextMaintenanceDate(vessel.getNextMaintenanceDate());
        response.setTotalOperationalHours(vessel.getTotalOperationalHours());
        response.setObservations(vessel.getObservations());
        response.setIsActive(vessel.getIsActive());
        response.setCreateDate(vessel.getCreateDate());
        return response;
    }
}
