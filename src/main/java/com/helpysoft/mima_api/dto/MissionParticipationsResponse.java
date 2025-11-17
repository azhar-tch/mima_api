package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionParticipationsResponse {
    private UUID trackingId;
    @JsonProperty("mission")
    private String missionTitle;
    @JsonProperty("agent")
    private String agentName;
    private String missionRule;
    private Double hoursCompleted;
    private LocalDateTime createDate;
}
