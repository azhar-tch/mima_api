package com.helpysoft.mima_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    // Statistiques principales
    private Long totalAgents;
    private Long availableAgents;
    private Long ongoingMissions;
    private Long dutiesThisWeek;
    private Long pendingAbsences;

    // Statistiques complémentaires
    private Long newMissionsThisWeek;
    private Long upcomingDutiesThisWeek;

    // Statistiques par statut d'agent
    private Long agentsOnMission;
    private Long agentsOnDuty;
    private Long agentsResting;
    private Long agentsAbsent;

    // Statistiques des missions
    private Long plannedMissions;
    private Long completedMissions;
    private Long cancelledMissions;

    // Statistiques des absences
    private Long approvedAbsences;
    private Long rejectedAbsences;
}