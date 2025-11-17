package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AgentAwardHistoryRequest;
import com.helpysoft.mima_api.dto.AgentAwardHistoryResponse;
import com.helpysoft.mima_api.entity.Agent;
import com.helpysoft.mima_api.entity.AgentAwardHistory;
import com.helpysoft.mima_api.entity.Award;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AgentAwardHistoryMapper {

    public AgentAwardHistory toEntity(AgentAwardHistoryRequest request, Agent agent, Award award) {
        AgentAwardHistory history = new AgentAwardHistory();
        history.setTrackingId(UUID.randomUUID());
        history.setAgent(agent);
        history.setAward(award);
        history.setAwardDate(request.getAwardDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setMotive(request.getMotive());
        history.setRemarks(request.getRemarks());
        return history;
    }

    public AgentAwardHistoryResponse toResponse(AgentAwardHistory history) {
        AgentAwardHistoryResponse response = new AgentAwardHistoryResponse();
        response.setTrackingId(history.getTrackingId());
        response.setAgentTrackingId(history.getAgent().getTrackingId());
        response.setAgentName(history.getAgent().getFirstName() + " " + history.getAgent().getLastName());
        response.setAgentMatricule(history.getAgent().getMatricule());
        response.setAwardTrackingId(history.getAward().getTrackingId());
        response.setAwardName(history.getAward().getAwardName());
        response.setAwardDate(history.getAwardDate());
        response.setDecisionReference(history.getDecisionReference());
        response.setMotive(history.getMotive());
        response.setRemarks(history.getRemarks());
        response.setCreateDate(history.getCreateDate());
        response.setUpdateDate(history.getUpdateDate());
        response.setCreatedBy(history.getCreatedBy());
        response.setUpdatedBy(history.getUpdatedBy());
        return response;
    }

    public void updateEntity(AgentAwardHistory history, AgentAwardHistoryRequest request, Agent agent, Award award) {
        history.setAgent(agent);
        history.setAward(award);
        history.setAwardDate(request.getAwardDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setMotive(request.getMotive());
        history.setRemarks(request.getRemarks());
    }
}
