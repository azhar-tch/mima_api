package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.MissionParticipationsRequest;
import com.helpysoft.mima_api.dto.MissionParticipationsResponse;
import com.helpysoft.mima_api.entity.MissionParticipations;
import com.helpysoft.mima_api.entity.Missions;
import com.helpysoft.mima_api.entity.Agents;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MissionParticipationsMapper {

    public MissionParticipations toEntity(MissionParticipationsRequest request, Missions mission, Agents agent) {
        MissionParticipations participation = new MissionParticipations();
        participation.setTrackingId(UUID.randomUUID());
        participation.setMission(mission);
        participation.setAgent(agent);
        participation.setMissionRule(request.getMissionRule());
        participation.setHoursCompleted(request.getHoursCompleted() != null ? request.getHoursCompleted() : 0.0);
        return participation;
    }

    public MissionParticipationsResponse toResponse(MissionParticipations participation) {
        MissionParticipationsResponse response = new MissionParticipationsResponse();
        response.setTrackingId(participation.getTrackingId());
        response.setMissionTitle(participation.getMission() != null ? participation.getMission().getTitle() : null);
        response.setAgentName(participation.getAgent() != null ?
            participation.getAgent().getRegistrationNo() + " - " +
            (participation.getAgent().getLastName() != null ?
                participation.getAgent().getFirstName() + " " + participation.getAgent().getLastName() :
                "N/A") : null);
        response.setMissionRule(participation.getMissionRule());
        response.setHoursCompleted(participation.getHoursCompleted());
        response.setCreateDate(participation.getCreateDate());
        return response;
    }
}
