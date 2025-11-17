package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AgentOtherPositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentOtherPositionHistoryResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentOtherPositionHistory;
import com.helpysoft.mima_api.entity.OtherPosition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AgentOtherPositionHistoryMapper {

    public AgentOtherPositionHistory toEntity(AgentOtherPositionHistoryRequest request, Agents agent, OtherPosition position) {
        AgentOtherPositionHistory history = new AgentOtherPositionHistory();
        history.setTrackingId(UUID.randomUUID());
        history.setAgent(agent);
        history.setOtherPosition(position);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setLocation(request.getLocation());
        history.setRemarks(request.getRemarks());
        return history;
    }

    public AgentOtherPositionHistoryResponse toResponse(AgentOtherPositionHistory history) {
        AgentOtherPositionHistoryResponse response = new AgentOtherPositionHistoryResponse();
        response.setTrackingId(history.getTrackingId());
        response.setAgentTrackingId(history.getAgent().getTrackingId());
        response.setAgentName(history.getAgent().getFirstName() + " " + history.getAgent().getLastName());
        response.setAgentMatricule(history.getAgent().getMatricule());
        response.setOtherPositionTrackingId(history.getOtherPosition().getTrackingId());
        response.setPositionName(history.getOtherPosition().getPositionName());
        response.setPositionType(history.getOtherPosition().getPositionType());
        response.setStartDate(history.getStartDate());
        response.setEndDate(history.getEndDate());
        response.setLocation(history.getLocation());
        response.setRemarks(history.getRemarks());
        response.setCreateDate(history.getCreateDate());
        response.setUpdateDate(history.getUpdateDate());
        response.setCreatedBy(history.getCreatedBy());
        response.setUpdatedBy(history.getUpdatedBy());
        return response;
    }

    public void updateEntity(AgentOtherPositionHistory history, AgentOtherPositionHistoryRequest request, Agents agent, OtherPosition position) {
        history.setAgent(agent);
        history.setOtherPosition(position);
        history.setStartDate(request.getStartDate());
        history.setEndDate(request.getEndDate());
        history.setLocation(request.getLocation());
        history.setRemarks(request.getRemarks());
    }
}
