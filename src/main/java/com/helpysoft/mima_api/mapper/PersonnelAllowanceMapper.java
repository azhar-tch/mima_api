package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.PersonnelAllowanceRequest;
import com.helpysoft.mima_api.dto.PersonnelAllowanceResponse;
import com.helpysoft.mima_api.entity.PersonnelAllowance;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PersonnelAllowanceMapper {

    public PersonnelAllowance toEntity(PersonnelAllowanceRequest request) {
        PersonnelAllowance allowance = new PersonnelAllowance();
        allowance.setTrackingId(UUID.randomUUID());
        allowance.setRankCode(request.getRankCode());
        allowance.setMaritimeRank(request.getMaritimeRank());
        allowance.setEscortDailyAllowance(request.getEscortDailyAllowance());
        allowance.setArmedGuardDailyAllowance(request.getArmedGuardDailyAllowance());
        allowance.setPatrolAllowance(request.getPatrolAllowance());
        allowance.setRiskAllowance(request.getRiskAllowance());
        allowance.setSeaAllowance(request.getSeaAllowance());
        allowance.setCurrency(request.getCurrency() != null ? request.getCurrency() : "XOF");
        allowance.setObservations(request.getObservations());
        allowance.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        return allowance;
    }

    public PersonnelAllowanceResponse toResponse(PersonnelAllowance allowance) {
        PersonnelAllowanceResponse response = new PersonnelAllowanceResponse();
        response.setTrackingId(allowance.getTrackingId());
        response.setRankCode(allowance.getRankCode());
        response.setMaritimeRank(allowance.getMaritimeRank());
        response.setEscortDailyAllowance(allowance.getEscortDailyAllowance());
        response.setArmedGuardDailyAllowance(allowance.getArmedGuardDailyAllowance());
        response.setPatrolAllowance(allowance.getPatrolAllowance());
        response.setRiskAllowance(allowance.getRiskAllowance());
        response.setSeaAllowance(allowance.getSeaAllowance());
        response.setCurrency(allowance.getCurrency());
        response.setObservations(allowance.getObservations());
        response.setIsActive(allowance.getIsActive());
        response.setCreateDate(allowance.getCreateDate());
        return response;
    }
}
