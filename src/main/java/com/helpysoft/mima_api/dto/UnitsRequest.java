package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.UnitStatus;
import com.helpysoft.mima_api.entity.UnitType;
import lombok.Data;

import java.util.UUID;

@Data
public class UnitsRequest {
    private String name;
    private String description;
    private UnitType type;
    private UUID chiefTrackingId;
    private UnitStatus status;
}
