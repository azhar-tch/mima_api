package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriesResponse {
    private UUID trackingId;
    private String agentName;
    private String entityName;
    private UUID entityTrackingId;
    private ActionType actionType;
    private String changesSummary;
    private String oldValue;
    private String newValue;
    private String details;
    private LocalDateTime createDate;
}
