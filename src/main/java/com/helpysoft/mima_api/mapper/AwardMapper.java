package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AwardRequest;
import com.helpysoft.mima_api.dto.AwardResponse;
import com.helpysoft.mima_api.entity.Award;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AwardMapper {

    public Award toEntity(AwardRequest request) {
        Award award = new Award();
        award.setTrackingId(UUID.randomUUID());
        award.setAwardName(request.getAwardName());
        award.setAwardType(request.getAwardType());
        award.setDescription(request.getDescription());
        return award;
    }

    public AwardResponse toResponse(Award award) {
        AwardResponse response = new AwardResponse();
        response.setTrackingId(award.getTrackingId());
        response.setAwardName(award.getAwardName());
        response.setAwardType(award.getAwardType());
        response.setDescription(award.getDescription());
        response.setCreateDate(award.getCreateDate());
        response.setUpdateDate(award.getUpdateDate());
        response.setCreatedBy(award.getCreatedBy());
        response.setUpdatedBy(award.getUpdatedBy());
        return response;
    }

    public void updateEntity(Award award, AwardRequest request) {
        award.setAwardName(request.getAwardName());
        award.setAwardType(request.getAwardType());
        award.setDescription(request.getDescription());
    }
}
