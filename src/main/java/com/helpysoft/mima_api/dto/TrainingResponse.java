package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TrainingResponse {
    private UUID trackingId;
    private String trainingName;
    private String trainingType;
    private String description;
    private String institution;
    private String country;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String updatedBy;
}
