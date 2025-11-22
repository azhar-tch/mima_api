package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.helpysoft.mima_api.config.FlexibleLocalDateTimeDeserializer;
import com.helpysoft.mima_api.entity.DutyStatus;
import com.helpysoft.mima_api.entity.DutyType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DutiesRequest {
    private String position;
    private DutyType dutyType;
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime startDate;
    @JsonDeserialize(using = FlexibleLocalDateTimeDeserializer.class)
    private LocalDateTime endDate;
    private DutyStatus status;
    private UUID agentTrackingId;
    private UUID unitTrackingId;
}
