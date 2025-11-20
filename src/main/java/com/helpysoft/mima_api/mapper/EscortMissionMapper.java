package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.EscortMissionRequest;
import com.helpysoft.mima_api.dto.EscortMissionResponse;
import com.helpysoft.mima_api.entity.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EscortMissionMapper {

    public EscortMissions toEntity(EscortMissionRequest request, CommercialShips ship,
                                   SecurityAgencies agency, NavalVessels vessel,
                                   Agents commander, NavalVessels secondaryVessel) {
        EscortMissions mission = new EscortMissions();
        mission.setTrackingId(UUID.randomUUID());
        if (request.getMissionNumber() != null && !request.getMissionNumber().trim().isEmpty()) {
            mission.setMissionNumber(request.getMissionNumber());
        }
        mission.setCommercialShip(ship);
        mission.setSecurityAgency(agency);
        mission.setNavalVessel(vessel);
        mission.setCommander(commander);
        mission.setCommanderRank(request.getCommanderRank());
        mission.setSecondaryVessel(secondaryVessel);
        mission.setVedettes(request.getVedettes());
        mission.setStartDate(request.getStartDate());
        mission.setEndDate(request.getEndDate());
        mission.setEscortType(request.getEscortType());
        mission.setDeparturePoint(request.getDeparturePoint());
        mission.setArrivalPoint(request.getArrivalPoint());
        mission.setDistance(request.getDistance());
        mission.setEscortZone(request.getEscortZone());
        mission.setStatus(request.getStatus() != null ? request.getStatus() : MissionStatus.IN_PROGRESS);
        mission.setIncidents(request.getIncidents());
        mission.setObservations(request.getObservations());
        return mission;
    }

    public EscortMissionResponse toResponse(EscortMissions mission) {
        EscortMissionResponse response = new EscortMissionResponse();
        response.setTrackingId(mission.getTrackingId());
        response.setMissionNumber(mission.getMissionNumber());

        // Commercial Ship info
        if (mission.getCommercialShip() != null) {
            response.setCommercialShipTrackingId(mission.getCommercialShip().getTrackingId());
            response.setCommercialShipName(mission.getCommercialShip().getShipName());
            response.setCommercialShipImoNumber(mission.getCommercialShip().getImoNumber());
        }

        // Security Agency info
        if (mission.getSecurityAgency() != null) {
            response.setSecurityAgencyTrackingId(mission.getSecurityAgency().getTrackingId());
            response.setSecurityAgencyName(mission.getSecurityAgency().getAgencyName());
            response.setSecurityAgencyNumber(mission.getSecurityAgency().getAgencyNumber());
        }

        // Naval Vessel info
        if (mission.getNavalVessel() != null) {
            response.setNavalVesselTrackingId(mission.getNavalVessel().getTrackingId());
            response.setNavalVesselName(mission.getNavalVessel().getVesselName());
            response.setNavalVesselNumber(mission.getNavalVessel().getVesselNumber());
        }

        // Commander info
        if (mission.getCommander() != null) {
            response.setCommanderTrackingId(mission.getCommander().getTrackingId());
            response.setCommanderName(mission.getCommander().getFirstName() + " " + mission.getCommander().getLastName());
        }
        response.setCommanderRank(mission.getCommanderRank());

        // Secondary Vessel info
        if (mission.getSecondaryVessel() != null) {
            response.setSecondaryVesselTrackingId(mission.getSecondaryVessel().getTrackingId());
            response.setSecondaryVesselName(mission.getSecondaryVessel().getVesselName());
        }

        response.setVedettes(mission.getVedettes());
        response.setStartDate(mission.getStartDate());
        response.setEndDate(mission.getEndDate());
        response.setDurationInHours(mission.getDurationInHours());
        response.setDurationInDays(mission.getDurationInDays());
        response.setEscortType(mission.getEscortType());
        response.setDeparturePoint(mission.getDeparturePoint());
        response.setArrivalPoint(mission.getArrivalPoint());
        response.setDistance(mission.getDistance());
        response.setEscortZone(mission.getEscortZone());
        response.setStatus(mission.getStatus());
        response.setIncidents(mission.getIncidents());
        response.setObservations(mission.getObservations());
        response.setCreateDate(mission.getCreateDate());

        return response;
    }
}
