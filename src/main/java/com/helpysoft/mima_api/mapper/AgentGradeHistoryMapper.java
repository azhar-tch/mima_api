package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AgentGradeHistoryRequest;
import com.helpysoft.mima_api.dto.AgentGradeHistoryResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentGradeHistory;
import com.helpysoft.mima_api.entity.HRGrade;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AgentGradeHistoryMapper {

    public AgentGradeHistory toEntity(AgentGradeHistoryRequest request, Agents agent, HRGrade grade) {
        AgentGradeHistory history = new AgentGradeHistory();
        history.setTrackingId(UUID.randomUUID());
        history.setAgent(agent);
        history.setGrade(grade);
        history.setPromotionDate(request.getPromotionDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setRemarks(request.getRemarks());
        return history;
    }

    public AgentGradeHistoryResponse toResponse(AgentGradeHistory history) {
        AgentGradeHistoryResponse response = new AgentGradeHistoryResponse();
        response.setTrackingId(history.getTrackingId());
        response.setAgentTrackingId(history.getAgent().getTrackingId());
        response.setAgentName(history.getAgent().getFirstName() + " " + history.getAgent().getLastName());
        response.setAgentMatricule(history.getAgent().getRegistrationNo());
        response.setGradeTrackingId(history.getGrade().getTrackingId());
        response.setGradeName(history.getGrade().getGradeName());
        response.setPromotionDate(history.getPromotionDate());
        response.setDecisionReference(history.getDecisionReference());
        response.setRemarks(history.getRemarks());
        response.setCreateDate(history.getCreateDate());
        response.setUpdateDate(history.getUpdateDate());
        response.setCreatedBy(history.getCreatedBy());
        response.setUpdatedBy(history.getUpdatedBy());
        return response;
    }

    public void updateEntity(AgentGradeHistory history, AgentGradeHistoryRequest request, Agents agent, HRGrade grade) {
        history.setAgent(agent);
        history.setGrade(grade);
        history.setPromotionDate(request.getPromotionDate());
        history.setDecisionReference(request.getDecisionReference());
        history.setRemarks(request.getRemarks());
    }
}
