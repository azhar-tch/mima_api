package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.ServicePositionRequest;
import com.helpysoft.mima_api.dto.ServicePositionResponse;
import com.helpysoft.mima_api.entity.ServicePosition;
import com.helpysoft.mima_api.entity.Units;
import com.helpysoft.mima_api.repository.UnitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ServicePositionMapper {

    private final UnitsRepository unitsRepository;

    public ServicePosition toEntity(ServicePositionRequest request) {
        ServicePosition position = new ServicePosition();
        position.setTrackingId(UUID.randomUUID());
        position.setPositionName(request.getPositionName());
        position.setPositionType(request.getPositionType());
        position.setLocation(request.getLocation());

        if (request.getUnitTrackingId() != null) {
            Units unit = unitsRepository.findByTrackingId(request.getUnitTrackingId())
                    .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + request.getUnitTrackingId()));
            position.setUnit(unit);
        }

        position.setDescription(request.getDescription());
        return position;
    }

    public ServicePositionResponse toResponse(ServicePosition position) {
        ServicePositionResponse response = new ServicePositionResponse();
        response.setTrackingId(position.getTrackingId());
        response.setPositionName(position.getPositionName());
        response.setPositionType(position.getPositionType());
        response.setLocation(position.getLocation());

        if (position.getUnit() != null) {
            response.setUnitTrackingId(position.getUnit().getTrackingId());
            response.setUnitName(position.getUnit().getName());
        }

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

        if (request.getUnitTrackingId() != null) {
            Units unit = unitsRepository.findByTrackingId(request.getUnitTrackingId())
                    .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + request.getUnitTrackingId()));
            position.setUnit(unit);
        } else {
            position.setUnit(null);
        }

        position.setDescription(request.getDescription());
    }
}
