package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpysoft.mima_api.entity.AbsenceStatus;
import com.helpysoft.mima_api.entity.AbsenceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbsencesResponse {
    private UUID trackingId;
    private AbsenceType absenceType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long numberOfDays;
    private String justification;
    private AbsenceStatus status;
    private String reason;
    private String agentName;
    private UUID agentTrackingId;
    @JsonProperty("validatedBy")
    private String validatedByName;
    private UUID validatedByTrackingId;
    private LocalDateTime createDate;
}
