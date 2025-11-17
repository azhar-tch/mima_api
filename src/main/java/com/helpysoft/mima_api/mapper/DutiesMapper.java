package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.DutiesRequest;
import com.helpysoft.mima_api.dto.DutiesResponse;
import com.helpysoft.mima_api.entity.DutyStatus;
import com.helpysoft.mima_api.entity.Duties;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Units;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DutiesMapper {

    public Duties toEntity(DutiesRequest request, Agents agent, Units unit) {
        Duties duty = new Duties();
        duty.setTrackingId(UUID.randomUUID());
        duty.setPosition(request.getPosition());
        duty.setDutyType(request.getDutyType());
        duty.setStartDate(request.getStartDate());
        duty.setEndDate(request.getEndDate());
        duty.setStatus(request.getStatus() != null ? request.getStatus() : DutyStatus.PLANNED);
        duty.setAgent(agent);
        duty.setUnit(unit);
        return duty;
    }

    public DutiesResponse toResponse(Duties duty) {
        DutiesResponse response = new DutiesResponse();
        response.setTrackingId(duty.getTrackingId());
        response.setPosition(duty.getPosition());
        response.setDutyType(duty.getDutyType());
        response.setStartDate(duty.getStartDate());
        response.setEndDate(duty.getEndDate());
        response.setStatus(duty.getStatus());

        // Ajouter les trackingIds et noms pour les relations
        if (duty.getAgent() != null) {
            response.setAgentTrackingId(duty.getAgent().getTrackingId());
            response.setAgentName(duty.getAgent().getRegistrationNo() + " - " +
                (duty.getAgent().getFirstName() != null && duty.getAgent().getLastName() != null ?
                    duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() :
                    duty.getAgent().getFirstName() != null ? duty.getAgent().getFirstName() : "N/A"));
        }

        if (duty.getUnit() != null) {
            response.setUnitTrackingId(duty.getUnit().getTrackingId());
            response.setUnitName(duty.getUnit().getName());
        }

        response.setCreateDate(duty.getCreateDate());
        return response;
    }
}
