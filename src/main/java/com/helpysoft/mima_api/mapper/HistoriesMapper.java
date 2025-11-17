package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.dto.HistoriesResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Histories;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HistoriesMapper {

    public Histories toEntity(HistoriesRequest request, Agents agent) {
        Histories history = new Histories();
        history.setTrackingId(UUID.randomUUID());
        history.setAgent(agent);
        history.setEntityName(request.getEntityName());
        history.setEntityTrackingId(request.getEntityTrackingId());
        history.setActionType(request.getActionType());
        history.setChangesSummary(request.getChangesSummary());
        history.setOldValue(request.getOldValue());
        history.setNewValue(request.getNewValue());
        history.setDetails(request.getDetails());
        return history;
    }

    public HistoriesResponse toResponse(Histories history) {
        HistoriesResponse response = new HistoriesResponse();
        response.setTrackingId(history.getTrackingId());
        response.setAgentName(history.getAgent() != null ?
            history.getAgent().getFirstName() + " " + history.getAgent().getLastName() : null);
        response.setEntityName(history.getEntityName());
        response.setEntityTrackingId(history.getEntityTrackingId());
        response.setActionType(history.getActionType());
        response.setChangesSummary(history.getChangesSummary());
        response.setOldValue(history.getOldValue());
        response.setNewValue(history.getNewValue());
        response.setDetails(history.getDetails());
        response.setCreateDate(history.getCreateDate());
        return response;
    }
}
