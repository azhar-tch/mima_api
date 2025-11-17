package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AgentFunctionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentFunctionHistoryResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentFunctionHistory;
import com.helpysoft.mima_api.entity.HRFunction;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AgentFunctionHistoryMapper {

    public AgentFunctionHistory toEntity(AgentFunctionHistoryRequest request, Agents agent, HRFunction function) {
        AgentFunctionHistory history = new AgentFunctionHistory();
        history.setTrackingId(UUID.randomUUID());
        history.setAgent(agent);
        history.setFunction(function);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setRemarks(request.getRemarks());
        return history;
    }

    public AgentFunctionHistoryResponse toResponse(AgentFunctionHistory history) {
        AgentFunctionHistoryResponse response = new AgentFunctionHistoryResponse();
        response.setTrackingId(history.getTrackingId());
        response.setAgentTrackingId(history.getAgent().getTrackingId());
        response.setAgentName(history.getAgent().getFirstName() + " " + history.getAgent().getLastName());
        response.setAgentMatricule(history.getAgent().getMatricule());
        response.setFunctionTrackingId(history.getFunction().getTrackingId());
        response.setFunctionName(history.getFunction().getFunctionName());
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

    public void updateEntity(AgentFunctionHistory history, AgentFunctionHistoryRequest request, Agents agent, HRFunction function) {
        history.setAgent(agent);
        history.setFunction(function);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setRemarks(request.getRemarks());
    }
}
