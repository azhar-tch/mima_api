package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AgentCompanyHistoryRequest;
import com.helpysoft.mima_api.dto.AgentCompanyHistoryResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentCompanyHistory;
import com.helpysoft.mima_api.entity.BMLCompany;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AgentCompanyHistoryMapper {

    public AgentCompanyHistory toEntity(AgentCompanyHistoryRequest request, Agents agent, BMLCompany company) {
        AgentCompanyHistory history = new AgentCompanyHistory();
        history.setTrackingId(UUID.randomUUID());
        history.setAgent(agent);
        history.setCompany(company);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setRemarks(request.getRemarks());
        return history;
    }

    public AgentCompanyHistoryResponse toResponse(AgentCompanyHistory history) {
        AgentCompanyHistoryResponse response = new AgentCompanyHistoryResponse();
        response.setTrackingId(history.getTrackingId());
        response.setAgentTrackingId(history.getAgent().getTrackingId());
        response.setAgentName(history.getAgent().getFirstName() + " " + history.getAgent().getLastName());
        response.setAgentMatricule(history.getAgent().getMatricule());
        response.setCompanyTrackingId(history.getCompany().getTrackingId());
        response.setCompanyName(history.getCompany().getCompanyName());
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

    public void updateEntity(AgentCompanyHistory history, AgentCompanyHistoryRequest request, Agents agent, BMLCompany company) {
        history.setAgent(agent);
        history.setCompany(company);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setRemarks(request.getRemarks());
    }
}
