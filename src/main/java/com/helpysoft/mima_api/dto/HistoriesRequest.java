package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.ActionType;
import lombok.Data;

import java.util.UUID;

@Data
public class HistoriesRequest {
    private UUID agentTrackingId;
    private String entityName;
    private UUID entityTrackingId;
    private ActionType actionType;
    private String changesSummary;
    private String oldValue;
    private String newValue;
    private String details;
}
