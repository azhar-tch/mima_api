package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AlertsRequest;
import com.helpysoft.mima_api.dto.AlertsResponse;
import com.helpysoft.mima_api.entity.AlertStatus;
import com.helpysoft.mima_api.entity.Alerts;
import com.helpysoft.mima_api.entity.Agents;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AlertsMapper {

    public Alerts toEntity(AlertsRequest request, Agents agent) {
        Alerts alert = new Alerts();
        alert.setTrackingId(UUID.randomUUID());
        alert.setAlertType(request.getAlertType());
        alert.setDescription(request.getDescription());
        alert.setLevel(request.getLevel());
        alert.setStatus(request.getStatus() != null ? request.getStatus() : AlertStatus.NEW);
        alert.setAgent(agent);
        return alert;
    }

    public AlertsResponse toResponse(Alerts alert) {
        AlertsResponse response = new AlertsResponse();
        response.setTrackingId(alert.getTrackingId());
        response.setAlertType(alert.getAlertType());
        response.setDescription(alert.getDescription());
        response.setLevel(alert.getLevel());
        response.setStatus(alert.getStatus());
        response.setAgentName(alert.getAgent() != null ?
            alert.getAgent().getRegistrationNo() + " - " +
            (alert.getAgent().getLastName() != null ?
                alert.getAgent().getFirstName() + " " + alert.getAgent().getLastName() :
                "N/A") : null);
        response.setCreateDate(alert.getCreateDate());
        return response;
    }
}
