package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.AlertStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class AlertsRequest {
    private String alertType;
    private String description;
    private String level;
    private AlertStatus status;
    private UUID agentTrackingId;
}
