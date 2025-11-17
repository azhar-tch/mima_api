package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AwardResponse {
    private UUID trackingId;
    private String awardName;
    private String awardType;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createdBy;
    private String updatedBy;
}
