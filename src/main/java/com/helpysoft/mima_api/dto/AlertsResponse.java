package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpysoft.mima_api.entity.AlertStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertsResponse {
    private UUID trackingId;
    private String alertType;
    private String description;
    private String level;
    private AlertStatus status;
    @JsonProperty("agent")
    private String agentName;
    private LocalDateTime createDate;
}
