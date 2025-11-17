package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.AbsenceStatus;
import com.helpysoft.mima_api.entity.AbsenceType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AbsencesRequest {
    private AbsenceType absenceType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String justification;
    private AbsenceStatus status;
    private String reason;
    private UUID agentTrackingId;
    private UUID validatedByTrackingId;
}
