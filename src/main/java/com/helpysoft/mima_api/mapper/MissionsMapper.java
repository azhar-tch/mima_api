package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.MissionsRequest;
import com.helpysoft.mima_api.dto.MissionsResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.entity.Missions;
import com.helpysoft.mima_api.entity.Units;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MissionsMapper {

    public Missions toEntity(MissionsRequest request, Set<Units> units, Set<Agents> agents) {
        Missions mission = new Missions();
        mission.setTrackingId(UUID.randomUUID());
        mission.setType(request.getType());
        mission.setTitle(request.getTitle());
        mission.setLocation(request.getLocation());
        mission.setShipName(request.getShipName());
        mission.setObjective(request.getObjective());
        mission.setPlannedStartDate(request.getPlannedStartDate());
        mission.setPlannedEndDate(request.getPlannedEndDate());
        mission.setActualStartDate(request.getActualStartDate());
        mission.setActualEndDate(request.getActualEndDate());
        mission.setStatus(request.getStatus() != null ? request.getStatus() : MissionStatus.PLANNED);
        if (units != null && !units.isEmpty()) {
            mission.getUnits().addAll(units);
        }

        if (agents != null && !agents.isEmpty()) {
            mission.getAgents().addAll(agents);
        }
        return mission;
    }

    public MissionsResponse toResponse(Missions mission) {
        MissionsResponse response = new MissionsResponse();
        response.setTrackingId(mission.getTrackingId());
        response.setType(mission.getType());
        response.setTitle(mission.getTitle());
        response.setLocation(mission.getLocation());
        response.setShipName(mission.getShipName());
        response.setObjective(mission.getObjective());
        response.setPlannedStartDate(mission.getPlannedStartDate());
        response.setPlannedEndDate(mission.getPlannedEndDate());
        response.setActualStartDate(mission.getActualStartDate());
        response.setActualEndDate(mission.getActualEndDate());
        response.setStatus(mission.getStatus());

        // Listes pour plusieurs unités
        if (mission.getUnits() != null && !mission.getUnits().isEmpty()) {
            response.setUnitNames(mission.getUnits().stream()
                    .map(Units::getName)
                    .collect(Collectors.toList()));
            response.setUnitTrackingIds(mission.getUnits().stream()
                    .map(Units::getTrackingId)
                    .collect(Collectors.toList()));
        }

        // Listes pour plusieurs agents
        if (mission.getAgents() != null && !mission.getAgents().isEmpty()) {
            response.setAgentNames(mission.getAgents().stream()
                    .map(agent -> agent.getFirstName() + " " + agent.getLastName())
                    .collect(Collectors.toList()));
            response.setAgentTrackingIds(mission.getAgents().stream()
                    .map(Agents::getTrackingId)
                    .collect(Collectors.toList()));
        }

        response.setCreateDate(mission.getCreateDate());
        return response;
    }
}
