package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpysoft.mima_api.entity.UnitStatus;
import com.helpysoft.mima_api.entity.UnitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitsResponse {
    private UUID trackingId;
    private String name;
    private String description;
    private UnitType type;
    private UUID chiefTrackingId;
    private String chiefName;
    private UnitStatus status;
    private LocalDateTime createDate;
}
