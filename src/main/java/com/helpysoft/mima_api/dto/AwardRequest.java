package com.helpysoft.mima_api.dto;

import lombok.Data;

@Data
public class AwardRequest {
    private String awardName;
    private String awardType;
    private String description;
}
