package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.OtherPositionRequest;
import com.helpysoft.mima_api.dto.OtherPositionResponse;
import com.helpysoft.mima_api.entity.OtherPosition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OtherPositionMapper {

    public OtherPosition toEntity(OtherPositionRequest request) {
        OtherPosition position = new OtherPosition();
        position.setTrackingId(UUID.randomUUID());
        position.setPositionName(request.getPositionName());
        position.setPositionType(request.getPositionType());
        position.setDescription(request.getDescription());
        return position;
    }

    public OtherPositionResponse toResponse(OtherPosition position) {
        OtherPositionResponse response = new OtherPositionResponse();
        response.setTrackingId(position.getTrackingId());
        response.setPositionName(position.getPositionName());
        response.setPositionType(position.getPositionType());
        response.setDescription(position.getDescription());
        response.setCreateDate(position.getCreateDate());
        response.setUpdateDate(position.getUpdateDate());
        response.setCreatedBy(position.getCreatedBy());
        response.setUpdatedBy(position.getUpdatedBy());
        return response;
    }

    public void updateEntity(OtherPosition position, OtherPositionRequest request) {
        position.setPositionName(request.getPositionName());
        position.setPositionType(request.getPositionType());
        position.setDescription(request.getDescription());
    }
}
