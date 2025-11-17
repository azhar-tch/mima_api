package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AgentServicePositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentServicePositionHistoryResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentServicePositionHistory;
import com.helpysoft.mima_api.entity.ServicePosition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AgentServicePositionHistoryMapper {

    public AgentServicePositionHistory toEntity(AgentServicePositionHistoryRequest request, Agents agent, ServicePosition position) {
        AgentServicePositionHistory history = new AgentServicePositionHistory();
        history.setTrackingId(UUID.randomUUID());
        history.setAgent(agent);
        history.setServicePosition(position);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setRemarks(request.getRemarks());
        return history;
    }

    public AgentServicePositionHistoryResponse toResponse(AgentServicePositionHistory history) {
        AgentServicePositionHistoryResponse response = new AgentServicePositionHistoryResponse();
        response.setTrackingId(history.getTrackingId());
        response.setAgentTrackingId(history.getAgent().getTrackingId());
        response.setAgentName(history.getAgent().getFirstName() + " " + history.getAgent().getLastName());
        response.setAgentMatricule(history.getAgent().getMatricule());
        response.setServicePositionTrackingId(history.getServicePosition().getTrackingId());
        response.setPositionName(history.getServicePosition().getPositionName());
        response.setLocation(history.getServicePosition().getLocation());
        response.setStartDate(history.getStartDate());
        response.setEndDate(history.getEndDate());
        response.setDecisionReference(history.getDecisionReference());
        response.setRemarks(history.getRemarks());
        response.setCreateDate(history.getCreateDate());
        response.setUpdateDate(history.getUpdateDate());
        response.setCreatedBy(history.getCreatedBy());
        response.setUpdatedBy(history.getUpdatedBy());
        return response;
    }

    public void updateEntity(AgentServicePositionHistory history, AgentServicePositionHistoryRequest request, Agents agent, ServicePosition position) {
        history.setAgent(agent);
        history.setServicePosition(position);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setRemarks(request.getRemarks());
    }
}
