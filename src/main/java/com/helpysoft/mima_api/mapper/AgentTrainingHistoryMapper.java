package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AgentTrainingHistoryRequest;
import com.helpysoft.mima_api.dto.AgentTrainingHistoryResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentTrainingHistory;
import com.helpysoft.mima_api.entity.Training;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AgentTrainingHistoryMapper {

    public AgentTrainingHistory toEntity(AgentTrainingHistoryRequest request, Agents agent, Training training) {
        AgentTrainingHistory history = new AgentTrainingHistory();
        history.setTrackingId(UUID.randomUUID());
        history.setAgent(agent);
        history.setTraining(training);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setDiploma(request.getDiploma());
        history.setRemarks(request.getRemarks());
        return history;
    }

    public AgentTrainingHistoryResponse toResponse(AgentTrainingHistory history) {
        AgentTrainingHistoryResponse response = new AgentTrainingHistoryResponse();
        response.setTrackingId(history.getTrackingId());
        response.setAgentTrackingId(history.getAgent().getTrackingId());
        response.setAgentName(history.getAgent().getFirstName() + " " + history.getAgent().getLastName());
        response.setAgentMatricule(history.getAgent().getRegistrationNo());
        response.setTrainingTrackingId(history.getTraining().getTrackingId());
        response.setTrainingName(history.getTraining().getTrainingName());
        response.setStartDate(history.getStartDate());
        response.setEndDate(history.getEndDate());
        response.setDiploma(history.getDiploma());
        response.setRemarks(history.getRemarks());
        response.setCreateDate(history.getCreateDate());
        response.setUpdateDate(history.getUpdateDate());
        response.setCreatedBy(history.getCreatedBy());
        response.setUpdatedBy(history.getUpdatedBy());
        return response;
    }

    public void updateEntity(AgentTrainingHistory history, AgentTrainingHistoryRequest request, Agents agent, Training training) {
        history.setAgent(agent);
        history.setTraining(training);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setDiploma(request.getDiploma());
        history.setRemarks(request.getRemarks());
    }
}
