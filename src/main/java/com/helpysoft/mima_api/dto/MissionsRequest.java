package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.MissionStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MissionsRequest {
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

    // Méthodes pour plusieurs unités et agents
    private List<UUID> unitTrackingIds;
    private List<UUID> agentTrackingIds;

    // Liste des participants (via MissionParticipations - ancienne méthode)
    private List<UUID> participantTrackingIds;
}
