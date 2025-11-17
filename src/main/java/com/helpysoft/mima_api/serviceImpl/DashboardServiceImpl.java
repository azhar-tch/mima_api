package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.DashboardStatsResponse;
import com.helpysoft.mima_api.entity.AbsenceStatus;
import com.helpysoft.mima_api.entity.MarinerStatus;
import com.helpysoft.mima_api.entity.DutyStatus;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.repository.AbsencesRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.DutiesRepository;
import com.helpysoft.mima_api.repository.MissionsRepository;
import com.helpysoft.mima_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final AgentsRepository agentsRepository;
    private final MissionsRepository missionsRepository;
    private final DutiesRepository dutiesRepository;
    private final AbsencesRepository absencesRepository;

    @Override
    public DashboardStatsResponse getStatistics() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        // Calcul des dates pour la semaine en cours
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // Statistiques des marins
        stats.setTotalAgents(agentsRepository.count());
        stats.setAvailableAgents(agentsRepository.countByStatus(MarinerStatus.DISPONIBLE));
        stats.setAgentsOnMission(agentsRepository.countByStatus(MarinerStatus.EN_MER));
        stats.setAgentsOnDuty(agentsRepository.countByStatus(MarinerStatus.EN_GARDE));
        stats.setAgentsResting(agentsRepository.countByStatus(MarinerStatus.PERMISSION));
        stats.setAgentsAbsent(agentsRepository.countByStatus(MarinerStatus.ABSENT));

        // Statistiques des missions
        stats.setOngoingMissions(missionsRepository.countByStatus(MissionStatus.IN_PROGRESS));
        stats.setPlannedMissions(missionsRepository.countByStatus(MissionStatus.PLANNED));
        stats.setCompletedMissions(missionsRepository.countByStatus(MissionStatus.COMPLETED));
        stats.setCancelledMissions(missionsRepository.countByStatus(MissionStatus.CANCELLED));

        // Nouvelles missions cette semaine
        stats.setNewMissionsThisWeek(
                missionsRepository.countByCreateDateBetween(startOfWeek, endOfWeek)
        );

        // Statistiques des gardes
        stats.setDutiesThisWeek(
                dutiesRepository.countByStartDateBetween(startOfWeek, endOfWeek)
        );

        // Gardes à venir cette semaine (planifiées et démarrant dans le futur)
        stats.setUpcomingDutiesThisWeek(
                dutiesRepository.countByStatusAndStartDateBetween(
                        DutyStatus.PLANNED, now, endOfWeek
                )
        );

        // Statistiques des absences
        stats.setPendingAbsences(absencesRepository.countByStatus(AbsenceStatus.PENDING));
        stats.setApprovedAbsences(absencesRepository.countByStatus(AbsenceStatus.APPROVED));
        stats.setRejectedAbsences(absencesRepository.countByStatus(AbsenceStatus.REJECTED));

        return stats;
    }
}