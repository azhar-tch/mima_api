package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.ArmedGuardMissionRequest;
import com.helpysoft.mima_api.dto.ArmedGuardMissionResponse;
import com.helpysoft.mima_api.entity.ArmedGuardMission;
import com.helpysoft.mima_api.entity.CommercialShip;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.entity.SecurityAgency;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ArmedGuardMissionMapper {

    public ArmedGuardMission toEntity(ArmedGuardMissionRequest request, CommercialShip ship, SecurityAgency agency) {
        ArmedGuardMission mission = new ArmedGuardMission();
        mission.setTrackingId(UUID.randomUUID());
        mission.setCommercialShip(ship);
        mission.setSecurityAgency(agency);
        mission.setEmbarkationDate(request.getEmbarkationDate());
        mission.setDisembarkationDate(request.getDisembarkationDate());
        mission.setEmbarkationPort(request.getEmbarkationPort());
        mission.setDisembarkationPort(request.getDisembarkationPort());
        mission.setPersonnelCount(request.getPersonnelCount());
        mission.setPatrolZone(request.getPatrolZone());
        mission.setStatus(request.getStatus() != null ? request.getStatus() : MissionStatus.EN_COURS);
        mission.setIncidents(request.getIncidents());
        mission.setObservations(request.getObservations());
        return mission;
    }

    public ArmedGuardMissionResponse toResponse(ArmedGuardMission mission) {
        ArmedGuardMissionResponse response = new ArmedGuardMissionResponse();
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

        response.setEmbarkationDate(mission.getEmbarkationDate());
        response.setDisembarkationDate(mission.getDisembarkationDate());
        response.setEmbarkationPort(mission.getEmbarkationPort());
        response.setDisembarkationPort(mission.getDisembarkationPort());
        response.setDaysCount(mission.getDaysCount());
        response.setPersonnelCount(mission.getPersonnelCount());
        response.setDurationInHours(mission.getDurationInHours());
        response.setPatrolZone(mission.getPatrolZone());
        response.setStatus(mission.getStatus());
        response.setIncidents(mission.getIncidents());
        response.setObservations(mission.getObservations());
        response.setCreateDate(mission.getCreateDate());

        return response;
    }
}
