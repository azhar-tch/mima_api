package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.DutiesRequest;
import com.helpysoft.mima_api.dto.DutiesResponse;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Duties;
import com.helpysoft.mima_api.entity.DutyStatus;
import com.helpysoft.mima_api.entity.Units;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.DutiesMapper;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.DutiesRepository;
import com.helpysoft.mima_api.repository.UnitsRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.DutiesService;
import com.helpysoft.mima_api.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DutiesServiceImpl implements DutiesService {

    private final DutiesRepository dutiesRepository;
    private final AgentsRepository agentsRepository;
    private final UnitsRepository unitsRepository;
    private final DutiesMapper dutiesMapper;
    private final NotificationsService notificationsService;
    private final UsersRepository usersRepository;
    private final HistoriesServiceImpl historiesService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public DutiesResponse create(DutiesRequest request) {
        Agents agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));

        Units unit = unitsRepository.findByTrackingId(request.getUnitTrackingId())
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + request.getUnitTrackingId()));

        Duties duty = dutiesMapper.toEntity(request, agent, unit);
        Duties savedDuty = dutiesRepository.save(duty);

        // Enregistrer dans l'historique
        historiesService.recordHistory(
                request.getAgentTrackingId(),
                "DUTY",
                savedDuty.getTrackingId(),
                ActionType.CREATE,
                "Création de la garde: " + savedDuty.getPosition() + " du " +
                        savedDuty.getStartDate().format(DATE_FORMATTER) + " au " +
                        savedDuty.getEndDate().format(DATE_FORMATTER),
                null,
                savedDuty
        );

        /* Notify the assigned agent
        try {
            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(
                    "Vous avez été assigné à une garde le " +
                            savedDuty.getStartDate().format(DATE_FORMATTER) +
                            " de " + savedDuty.getStartDate().toLocalTime().format(TIME_FORMATTER) +
                            " à " + savedDuty.getEndDate().toLocalTime().format(TIME_FORMATTER)
            );
            notificationRequest.setNotificationType("duties");
            notificationRequest.setRecipientTrackingId(request.getAgentTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification envoyée à l'agent {} pour l'assignation de garde",
                    agent.getFirstName() + " " + agent.getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
        }*/

        // Notify all users about the new duty assignment
        notifyAllUsersOfDutyCreation(savedDuty, agent);

        return dutiesMapper.toResponse(savedDuty);
    }

    @Override
    public DutiesResponse update(UUID trackingId, DutiesRequest request) {
        Duties duty = dutiesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Duty not found with trackingId: " + trackingId));

        Agents agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));

        Units unit = unitsRepository.findByTrackingId(request.getUnitTrackingId())
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + request.getUnitTrackingId()));

        // Cloner l'entité avant modification pour l'historique
        Duties oldDuty = new Duties();
        oldDuty.setTrackingId(duty.getTrackingId());
        oldDuty.setPosition(duty.getPosition());
        oldDuty.setDutyType(duty.getDutyType());
        oldDuty.setStartDate(duty.getStartDate());
        oldDuty.setEndDate(duty.getEndDate());
        oldDuty.setStatus(duty.getStatus());
        oldDuty.setAgent(duty.getAgent());
        oldDuty.setUnit(duty.getUnit());

        // Sauvegarder les anciennes valeurs pour détecter les modifications
        String oldPosition = duty.getPosition();
        String oldDutyType = duty.getDutyType() != null ? duty.getDutyType().toString() : null;
        LocalDateTime oldStartDate = duty.getStartDate();
        LocalDateTime oldEndDate = duty.getEndDate();
        DutyStatus oldStatus = duty.getStatus();
        String oldAgentName = duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName();
        String oldUnitName = duty.getUnit().getName();

        // Appliquer les modifications
        duty.setPosition(request.getPosition());
        duty.setDutyType(request.getDutyType());
        duty.setStartDate(request.getStartDate());
        duty.setEndDate(request.getEndDate());
        duty.setStatus(request.getStatus());
        duty.setAgent(agent);
        duty.setUnit(unit);

        Duties updatedDuty = dutiesRepository.save(duty);

        // Détecter tous les changements et construire un message détaillé
        StringBuilder changes = new StringBuilder();
        boolean hasChanges = false;

        // Vérifier changement de position
        if (oldPosition != null && request.getPosition() != null &&
            !oldPosition.equals(request.getPosition())) {
            changes.append("Position: '").append(oldPosition).append("' → '")
                   .append(request.getPosition()).append("' | ");
            hasChanges = true;
        }

        // Vérifier changement de type
        if (oldDutyType != null && request.getDutyType() != null &&
            !oldDutyType.equals(request.getDutyType().toString())) {
            changes.append("Type: '").append(oldDutyType).append("' → '")
                   .append(request.getDutyType()).append("' | ");
            hasChanges = true;
        }

        // Vérifier changement de date de début
        if (oldStartDate != null && request.getStartDate() != null &&
            !oldStartDate.isEqual(request.getStartDate())) {
            changes.append("Date début: ").append(oldStartDate.format(DATE_FORMATTER))
                   .append(" ").append(oldStartDate.toLocalTime().format(TIME_FORMATTER))
                   .append(" → ").append(request.getStartDate().format(DATE_FORMATTER))
                   .append(" ").append(request.getStartDate().toLocalTime().format(TIME_FORMATTER))
                   .append(" | ");
            hasChanges = true;
        }

        // Vérifier changement de date de fin
        if (oldEndDate != null && request.getEndDate() != null &&
            !oldEndDate.isEqual(request.getEndDate())) {
            changes.append("Date fin: ").append(oldEndDate.format(DATE_FORMATTER))
                   .append(" ").append(oldEndDate.toLocalTime().format(TIME_FORMATTER))
                   .append(" → ").append(request.getEndDate().format(DATE_FORMATTER))
                   .append(" ").append(request.getEndDate().toLocalTime().format(TIME_FORMATTER))
                   .append(" | ");
            hasChanges = true;
        }

        // Vérifier changement de statut
        if (oldStatus != request.getStatus()) {
            changes.append("Statut: '").append(oldStatus).append("' → '")
                   .append(request.getStatus()).append("' | ");
            hasChanges = true;
        }

        // Vérifier changement d'agent
        String newAgentName = agent.getFirstName() + " " + agent.getLastName();
        if (!oldAgentName.equals(newAgentName)) {
            changes.append("Agent: '").append(oldAgentName).append("' → '")
                   .append(newAgentName).append("' | ");
            hasChanges = true;
        }

        // Vérifier changement d'unité
        String newUnitName = unit.getName();
        if (!oldUnitName.equals(newUnitName)) {
            changes.append("Unité: '").append(oldUnitName).append("' → '")
                   .append(newUnitName).append("' | ");
            hasChanges = true;
        }

        // Si des modifications ont été détectées, envoyer une notification
        if (hasChanges) {
            // Supprimer le dernier " | "
            String changesMessage = changes.substring(0, changes.length() - 3);
            notifyDutyModification(updatedDuty, changesMessage);
            log.info("Garde modifiée - Changements: {}", changesMessage);

            // Enregistrer dans l'historique (sans oldValue/newValue pour les UPDATE)
            historiesService.recordHistory(
                    request.getAgentTrackingId(),
                    "DUTY",
                    updatedDuty.getTrackingId(),
                    ActionType.UPDATE,
                    changesMessage,
                    null,
                    null
            );
        }

        return dutiesMapper.toResponse(updatedDuty);
    }

    /**
     * Notifies all users when a duty is modified
     * Sends a consolidated notification with all field changes
     */
    private void notifyDutyModification(Duties duty, String changesMessage) {
        // Notify all users with broadcast message
        List<Users> allUsers = usersRepository.findAll();
        String broadcastMessage = "La garde de " +
            duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() +
            " a été modifiée. Changements: " + changesMessage;

        for (Users user : allUsers) {
            try {
                NotificationsRequest userNotification = new NotificationsRequest();
                userNotification.setMessage(broadcastMessage);
                userNotification.setNotificationType("duties");
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
    public DutiesResponse findByTrackingId(UUID trackingId) {
        Duties duty = dutiesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Duty not found with trackingId: " + trackingId));
        return dutiesMapper.toResponse(duty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutiesResponse> findByAgent(UUID agentTrackingId) {
        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + agentTrackingId));
        return dutiesRepository.findByAgent(agent)
                .stream()
                .map(dutiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutiesResponse> findByUnit(UUID unitTrackingId) {
        Units unit = unitsRepository.findByTrackingId(unitTrackingId)
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + unitTrackingId));
        return dutiesRepository.findByUnit(unit)
                .stream()
                .map(dutiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutiesResponse> findByStatus(DutyStatus status) {
        return dutiesRepository.findByStatus(status)
                .stream()
                .map(dutiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutiesResponse> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return dutiesRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(dutiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DutiesResponse> findAll() {
        return dutiesRepository.findAll()
                .stream()
                .map(dutiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Duties duty = dutiesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Duty not found with trackingId: " + trackingId));

        // Notify agent before deletion
        try {
            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(
                    "Votre garde du " + duty.getStartDate().format(DATE_FORMATTER) +
                            " a été supprimée"
            );
            notificationRequest.setNotificationType("duties");
            notificationRequest.setRecipientTrackingId(duty.getAgent().getTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification de suppression envoyée à l'agent {} pour la garde",
                    duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
        }

        // Notify all users about the duty deletion
        notifyAllUsersOfDutyDeletion(duty);

        // Enregistrer dans l'historique avant suppression
        historiesService.recordHistory(
                duty.getAgent().getTrackingId(),
                "DUTY",
                duty.getTrackingId(),
                ActionType.DELETE,
                "Suppression de la garde: " + duty.getPosition() + " du " +
                        duty.getStartDate().format(DATE_FORMATTER) + " au " +
                        duty.getEndDate().format(DATE_FORMATTER),
                duty,
                null
        );

        dutiesRepository.delete(duty);
    }

    /**
     * Notifies all users when a new duty is created
     */
    private void notifyAllUsersOfDutyCreation(Duties duty, Agents agent) {
        List<Users> allUsers = usersRepository.findAll();

        String message = agent.getFirstName() + " " + agent.getLastName() +
                " a été assigné à une garde le " + duty.getStartDate().format(DATE_FORMATTER) +
                " de " + duty.getStartDate().toLocalTime().format(TIME_FORMATTER) +
                " à " + duty.getEndDate().toLocalTime().format(TIME_FORMATTER);

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("duties");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de création de garde: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de création de garde envoyée à {} utilisateurs", allUsers.size());
    }

    /**
     * Notifies the agent when their duty status changes
     */
    private void notifyAgentOfStatusChange(Duties duty, DutyStatus oldStatus, DutyStatus newStatus) {
        // Notify the assigned agent with personalized message
        try {
            String statusMessage;
            switch (newStatus) {
                case COMPLETED:
                    statusMessage = "Votre garde du " + duty.getStartDate().format(DATE_FORMATTER) +
                            " a été marquée comme terminée";
                    break;
                case CANCELLED:
                    statusMessage = "Votre garde du " + duty.getStartDate().format(DATE_FORMATTER) +
                            " a été annulée";
                    break;
                case ACTIVE:
                    statusMessage = "Votre garde du " + duty.getStartDate().format(DATE_FORMATTER) +
                            " est maintenant active";
                    break;
                case REPLACED:
                    statusMessage = "Votre garde du " + duty.getStartDate().format(DATE_FORMATTER) +
                            " a été remplacée";
                    break;
                case PLANNED:
                    statusMessage = "Votre garde du " + duty.getStartDate().format(DATE_FORMATTER) +
                            " a été planifiée";
                    break;
                default:
                    statusMessage = "Le statut de votre garde a été mis à jour";
            }

            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(statusMessage);
            notificationRequest.setNotificationType("duties");
            notificationRequest.setRecipientTrackingId(duty.getAgent().getTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification de changement de statut envoyée à l'agent {} pour la garde",
                    duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
        }

        // Notify all users about the status change
        notifyAllUsersOfDutyStatusChange(duty, newStatus);
    }

    /**
     * Notifies the agent when their duty schedule is modified
     */
    private void notifyAgentOfDutyModification(Duties duty) {
        // Notify the assigned agent with personalized message
        try {
            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(
                    "Votre garde a été modifiée. Nouvelle date: " +
                            duty.getStartDate().format(DATE_FORMATTER) +
                            " de " + duty.getStartDate().toLocalTime().format(TIME_FORMATTER) +
                            " à " + duty.getEndDate().toLocalTime().format(TIME_FORMATTER)
            );
            notificationRequest.setNotificationType("duties");
            notificationRequest.setRecipientTrackingId(duty.getAgent().getTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification de modification envoyée à l'agent {} pour la garde",
                    duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
        }

        // Notify all users about the duty modification
        notifyAllUsersOfDutyModification(duty);
    }

    /**
     * Notifies all users when a duty status changes
     */
    private void notifyAllUsersOfDutyStatusChange(Duties duty, DutyStatus newStatus) {
        List<Users> allUsers = usersRepository.findAll();

        String statusMessage;
        switch (newStatus) {
            case COMPLETED:
                statusMessage = "La garde de " + duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() +
                        " du " + duty.getStartDate().format(DATE_FORMATTER) +
                        " a été marquée comme terminée";
                break;
            case CANCELLED:
                statusMessage = "La garde de " + duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() +
                        " du " + duty.getStartDate().format(DATE_FORMATTER) +
                        " a été annulée";
                break;
            case ACTIVE:
                statusMessage = "La garde de " + duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() +
                        " du " + duty.getStartDate().format(DATE_FORMATTER) +
                        " est maintenant active";
                break;
            case REPLACED:
                statusMessage = "La garde de " + duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() +
                        " du " + duty.getStartDate().format(DATE_FORMATTER) +
                        " a été remplacée";
                break;
            case PLANNED:
                statusMessage = "La garde de " + duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() +
                        " du " + duty.getStartDate().format(DATE_FORMATTER) +
                        " a été planifiée";
                break;
            default:
                statusMessage = "Le statut d'une garde a été mis à jour";
        }

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(statusMessage);
                notificationRequest.setNotificationType("duties");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de statut: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de changement de statut envoyée à {} utilisateurs", allUsers.size());
    }

    /**
     * Notifies all users when a duty is modified
     */
    private void notifyAllUsersOfDutyModification(Duties duty) {
        List<Users> allUsers = usersRepository.findAll();

        String message = "La garde de " + duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() +
                " a été modifiée. Nouvelle date: " +
                duty.getStartDate().format(DATE_FORMATTER) +
                " de " + duty.getStartDate().toLocalTime().format(TIME_FORMATTER) +
                " à " + duty.getEndDate().toLocalTime().format(TIME_FORMATTER);

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("duties");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de modification: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de modification envoyée à {} utilisateurs", allUsers.size());
    }

    /**
     * Notifies all users when a duty is deleted
     */
    private void notifyAllUsersOfDutyDeletion(Duties duty) {
        List<Users> allUsers = usersRepository.findAll();

        String message = "La garde de " + duty.getAgent().getFirstName() + " " + duty.getAgent().getLastName() +
                " du " + duty.getStartDate().format(DATE_FORMATTER) +
                " a été supprimée";

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("duties");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de suppression: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de suppression envoyée à {} utilisateurs", allUsers.size());
    }

    /**
     * Tâche planifiée qui s'exécute toutes les heures pour mettre à jour automatiquement
     * les statuts des gardes en fonction de leurs dates de début et de fin
     */
    @Scheduled(cron = "0 0 * * * *") // Exécute toutes les heures à la minute 0
    @Transactional
    public void updateDutyStatuses() {
        LocalDateTime now = LocalDateTime.now();
        int updatedCount = 0;

        log.info("Démarrage de la mise à jour automatique des statuts de gardes...");

        // 1. Passer les gardes PLANNED à ACTIVE si la date de début est dépassée
        List<Duties> dutiesToStart = dutiesRepository
                .findByStatusAndStartDateBefore(DutyStatus.PLANNED, now);

        for (Duties duty : dutiesToStart) {
            DutyStatus oldStatus = duty.getStatus();
            duty.setStatus(DutyStatus.ACTIVE);
            dutiesRepository.save(duty);
            updatedCount++;
            log.info("Garde {} passée de PLANNED à ACTIVE", duty.getTrackingId());

            // Notify agent and all users
            notifyAgentOfStatusChange(duty, oldStatus, DutyStatus.ACTIVE);
        }

        // 2. Passer les gardes ACTIVE à COMPLETED si la date de fin est dépassée
        List<Duties> dutiesToComplete = dutiesRepository
                .findByStatusAndEndDateBefore(DutyStatus.ACTIVE, now);

        for (Duties duty : dutiesToComplete) {
            DutyStatus oldStatus = duty.getStatus();
            duty.setStatus(DutyStatus.COMPLETED);
            dutiesRepository.save(duty);
            updatedCount++;
            log.info("Garde {} passée de ACTIVE à COMPLETED", duty.getTrackingId());

            // Notify agent and all users
            notifyAgentOfStatusChange(duty, oldStatus, DutyStatus.COMPLETED);
        }

        log.info("Mise à jour automatique des statuts de gardes terminée. {} garde(s) mise(s) à jour.", updatedCount);
    }
}
