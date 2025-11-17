package com.helpysoft.mima_api.dto;

import lombok.Data;

@Data
public class HRGradeRequest {
    private String gradeName;
    private String description;
    private Integer hierarchyLevel;
}
