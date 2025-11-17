package com.helpysoft.mima_api.dto;

import lombok.Data;

@Data
public class TrainingRequest {
    private String trainingName;
    private String trainingType;
    private String description;
    private String institution;
    private String country;
}
