package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AbsencesRequest;
import com.helpysoft.mima_api.dto.AbsencesResponse;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.entity.AbsenceStatus;
import com.helpysoft.mima_api.entity.AbsenceType;
import com.helpysoft.mima_api.entity.Absences;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.AbsencesMapper;
import com.helpysoft.mima_api.repository.AbsencesRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.AbsencesService;
import com.helpysoft.mima_api.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class AbsencesServiceImpl implements AbsencesService {

    private final AbsencesRepository absencesRepository;
    private final AgentsRepository agentsRepository;
    private final UsersRepository usersRepository;
    private final AbsencesMapper absencesMapper;
    private final NotificationsService notificationsService;
    private final HistoriesServiceImpl historiesService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public AbsencesResponse create(AbsencesRequest request) {
        Agents agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

        Absences absence = absencesMapper.toEntity(request, agent, null);
        absence.setStatus(AbsenceStatus.PENDING); // Default status
        Absences savedAbsence = absencesRepository.save(absence);

        /* Notify the agent that their request was submitted
        try {
            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(
                    "Votre demande d'absence du " + savedAbsence.getStartDate().format(DATE_FORMATTER) +
                            " au " + savedAbsence.getEndDate().format(DATE_FORMATTER) +
                            " a été soumise et est en attente d'approbation"
            );
            notificationRequest.setNotificationType("absences");
            notificationRequest.setRecipientTrackingId(request.getAgentTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification envoyée à l'agent {} pour la demande d'absence",
                    agent.getFirstName() + " " + agent.getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
        }*/

        // Notify all users about the new absence request
        notifyAllUsersOfAbsenceRequest(savedAbsence, agent);

        // Enregistrer dans l'historique
        historiesService.recordHistory(
                request.getAgentTrackingId(),
                "ABSENCE",
                savedAbsence.getTrackingId(),
                ActionType.CREATE,
                "Création d'une demande d'absence: " + savedAbsence.getAbsenceType() + " du " +
                        savedAbsence.getStartDate().format(DATE_FORMATTER) + " au " +
                        savedAbsence.getEndDate().format(DATE_FORMATTER),
                null,
                savedAbsence
        );

        return absencesMapper.toResponse(savedAbsence);
    }

    @Override
    public AbsencesResponse update(UUID trackingId, AbsencesRequest request) {
        Absences absence = absencesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Absence non trouvée"));

        // Update absence fields
        Agents agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));

        // Cloner l'entité avant modification pour l'historique
        Absences oldAbsence = new Absences();
        oldAbsence.setTrackingId(absence.getTrackingId());
        oldAbsence.setAgent(absence.getAgent());
        oldAbsence.setAbsenceType(absence.getAbsenceType());
        oldAbsence.setStartDate(absence.getStartDate());
        oldAbsence.setEndDate(absence.getEndDate());
        oldAbsence.setReason(absence.getReason());
        oldAbsence.setStatus(absence.getStatus());

        // Sauvegarder les anciennes valeurs pour détecter les modifications
        String oldAgentName = absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName();
        AbsenceType oldAbsenceType = absence.getAbsenceType();
        LocalDateTime oldStartDate = absence.getStartDate();
        LocalDateTime oldEndDate = absence.getEndDate();
        String oldReason = absence.getReason();
        AbsenceStatus oldStatus = absence.getStatus();

        // Appliquer les modifications
        absence.setAgent(agent);
        absence.setAbsenceType(request.getAbsenceType());
        absence.setStartDate(request.getStartDate());
        absence.setEndDate(request.getEndDate());
        absence.setReason(request.getReason());
        absence.setStatus(request.getStatus());
        absence.setUpdateDate(LocalDateTime.now());

        Absences updatedAbsence = absencesRepository.save(absence);

        // Détecter tous les changements et construire un message détaillé
        StringBuilder changes = new StringBuilder();
        boolean hasChanges = false;

        // Vérifier changement d'agent
        String newAgentName = agent.getFirstName() + " " + agent.getLastName();
        if (!oldAgentName.equals(newAgentName)) {
            changes.append("Agent: '").append(oldAgentName).append("' → '")
                   .append(newAgentName).append("' | ");
            hasChanges = true;
        }

        // Vérifier changement de type d'absence
        if (oldAbsenceType != request.getAbsenceType()) {
            changes.append("Type: '").append(oldAbsenceType).append("' → '")
                   .append(request.getAbsenceType()).append("' | ");
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

        // Vérifier changement de raison
        if (oldReason != null && request.getReason() != null && !oldReason.equals(request.getReason())) {
            changes.append("Raison: '").append(oldReason).append("' → '")
                   .append(request.getReason()).append("' | ");
            hasChanges = true;
        }

        // Vérifier changement de statut
        if (oldStatus != request.getStatus()) {
            changes.append("Statut: '").append(oldStatus).append("' → '")
                   .append(request.getStatus()).append("' | ");
            hasChanges = true;
        }

        // Si des modifications ont été détectées, envoyer une notification
        if (hasChanges) {
            // Supprimer le dernier " | "
            String changesMessage = changes.substring(0, changes.length() - 3);
            notifyAbsenceModification(updatedAbsence, changesMessage);
            log.info("Absence modifiée - Changements: {}", changesMessage);

            // Enregistrer dans l'historique (sans oldValue/newValue pour les UPDATE)
            historiesService.recordHistory(
                    request.getAgentTrackingId(),
                    "ABSENCE",
                    updatedAbsence.getTrackingId(),
                    ActionType.UPDATE,
                    changesMessage,
                    null,
                    null
            );
        }

        return absencesMapper.toResponse(updatedAbsence);
    }

    /**
     * Notifies all users when an absence is modified
     * Sends a consolidated notification with all field changes
     */
    private void notifyAbsenceModification(Absences absence, String changesMessage) {
        // Notify all users with broadcast message
        List<Users> allUsers = usersRepository.findAll();
        String broadcastMessage = "L'absence de " +
            absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName() +
            " a été modifiée. Changements: " + changesMessage;

        for (Users user : allUsers) {
            try {
                NotificationsRequest userNotification = new NotificationsRequest();
                userNotification.setMessage(broadcastMessage);
                userNotification.setNotificationType("absences");
                userNotification.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(userNotification);
            } catch (Exception e) {
                log.error("❌ Erreur notification utilisateur {}: {}",
                    user.getFirstName() + " " + user.getLastName(), e.getMessage());
            }
        }

        log.info("✅ Notifications de modification envoyées à {} utilisateurs", allUsers.size());
    }

    private void notifyAgentOfStatusChange(Absences absence, AbsenceStatus newStatus) {
        try {
            String statusMessage;
            switch (newStatus) {
                case APPROVED:
                    statusMessage = "Votre demande d'absence du " +
                            absence.getStartDate().format(DATE_FORMATTER) +
                            " au " + absence.getEndDate().format(DATE_FORMATTER) +
                            " a été approuvée";
                    break;
                case REJECTED:
                    statusMessage = "Votre demande d'absence du " +
                            absence.getStartDate().format(DATE_FORMATTER) +
                            " au " + absence.getEndDate().format(DATE_FORMATTER) +
                            " a été rejetée";
                    break;
                case CANCELLED:
                    statusMessage = "Votre demande d'absence du " +
                            absence.getStartDate().format(DATE_FORMATTER) +
                            " au " + absence.getEndDate().format(DATE_FORMATTER) +
                            " a été annulée";
                    break;
                default:
                    statusMessage = "Le statut de votre demande d'absence a été mis à jour";
            }

            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(statusMessage);
            notificationRequest.setNotificationType("absences");
            notificationRequest.setRecipientTrackingId(absence.getAgent().getTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification de changement de statut envoyée à l'agent {} pour l'absence",
                    absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
        }

        // Notify all users about the status change
        notifyAllUsersOfAbsenceStatusChange(absence, newStatus);
    }

    /**
     * Notifies all users when a new absence request is created
     */
    private void notifyAllUsersOfAbsenceRequest(Absences absence, Agents agent) {
        List<Users> allUsers = usersRepository.findAll();

        String message = agent.getFirstName() + " " + agent.getLastName() +
                " a soumis une demande d'absence du " + absence.getStartDate().format(DATE_FORMATTER) +
                " au " + absence.getEndDate().format(DATE_FORMATTER);

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("absences");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de demande d'absence: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de demande d'absence envoyée à {} utilisateurs", allUsers.size());
    }

    /**
     * Notifies all users when an absence status changes
     */
    private void notifyAllUsersOfAbsenceStatusChange(Absences absence, AbsenceStatus newStatus) {
        List<Users> allUsers = usersRepository.findAll();

        String statusMessage;
        switch (newStatus) {
            case APPROVED:
                statusMessage = "La demande d'absence de " +
                        absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName() +
                        " du " + absence.getStartDate().format(DATE_FORMATTER) +
                        " au " + absence.getEndDate().format(DATE_FORMATTER) +
                        " a été approuvée";
                break;
            case REJECTED:
                statusMessage = "La demande d'absence de " +
                        absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName() +
                        " du " + absence.getStartDate().format(DATE_FORMATTER) +
                        " au " + absence.getEndDate().format(DATE_FORMATTER) +
                        " a été rejetée";
                break;
            case CANCELLED:
                statusMessage = "La demande d'absence de " +
                        absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName() +
                        " du " + absence.getStartDate().format(DATE_FORMATTER) +
                        " au " + absence.getEndDate().format(DATE_FORMATTER) +
                        " a été annulée";
                break;
            default:
                statusMessage = "Le statut d'une demande d'absence a été mis à jour";
        }

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(statusMessage);
                notificationRequest.setNotificationType("absences");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de statut: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de changement de statut envoyée à {} utilisateurs", allUsers.size());
    }

    /**
     * Notifies the agent when their absence is modified
     */
    private void notifyAgentOfAbsenceModification(Absences absence) {
        try {
            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(
                    "Votre demande d'absence a été modifiée. Nouvelles dates : du " +
                            absence.getStartDate().format(DATE_FORMATTER) +
                            " au " + absence.getEndDate().format(DATE_FORMATTER)
            );
            notificationRequest.setNotificationType("absences");
            notificationRequest.setRecipientTrackingId(absence.getAgent().getTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification de modification envoyée à l'agent {} pour l'absence",
                    absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
        }

        // Notify all users about the modification
        notifyAllUsersOfAbsenceModification(absence);
    }

    /**
     * Notifies all users when an absence is modified
     */
    private void notifyAllUsersOfAbsenceModification(Absences absence) {
        List<Users> allUsers = usersRepository.findAll();

        String message = "La demande d'absence de " +
                absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName() +
                " a été modifiée. Nouvelles dates : du " +
                absence.getStartDate().format(DATE_FORMATTER) +
                " au " + absence.getEndDate().format(DATE_FORMATTER);

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("absences");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de modification: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de modification envoyée à {} utilisateurs", allUsers.size());
    }

    /**
     * Notifies the agent when their absence is deleted
     */
    private void notifyAgentOfAbsenceDeletion(Absences absence) {
        try {
            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(
                    "Votre demande d'absence du " +
                            absence.getStartDate().format(DATE_FORMATTER) +
                            " au " + absence.getEndDate().format(DATE_FORMATTER) +
                            " a été supprimée"
            );
            notificationRequest.setNotificationType("absences");
            notificationRequest.setRecipientTrackingId(absence.getAgent().getTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification de suppression envoyée à l'agent {} pour l'absence",
                    absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification: {}", e.getMessage());
        }
    }

    /**
     * Notifies all users when an absence is deleted
     */
    private void notifyAllUsersOfAbsenceDeletion(Absences absence) {
        List<Users> allUsers = usersRepository.findAll();

        String message = "La demande d'absence de " +
                absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName() +
                " du " + absence.getStartDate().format(DATE_FORMATTER) +
                " au " + absence.getEndDate().format(DATE_FORMATTER) +
                " a été supprimée";

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("absences");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de suppression: {}", e.getMessage());
            }
        }

        log.info("✅ Notification de suppression envoyée à {} utilisateurs", allUsers.size());
    }

    @Override
    @Transactional(readOnly = true)
    public AbsencesResponse findByTrackingId(UUID trackingId) {
        Absences absence = absencesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Absence not found with trackingId: " + trackingId));
        return absencesMapper.toResponse(absence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsencesResponse> findByAgent(UUID agentTrackingId) {
        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + agentTrackingId));
        return absencesRepository.findByAgent(agent)
                .stream()
                .map(absencesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsencesResponse> findByStatus(AbsenceStatus status) {
        return absencesRepository.findByStatus(status)
                .stream()
                .map(absencesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsencesResponse> findByAbsenceType(AbsenceType absenceType) {
        return absencesRepository.findByAbsenceType(absenceType)
                .stream()
                .map(absencesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsencesResponse> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return absencesRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(absencesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbsencesResponse> findAll() {
        return absencesRepository.findAll()
                .stream()
                .map(absencesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AbsencesResponse updateStatus(UUID trackingId, AbsenceStatus status, UUID validatedByTrackingId) {
        Absences absence = absencesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Absence not found with trackingId: " + trackingId));

        Users validatedBy = usersRepository.findByTrackingId(validatedByTrackingId)
                .orElseThrow(() -> new RuntimeException("User not found with trackingId: " + validatedByTrackingId));

        AbsenceStatus oldStatus = absence.getStatus();
        absence.setStatus(status);
        absence.setValidatedBy(validatedBy);
        absence.setUpdateDate(LocalDateTime.now());

        Absences updatedAbsence = absencesRepository.save(absence);

        // Notify agent of status change
        if (oldStatus != status) {
            notifyAgentOfStatusChange(updatedAbsence, status);
        }

        return absencesMapper.toResponse(updatedAbsence);
    }

    @Override
    public void delete(UUID trackingId) {
        Absences absence = absencesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Absence not found with trackingId: " + trackingId));

        // Notify agent and all users before deletion
        notifyAgentOfAbsenceDeletion(absence);
        notifyAllUsersOfAbsenceDeletion(absence);

        // Enregistrer dans l'historique avant suppression
        historiesService.recordHistory(
                absence.getAgent().getTrackingId(),
                "ABSENCE",
                absence.getTrackingId(),
                ActionType.DELETE,
                "Suppression de l'absence: " + absence.getAbsenceType() + " du " +
                        absence.getStartDate().format(DATE_FORMATTER) + " au " +
                        absence.getEndDate().format(DATE_FORMATTER),
                absence,
                null
        );

        absencesRepository.delete(absence);
    }
}