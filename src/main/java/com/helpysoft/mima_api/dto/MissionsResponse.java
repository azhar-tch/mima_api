package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpysoft.mima_api.entity.MissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionsResponse {
    private UUID trackingId;
    private String type;
    private String title;
    private String location;
    private String shipName;
    private String objective;
    private LocalDateTime plannedStartDate;
    private LocalDateTime plannedEndDate;
    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;
    private MissionStatus status;

    // Listes pour plusieurs unités et agents
    private List<String> unitNames;
    private List<UUID> unitTrackingIds;
    private List<String> agentNames;
    private List<UUID> agentTrackingIds;

    private LocalDateTime createDate;
}
