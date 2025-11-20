package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ArmedGuardMissionRequest;
import com.helpysoft.mima_api.dto.ArmedGuardMissionResponse;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.entity.ArmedGuardMissions;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.entity.SecurityAgencies;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.ArmedGuardMissionMapper;
import com.helpysoft.mima_api.repository.ArmedGuardMissionRepository;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.SecurityAgencyRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.ArmedGuardMissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArmedGuardMissionServiceImpl implements ArmedGuardMissionService {

    private final ArmedGuardMissionRepository armedGuardMissionRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final SecurityAgencyRepository securityAgencyRepository;
    private final ArmedGuardMissionMapper armedGuardMissionMapper;
    private final NotificationsServiceImpl notificationsService;
    private final UsersRepository usersRepository;

    @Override
    public ArmedGuardMissionResponse create(ArmedGuardMissionRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(request.getCommercialShipTrackingId())
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(request.getSecurityAgencyTrackingId())
                .orElseThrow(() -> new RuntimeException("Security agency not found"));

        ArmedGuardMissions mission = armedGuardMissionMapper.toEntity(request, ship, agency);
        mission.calculateDaysCount();
        ArmedGuardMissions savedMission = armedGuardMissionRepository.save(mission);
        return armedGuardMissionMapper.toResponse(savedMission);
    }

    @Override
    public ArmedGuardMissionResponse update(UUID trackingId, ArmedGuardMissionRequest request) {
        ArmedGuardMissions mission = armedGuardMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Armed guard mission not found"));

        CommercialShips ship = commercialShipRepository.findByTrackingId(request.getCommercialShipTrackingId())
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(request.getSecurityAgencyTrackingId())
                .orElseThrow(() -> new RuntimeException("Security agency not found"));

        mission.setCommercialShip(ship);
        mission.setSecurityAgency(agency);
        mission.setEmbarkationDate(request.getEmbarkationDate());
        mission.setDisembarkationDate(request.getDisembarkationDate());
        mission.setEmbarkationPort(request.getEmbarkationPort());
        mission.setDisembarkationPort(request.getDisembarkationPort());
        mission.setPersonnelCount(request.getPersonnelCount());
        mission.setPatrolZone(request.getPatrolZone());
        mission.setStatus(request.getStatus());
        mission.setIncidents(request.getIncidents());
        mission.setObservations(request.getObservations());
        mission.calculateDaysCount();

        ArmedGuardMissions updatedMission = armedGuardMissionRepository.save(mission);
        return armedGuardMissionMapper.toResponse(updatedMission);
    }

    @Override
    @Transactional(readOnly = true)
    public ArmedGuardMissionResponse findByTrackingId(UUID trackingId) {
        ArmedGuardMissions mission = armedGuardMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Armed guard mission not found"));
        return armedGuardMissionMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public ArmedGuardMissionResponse findByMissionNumber(String missionNumber) {
        ArmedGuardMissions mission = armedGuardMissionRepository.findByMissionNumber(missionNumber)
                .orElseThrow(() -> new RuntimeException("Armed guard mission not found"));
        return armedGuardMissionMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findByStatus(MissionStatus status) {
        return armedGuardMissionRepository.findByStatus(status)
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return armedGuardMissionRepository.findByPeriod(startDate, endDate)
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findByCommercialShip(UUID shipTrackingId) {
        return armedGuardMissionRepository.findByCommercialShipTrackingId(shipTrackingId)
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findBySecurityAgency(UUID agencyTrackingId) {
        return armedGuardMissionRepository.findBySecurityAgencyTrackingId(agencyTrackingId)
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findAll() {
        return armedGuardMissionRepository.findAll()
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ArmedGuardMissions mission = armedGuardMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Armed guard mission not found"));
        armedGuardMissionRepository.delete(mission);
    }

    /**
     * Notifies all users when an armed guard mission status changes
     */
    private void notifyParticipantsOfStatusChange(ArmedGuardMissions mission, MissionStatus oldStatus, MissionStatus newStatus) {
        List<Users> allUsers = usersRepository.findAll();

        String statusMessage;
        switch (newStatus) {
            case IN_PROGRESS:
                statusMessage = "La mission de garde armée " + mission.getMissionNumber() + " a démarré";
                break;
            case COMPLETED:
                statusMessage = "La mission de garde armée " + mission.getMissionNumber() + " est terminée";
                break;
            case CANCELLED:
                statusMessage = "La mission de garde armée " + mission.getMissionNumber() + " a été annulée";
                break;
            default:
                statusMessage = "La mission de garde armée " + mission.getMissionNumber() + " a été mise à jour";
        }

        // Notify all users about the status change
        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(statusMessage);
                notificationRequest.setNotificationType("armed_guard_missions");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de changement de statut: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de changement de statut envoyée à {} utilisateurs pour la mission de garde armée {}",
                allUsers.size(), mission.getMissionNumber());
    }

    /**
     * Tâche planifiée qui s'exécute toutes les heures pour mettre à jour automatiquement
     * les statuts des missions de garde armée en fonction de leurs dates d'embarquement et de débarquement
     */
    @Scheduled(cron = "0 0 * * * *") // Exécute toutes les heures à la minute 0
    @Transactional
    public void updateArmedGuardMissionStatuses() {
        LocalDateTime now = LocalDateTime.now();
        int updatedCount = 0;

        log.info("🔄 Démarrage de la mise à jour automatique des statuts des missions de garde armée...");

        // 1. Passer les missions PLANNED à IN_PROGRESS si la date d'embarquement est dépassée
        List<ArmedGuardMissions> missionsToStart = armedGuardMissionRepository
                .findByStatusAndEmbarkationDateBefore(MissionStatus.PLANNED, now);

        for (ArmedGuardMissions mission : missionsToStart) {
            MissionStatus oldStatus = mission.getStatus();
            mission.setStatus(MissionStatus.IN_PROGRESS);
            armedGuardMissionRepository.save(mission);
            updatedCount++;
            log.info("✅ Mission de garde armée {} passée de PLANNED à IN_PROGRESS", mission.getMissionNumber());

            // Notify participants
            notifyParticipantsOfStatusChange(mission, oldStatus, MissionStatus.IN_PROGRESS);
        }

        // 2. Passer les missions IN_PROGRESS à COMPLETED si la date de débarquement est dépassée
        List<ArmedGuardMissions> missionsToComplete = armedGuardMissionRepository
                .findByStatusAndDisembarkationDateBefore(MissionStatus.IN_PROGRESS, now);

        for (ArmedGuardMissions mission : missionsToComplete) {
            MissionStatus oldStatus = mission.getStatus();
            mission.setStatus(MissionStatus.COMPLETED);
            armedGuardMissionRepository.save(mission);
            updatedCount++;
            log.info("✅ Mission de garde armée {} passée de IN_PROGRESS à COMPLETED", mission.getMissionNumber());

            // Notify participants
            notifyParticipantsOfStatusChange(mission, oldStatus, MissionStatus.COMPLETED);
        }

        log.info("✅ Mise à jour automatique des statuts des missions de garde armée terminée. {} mission(s) mise(s) à jour.", updatedCount);
    }
}
