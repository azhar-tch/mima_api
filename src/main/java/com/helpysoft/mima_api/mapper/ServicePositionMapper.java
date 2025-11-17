package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.ServicePositionRequest;
import com.helpysoft.mima_api.dto.ServicePositionResponse;
import com.helpysoft.mima_api.entity.ServicePosition;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ServicePositionMapper {

    public ServicePosition toEntity(ServicePositionRequest request) {
        ServicePosition position = new ServicePosition();
        position.setTrackingId(UUID.randomUUID());
        position.setPositionName(request.getPositionName());
        position.setPositionType(request.getPositionType());
        position.setLocation(request.getLocation());
        position.setUnit(request.getUnit());
        position.setDescription(request.getDescription());
        return position;
    }

    public ServicePositionResponse toResponse(ServicePosition position) {
        ServicePositionResponse response = new ServicePositionResponse();
        response.setTrackingId(position.getTrackingId());
        response.setPositionName(position.getPositionName());
        response.setPositionType(position.getPositionType());
        response.setLocation(position.getLocation());
        response.setUnit(position.getUnit());
        response.setDescription(position.getDescription());
        response.setCreateDate(position.getCreateDate());
        response.setUpdateDate(position.getUpdateDate());
        response.setCreatedBy(position.getCreatedBy());
        response.setUpdatedBy(position.getUpdatedBy());
        return response;
    }

    public void updateEntity(ServicePosition position, ServicePositionRequest request) {
        position.setPositionName(request.getPositionName());
        position.setPositionType(request.getPositionType());
        position.setLocation(request.getLocation());
        position.setUnit(request.getUnit());
        position.setDescription(request.getDescription());
    }
}
