package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class HRGradeResponse {
    private UUID trackingId;
    private String gradeName;
    private String description;
    private Integer hierarchyLevel;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String updatedBy;
}
