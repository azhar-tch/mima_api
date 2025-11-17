package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.UnitsRequest;
import com.helpysoft.mima_api.dto.UnitsResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.UnitStatus;
import com.helpysoft.mima_api.entity.Units;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UnitsMapper {

    public Units toEntity(UnitsRequest request, Agents chief) {
        Units unit = new Units();
        unit.setTrackingId(UUID.randomUUID());
        unit.setName(request.getName());
        unit.setDescription(request.getDescription());
        unit.setType(request.getType());
        unit.setChief(chief);
        unit.setStatus(UnitStatus.ACTIVE);
        return unit;
    }

    public UnitsResponse toResponse(Units unit) {
        UnitsResponse response = new UnitsResponse();
        response.setTrackingId(unit.getTrackingId());
        response.setName(unit.getName());
        response.setDescription(unit.getDescription());
        response.setType(unit.getType());
        response.setChiefTrackingId(unit.getChief() != null ? unit.getChief().getTrackingId() : null);
        response.setChiefName(unit.getChief() != null ?
            unit.getChief().getFirstName() + " " + unit.getChief().getLastName() : null);
        response.setStatus(unit.getStatus());
        response.setCreateDate(unit.getCreateDate());
        return response;
    }
}
