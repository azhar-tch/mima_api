package com.helpysoft.mima_api.dto;

import lombok.Data;

@Data
public class HRFunctionRequest {
    private String functionName;
    private String description;
    private String department;
}
