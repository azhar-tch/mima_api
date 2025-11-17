package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class HRFunctionResponse {
    private UUID trackingId;
    private String functionName;
    private String description;
    private String department;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String updatedBy;
}
