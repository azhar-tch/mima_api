package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.*;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.MissionParticipations;
import com.helpysoft.mima_api.entity.Missions;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.entity.Units;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.MissionsMapper;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.MissionParticipationsRepository;
import com.helpysoft.mima_api.repository.MissionsRepository;
import com.helpysoft.mima_api.repository.UnitsRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.MissionsService;
import com.helpysoft.mima_api.service.NotificationsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MissionsServiceImpl implements MissionsService {

    private final MissionsRepository missionsRepository;
    private final UnitsRepository unitsRepository;
    private final MissionsMapper missionsMapper;
    private final NotificationsService notificationsService;
    private final MissionParticipationsRepository missionParticipationsRepository;
    private final AgentsRepository agentsRepository;
    private final UsersRepository usersRepository;
    private final HistoriesServiceImpl historiesService;
    @PersistenceContext
    private EntityManager entityManager;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public MissionsResponse create(MissionsRequest request) {
        // Récupérer plusieurs unités
        Set<Units> units = new HashSet<>();
        if (request.getUnitTrackingIds() != null && !request.getUnitTrackingIds().isEmpty()) {
            for (UUID unitId : request.getUnitTrackingIds()) {
                Units u = unitsRepository.findByTrackingId(unitId)
                        .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + unitId));
                units.add(u);
            }
        }

        // Récupérer plusieurs agents
        Set<Agents> agents = new HashSet<>();
        if (request.getAgentTrackingIds() != null && !request.getAgentTrackingIds().isEmpty()) {
            for (UUID agentId : request.getAgentTrackingIds()) {
                Agents agent = agentsRepository.findByTrackingId(agentId)
                        .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + agentId));
                agents.add(agent);
            }
        }

        Missions mission = missionsMapper.toEntity(request, units, agents);
        Missions savedMission = missionsRepository.save(mission);

        // Create participations for assigned agents (agents don't have system access, so no notifications sent to them)
        if (request.getParticipantTrackingIds() != null && !request.getParticipantTrackingIds().isEmpty()) {
            for (UUID agentTrackingId : request.getParticipantTrackingIds()) {
                try {
                    Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                            .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

                    MissionParticipations participation = new MissionParticipations();
                    participation.setTrackingId(UUID.randomUUID());
                    participation.setMission(savedMission);
                    participation.setAgent(agent);
                    participation.setCreateDate(LocalDateTime.now());
                    missionParticipationsRepository.save(participation);

                    log.info("✅ Participation créée pour l'agent {} dans la mission {}",
                            agent.getFirstName() + " " + agent.getLastName(),
                            savedMission.getTitle());
                } catch (Exception e) {
                    log.error("❌ Erreur lors de la création de la participation: {}", e.getMessage());
                }
            }
        }

        notifyAllUsersOfMissionCreation(savedMission);

        // ✅ ENREGISTRER DANS L'HISTORIQUE - Version corrigée
        UUID agentTrackingId = request.getParticipantTrackingIds() != null && !request.getParticipantTrackingIds().isEmpty()
                ? request.getParticipantTrackingIds().get(0)
                : null;

        if (agentTrackingId != null) {
            try {
                // Créer une version simplifiée pour l'historique (sans les collections lazy)
                String missionSummary = String.format(
                        "{\"trackingId\":\"%s\",\"title\":\"%s\",\"type\":\"%s\",\"location\":\"%s\",\"status\":\"%s\"}",
                        savedMission.getTrackingId(),
                        savedMission.getTitle(),
                        savedMission.getType(),
                        savedMission.getLocation(),
                        savedMission.getStatus()
                );

                HistoriesRequest historyRequest = new HistoriesRequest();
                historyRequest.setAgentTrackingId(agentTrackingId);
                historyRequest.setEntityName("MISSION");
                historyRequest.setEntityTrackingId(savedMission.getTrackingId());
                historyRequest.setActionType(ActionType.CREATE);
                historyRequest.setChangesSummary(
                        "Création de la mission: " + savedMission.getTitle() + " du " +
                                savedMission.getPlannedStartDate().format(DATE_FORMATTER) + " au " +
                                savedMission.getPlannedEndDate().format(DATE_FORMATTER)
                );
                historyRequest.setNewValue(missionSummary);

                historiesService.create(historyRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
            }
        }
        entityManager.flush();
        entityManager.clear();
        return missionsMapper.toResponse(savedMission);
    }

    @Override
    @Transactional
    public MissionsResponse update(UUID trackingId, MissionsRequest request) {

        Missions oldMission = missionsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Mission not found with trackingId: " + trackingId));

        log.info("🔍 Mission trouvée: {}", oldMission.getTitle());


        // ============================
        // 🔥 SAUVEGARDE ANCIENNES VALEURS
        // ============================

        String oldType = oldMission.getType();
        String oldTitle = oldMission.getTitle();
        String oldLocation = oldMission.getLocation();
        String oldShipName = oldMission.getShipName();
        String oldObjective = oldMission.getObjective();
        LocalDateTime oldPlannedStartDate = oldMission.getPlannedStartDate();
        LocalDateTime oldPlannedEndDate = oldMission.getPlannedEndDate();
        MissionStatus oldStatus = oldMission.getStatus();

        List<String> oldUnitNames = oldMission.getUnits()
                .stream().map(Units::getName).toList();

        List<String> oldAgentNames = oldMission.getAgents()
                .stream().map(a -> a.getFirstName() + " " + a.getLastName()).toList();


        // ============================
        // 🔥 RECONSTRUCTION ENTIÈRE DE LA MISSION
        // ============================

        Missions mission = new Missions();

        mission.setId(oldMission.getId()); // indispensable !
        mission.setTrackingId(oldMission.getTrackingId());
        mission.setCreateDate(oldMission.getCreateDate());
        mission.setCreatedBy(oldMission.getCreatedBy());

        // Nouvelles valeurs
        mission.setType(request.getType());
        mission.setTitle(request.getTitle());
        mission.setLocation(request.getLocation());
        mission.setShipName(request.getShipName());
        mission.setObjective(request.getObjective());
        mission.setPlannedStartDate(request.getPlannedStartDate());
        mission.setPlannedEndDate(request.getPlannedEndDate());
        mission.setActualStartDate(request.getActualStartDate());
        mission.setActualEndDate(request.getActualEndDate());
        mission.setStatus(request.getStatus());
        mission.setUpdateDate(LocalDateTime.now());
        mission.setUpdatedBy(null);


        // ============================
        // 🔄 RECONSTRUCTION DES UNITS
        // ============================

        Set<Units> newUnits = new HashSet<>();
        List<String> newUnitNames = new ArrayList<>();

        if (request.getUnitTrackingIds() != null) {
            for (UUID unitId : request.getUnitTrackingIds()) {
                Units u = unitsRepository.findByTrackingId(unitId)
                        .orElseThrow(() -> new RuntimeException("Unit not found: " + unitId));

                newUnits.add(u);
                newUnitNames.add(u.getName());
            }
        }
        mission.setUnits(newUnits);


        // ============================
        // 🔄 RECONSTRUCTION DES AGENTS
        // ============================

        Set<Agents> newAgents = new HashSet<>();
        List<String> newAgentNames = new ArrayList<>();

        if (request.getAgentTrackingIds() != null) {
            for (UUID agentId : request.getAgentTrackingIds()) {
                Agents a = agentsRepository.findByTrackingId(agentId)
                        .orElseThrow(() -> new RuntimeException("Agent not found: " + agentId));

                newAgents.add(a);
                newAgentNames.add(a.getFirstName() + " " + a.getLastName());
            }
        }
        mission.setAgents(newAgents);


        // ============================
        // 🔥 SAUVEGARDE SANS MERGE
        // ============================

        Missions updatedMission = missionsRepository.save(mission);


        // ============================
        // 🔥 DÉTECTION DES CHANGEMENTS
        // ============================

        StringBuilder changes = new StringBuilder();
        boolean hasChanges = false;

        if (!Objects.equals(oldType, request.getType())) {
            changes.append("Type: '").append(oldType).append("' → '").append(request.getType()).append("' | ");
            hasChanges = true;
        }

        if (!Objects.equals(oldTitle, request.getTitle())) {
            changes.append("Titre: '").append(oldTitle).append("' → '").append(request.getTitle()).append("' | ");
            hasChanges = true;
        }

        if (!Objects.equals(oldLocation, request.getLocation())) {
            changes.append("Localisation: '").append(oldLocation).append("' → '").append(request.getLocation()).append("' | ");
            hasChanges = true;
        }

        if (!Objects.equals(oldShipName, request.getShipName())) {
            changes.append("Navire: '").append(oldShipName).append("' → '").append(request.getShipName()).append("' | ");
            hasChanges = true;
        }

        if (!Objects.equals(oldObjective, request.getObjective())) {
            changes.append("Objectif modifié | ");
            hasChanges = true;
        }

        if (!oldPlannedStartDate.isEqual(request.getPlannedStartDate())) {
            changes.append("Date début modifiée | ");
            hasChanges = true;
        }

        if (!oldPlannedEndDate.isEqual(request.getPlannedEndDate())) {
            changes.append("Date fin modifiée | ");
            hasChanges = true;
        }

        if (oldStatus != request.getStatus()) {
            changes.append("Statut: '").append(oldStatus).append("' → '").append(request.getStatus()).append("' | ");
            hasChanges = true;
        }

        if (!oldUnitNames.equals(newUnitNames)) {
            changes.append("Unités modifiées | ");
            hasChanges = true;
        }

        if (!oldAgentNames.equals(newAgentNames)) {
            changes.append("Agents modifiés | ");
            hasChanges = true;
        }


        // ============================
        // 🔔 NOTIFICATION + HISTORIQUE
        // ============================

        if (hasChanges) {

            String msg = changes.substring(0, changes.length() - 3);

            notifyMissionModification(updatedMission, msg);

            HistoriesRequest h = new HistoriesRequest();
            h.setAgentTrackingId(newAgents.isEmpty() ? null : newAgents.iterator().next().getTrackingId());
            h.setEntityName("MISSION");
            h.setEntityTrackingId(updatedMission.getTrackingId());
            h.setActionType(ActionType.UPDATE);
            h.setChangesSummary(msg);

            historiesService.create(h);
        }

        return missionsMapper.toResponse(updatedMission);
    }


    /**
     * Notifies all users when a mission is modified
     * Sends a consolidated notification with all field changes
     */
    private void notifyMissionModification(Missions mission, String changesMessage) {
        // Notify all users with broadcast message
        List<Users> allUsers = usersRepository.findAll();
        String broadcastMessage = "La mission '" + mission.getTitle() +
            "' a été modifiée. Changements: " + changesMessage;

        for (Users user : allUsers) {
            try {
                NotificationsRequest userNotification = new NotificationsRequest();
                userNotification.setMessage(broadcastMessage);
                userNotification.setNotificationType("missions");
                userNotification.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(userNotification);
            } catch (Exception e) {
                log.error("❌ Erreur notification utilisateur {}: {}",
                    user.getFirstName() + " " + user.getLastName(), e.getMessage());
            }
        }

        log.info("✅ Notifications de modification envoyées à {} utilisateurs", allUsers.size());
    }

    @Override
    @Transactional(readOnly = true)
    public MissionsResponse findByTrackingId(UUID trackingId) {
        Missions mission = missionsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Mission not found with trackingId: " + trackingId));
        return missionsMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionsResponse> findByStatus(MissionStatus status) {
        return missionsRepository.findByStatus(status)
                .stream()
                .map(missionsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionsResponse> findByUnit(UUID unitTrackingId) {
        Units unit = unitsRepository.findByTrackingId(unitTrackingId)
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + unitTrackingId));
        return missionsRepository.findByUnit(unit)
                .stream()
                .map(missionsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionsResponse> findByPlannedStartDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return missionsRepository.findByPlannedStartDateBetween(startDate, endDate)
                .stream()
                .map(missionsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionsResponse> findAll() {
        return missionsRepository.findAll()
                .stream()
                .map(missionsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Missions mission = missionsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Mission not found with trackingId: " + trackingId));

        // Sauvegarder les infos avant suppression
        String missionTitle = mission.getTitle();
        LocalDateTime plannedStart = mission.getPlannedStartDate();
        LocalDateTime plannedEnd = mission.getPlannedEndDate();
        UUID missionTrackingId = mission.getTrackingId();

        // Notify all users before deletion
        notifyAllUsersOfMissionDeletion(mission);

        // Trouver un agent pour l'historique
        List<MissionParticipations> participations = missionParticipationsRepository.findByMission(mission);
        UUID agentTrackingId = !participations.isEmpty()
                ? participations.get(0).getAgent().getTrackingId()
                : null;

        // Supprimer la mission
        missionsRepository.delete(mission);
        entityManager.flush();

        // ✅ Enregistrer dans l'historique APRÈS la suppression, sans l'objet
        if (agentTrackingId != null) {
            try {
                HistoriesRequest historyRequest = new HistoriesRequest();
                historyRequest.setAgentTrackingId(agentTrackingId);
                historyRequest.setEntityName("MISSION");
                historyRequest.setEntityTrackingId(missionTrackingId);
                historyRequest.setActionType(ActionType.DELETE);
                historyRequest.setChangesSummary(
                        "Suppression de la mission: " + missionTitle + " du " +
                                plannedStart.format(DATE_FORMATTER) + " au " +
                                plannedEnd.format(DATE_FORMATTER)
                );

                historiesService.create(historyRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
            }
        }
    }

    /**
     * Notifies all users when a new mission is created
     */
    private void notifyAllUsersOfMissionCreation(Missions mission) {
        List<Users> allUsers = usersRepository.findAll();

        String message = "Nouvelle mission créée : " + mission.getTitle() +
                " du " + mission.getPlannedStartDate().format(DATE_FORMATTER) +
                " au " + mission.getPlannedEndDate().format(DATE_FORMATTER);

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("missions");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de création de mission: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de création envoyée à {} utilisateurs pour la mission {}",
                 allUsers.size(), mission.getTitle());
    }

    /**
     * Notifies all users when a mission status changes
     */
    private void notifyParticipantsOfStatusChange(Missions mission, MissionStatus oldStatus, MissionStatus newStatus) {
        List<Users> allUsers = usersRepository.findAll();

        String statusMessage;
        switch (newStatus) {
            case IN_PROGRESS:
                statusMessage = "La mission " + mission.getTitle() + " a démarré";
                break;
            case COMPLETED:
                statusMessage = "La mission " + mission.getTitle() + " est terminée";
                break;
            case CANCELLED:
                statusMessage = "La mission " + mission.getTitle() + " a été annulée";
                break;
            default:
                statusMessage = "La mission " + mission.getTitle() + " a été mise à jour";
        }

        // Notify all users about the status change
        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(statusMessage);
                notificationRequest.setNotificationType("missions");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de changement de statut envoyée à {} utilisateurs pour la mission {}",
                 allUsers.size(), mission.getTitle());
    }

    /**
     * Notifies all users when a mission is modified
     */
    private void notifyAllUsersOfMissionModification(Missions mission) {
        List<Users> allUsers = usersRepository.findAll();

        String message = "La mission " + mission.getTitle() +
                " a été modifiée. Nouvelles dates : du " +
                mission.getPlannedStartDate().format(DATE_FORMATTER) +
                " au " + mission.getPlannedEndDate().format(DATE_FORMATTER);

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("missions");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de modification: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de modification envoyée à {} utilisateurs pour la mission {}",
                allUsers.size(), mission.getTitle());
    }

    /**
     * Notifies all users when a mission is deleted
     */
    private void notifyAllUsersOfMissionDeletion(Missions mission) {
        List<Users> allUsers = usersRepository.findAll();

        String message = "La mission " + mission.getTitle() +
                " du " + mission.getPlannedStartDate().format(DATE_FORMATTER) +
                " au " + mission.getPlannedEndDate().format(DATE_FORMATTER) +
                " a été supprimée";

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("missions");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de suppression: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de suppression envoyée à {} utilisateurs pour la mission {}",
                allUsers.size(), mission.getTitle());
    }

    /**
     * Tâche planifiée qui s'exécute toutes les heures pour mettre à jour automatiquement
     * les statuts des missions en fonction de leurs dates de début et de fin
     */
    @Scheduled(cron = "0 0 * * * *") // Exécute toutes les heures à la minute 0
    @Transactional
    public void updateMissionStatuses() {
        LocalDateTime now = LocalDateTime.now();
        int updatedCount = 0;

        log.info("Démarrage de la mise à jour automatique des statuts de missions...");

        // 1. Passer les missions PLANNED à IN_PROGRESS si la date de début est dépassée
        List<Missions> missionsToStart = missionsRepository
                .findByStatusAndPlannedStartDateBefore(MissionStatus.PLANNED, now);

        for (Missions mission : missionsToStart) {
            MissionStatus oldStatus = mission.getStatus();
            mission.setStatus(MissionStatus.IN_PROGRESS);
            mission.setActualStartDate(mission.getPlannedStartDate());
            missionsRepository.save(mission);
            updatedCount++;
            log.info("Mission {} passée de PLANNED à IN_PROGRESS", mission.getTrackingId());

            // Notify participants
            notifyParticipantsOfStatusChange(mission, oldStatus, MissionStatus.IN_PROGRESS);
        }

        // 2. Passer les missions IN_PROGRESS à COMPLETED si la date de fin est dépassée
        List<Missions> missionsToComplete = missionsRepository
                .findByStatusAndPlannedEndDateBefore(MissionStatus.IN_PROGRESS, now);

        for (Missions mission : missionsToComplete) {
            MissionStatus oldStatus = mission.getStatus();
            mission.setStatus(MissionStatus.COMPLETED);
            mission.setActualEndDate(mission.getPlannedEndDate());
            missionsRepository.save(mission);
            updatedCount++;
            log.info("Mission {} passée de IN_PROGRESS à COMPLETED", mission.getTrackingId());

            // Notify participants
            notifyParticipantsOfStatusChange(mission, oldStatus, MissionStatus.COMPLETED);
        }

        log.info("Mise à jour automatique des statuts terminée. {} mission(s) mise(s) à jour.", updatedCount);
    }
}
