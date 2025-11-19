package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.EscortMissionRequest;
import com.helpysoft.mima_api.dto.EscortMissionResponse;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.entity.*;
import com.helpysoft.mima_api.mapper.EscortMissionMapper;
import com.helpysoft.mima_api.repository.*;
import com.helpysoft.mima_api.service.EscortMissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EscortMissionServiceImpl implements EscortMissionService {

    private final EscortMissionRepository escortMissionRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final SecurityAgencyRepository securityAgencyRepository;
    private final NavalVesselRepository navalVesselRepository;
    private final AgentsRepository agentsRepository;
    private final EscortMissionMapper escortMissionMapper;
    private final HistoriesServiceImpl historiesService;
    private final NotificationsServiceImpl notificationsService;
    private final UsersRepository usersRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public EscortMissionResponse create(EscortMissionRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(request.getCommercialShipTrackingId())
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(request.getSecurityAgencyTrackingId())
                .orElseThrow(() -> new RuntimeException("Security agency not found"));

        NavalVessels vessel = navalVesselRepository.findByTrackingId(request.getNavalVesselTrackingId())
                .orElseThrow(() -> new RuntimeException("Naval vessel not found"));

        Agents commander = agentsRepository.findByTrackingId(request.getCommanderTrackingId())
                .orElseThrow(() -> new RuntimeException("Commander not found"));

        NavalVessels secondaryVessel = null;
        if (request.getSecondaryVesselTrackingId() != null) {
            secondaryVessel = navalVesselRepository.findByTrackingId(request.getSecondaryVesselTrackingId())
                    .orElse(null);
        }

        EscortMissions mission = escortMissionMapper.toEntity(request, ship, agency, vessel, commander, secondaryVessel);
        EscortMissions savedMission = escortMissionRepository.save(mission);

        // Enregistrer dans l'historique
        try {
            String summary = String.format(
                "Création de la mission d'escorte %s - Navire: %s - Patrouilleur: %s - Du %s au %s",
                savedMission.getMissionNumber(),
                ship.getShipName(),
                vessel.getVesselName(),
                savedMission.getStartDate().format(DATE_FORMATTER),
                savedMission.getEndDate() != null ? savedMission.getEndDate().format(DATE_FORMATTER) : "N/A"
            );

            historiesService.recordHistory(
                commander.getTrackingId(),
                "ESCORT_MISSION",
                savedMission.getTrackingId(),
                ActionType.CREATE,
                summary,
                null,
                savedMission
            );
            log.info("✅ Historique enregistré pour la mission d'escorte {}", savedMission.getMissionNumber());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }

        // Notifier tous les utilisateurs de la création
        notifyAllUsersOfEscortMissionCreation(savedMission, ship, vessel);

        return escortMissionMapper.toResponse(savedMission);
    }

    @Override
    public EscortMissionResponse update(UUID trackingId, EscortMissionRequest request) {
        EscortMissions mission = escortMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Escort mission not found"));

        // Sauvegarder les anciennes valeurs pour l'historique
        String oldShipName = mission.getCommercialShip().getShipName();
        String oldVesselName = mission.getNavalVessel().getVesselName();
        MissionStatus oldStatus = mission.getStatus();
        LocalDateTime oldStartDate = mission.getStartDate();
        LocalDateTime oldEndDate = mission.getEndDate();

        CommercialShips ship = commercialShipRepository.findByTrackingId(request.getCommercialShipTrackingId())
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(request.getSecurityAgencyTrackingId())
                .orElseThrow(() -> new RuntimeException("Security agency not found"));

        NavalVessels vessel = navalVesselRepository.findByTrackingId(request.getNavalVesselTrackingId())
                .orElseThrow(() -> new RuntimeException("Naval vessel not found"));

        Agents commander = agentsRepository.findByTrackingId(request.getCommanderTrackingId())
                .orElseThrow(() -> new RuntimeException("Commander not found"));

        NavalVessels secondaryVessel = null;
        if (request.getSecondaryVesselTrackingId() != null) {
            secondaryVessel = navalVesselRepository.findByTrackingId(request.getSecondaryVesselTrackingId())
                    .orElse(null);
        }

        mission.setCommercialShip(ship);
        mission.setSecurityAgency(agency);
        mission.setNavalVessel(vessel);
        mission.setCommander(commander);
        mission.setCommanderRank(request.getCommanderRank());
        mission.setSecondaryVessel(secondaryVessel);
        mission.setVedettes(request.getVedettes());
        mission.setStartDate(request.getStartDate());
        mission.setEndDate(request.getEndDate());
        mission.setEscortType(request.getEscortType());
        mission.setDeparturePoint(request.getDeparturePoint());
        mission.setArrivalPoint(request.getArrivalPoint());
        mission.setDistance(request.getDistance());
        mission.setEscortZone(request.getEscortZone());
        mission.setStatus(request.getStatus());
        mission.setIncidents(request.getIncidents());
        mission.setObservations(request.getObservations());

        EscortMissions updatedMission = escortMissionRepository.save(mission);

        // Détection des changements et enregistrement dans l'historique
        StringBuilder changes = new StringBuilder();
        boolean hasChanges = false;

        if (!oldShipName.equals(ship.getShipName())) {
            changes.append(String.format("Navire: '%s' → '%s' | ", oldShipName, ship.getShipName()));
            hasChanges = true;
        }

        if (!oldVesselName.equals(vessel.getVesselName())) {
            changes.append(String.format("Patrouilleur: '%s' → '%s' | ", oldVesselName, vessel.getVesselName()));
            hasChanges = true;
        }

        if (!oldStatus.equals(request.getStatus())) {
            changes.append(String.format("Statut: '%s' → '%s' | ", oldStatus, request.getStatus()));
            hasChanges = true;
        }

        if (!oldStartDate.equals(request.getStartDate())) {
            changes.append(String.format("Date début: '%s' → '%s' | ",
                oldStartDate.format(DATE_FORMATTER), request.getStartDate().format(DATE_FORMATTER)));
            hasChanges = true;
        }

        if (!Objects.equals(oldEndDate, request.getEndDate())) {
            changes.append(String.format("Date fin: '%s' → '%s' | ",
                oldEndDate != null ? oldEndDate.format(DATE_FORMATTER) : "N/A",
                request.getEndDate() != null ? request.getEndDate().format(DATE_FORMATTER) : "N/A"));
            hasChanges = true;
        }

        if (hasChanges) {
            String changesMessage = changes.substring(0, changes.length() - 3);
            try {
                String summary = "Modification de la mission d'escorte " + updatedMission.getMissionNumber() + " - " + changesMessage;

                historiesService.recordHistory(
                    commander.getTrackingId(),
                    "ESCORT_MISSION",
                    updatedMission.getTrackingId(),
                    ActionType.UPDATE,
                    summary,
                    null,
                    updatedMission
                );
                log.info("✅ Historique de modification enregistré pour la mission d'escorte {}", updatedMission.getMissionNumber());
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
            }

            // Notifier tous les utilisateurs de la modification
            notifyEscortMissionModification(updatedMission, changesMessage);
        }

        return escortMissionMapper.toResponse(updatedMission);
    }

    @Override
    @Transactional(readOnly = true)
    public EscortMissionResponse findByTrackingId(UUID trackingId) {
        EscortMissions mission = escortMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Escort mission not found"));
        return escortMissionMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public EscortMissionResponse findByMissionNumber(String missionNumber) {
        EscortMissions mission = escortMissionRepository.findByMissionNumber(missionNumber)
                .orElseThrow(() -> new RuntimeException("Escort mission not found"));
        return escortMissionMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByStatus(MissionStatus status) {
        return escortMissionRepository.findByStatus(status)
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return escortMissionRepository.findByPeriod(startDate, endDate)
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByCommercialShip(UUID shipTrackingId) {
        return escortMissionRepository.findByCommercialShipTrackingId(shipTrackingId)
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findBySecurityAgency(UUID agencyTrackingId) {
        return escortMissionRepository.findBySecurityAgencyTrackingId(agencyTrackingId)
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByNavalVessel(UUID vesselTrackingId) {
        return escortMissionRepository.findByNavalVesselTrackingId(vesselTrackingId)
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByCommander(UUID commanderTrackingId) {
        return escortMissionRepository.findByCommanderTrackingId(commanderTrackingId)
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findAll() {
        return escortMissionRepository.findAll()
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        EscortMissions mission = escortMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Escort mission not found"));

        // Sauvegarder les informations avant suppression
        String missionNumber = mission.getMissionNumber();
        String shipName = mission.getCommercialShip().getShipName();
        UUID commanderTrackingId = mission.getCommander().getTrackingId();
        LocalDateTime startDate = mission.getStartDate();

        // Notifier tous les utilisateurs avant la suppression
        notifyAllUsersOfEscortMissionDeletion(mission);

        escortMissionRepository.delete(mission);

        // Enregistrer dans l'historique après suppression
        try {
            String summary = String.format(
                "Suppression de la mission d'escorte %s - Navire: %s - Début: %s",
                missionNumber,
                shipName,
                startDate.format(DATE_FORMATTER)
            );

            historiesService.recordHistory(
                commanderTrackingId,
                "ESCORT_MISSION",
                trackingId,
                ActionType.DELETE,
                summary,
                null,
                null
            );
            log.info("✅ Historique de suppression enregistré pour la mission d'escorte {}", missionNumber);
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }
    }

    // Helper methods pour les notifications
    private void notifyAllUsersOfEscortMissionCreation(EscortMissions mission, CommercialShips ship, NavalVessels vessel) {
        List<Users> allUsers = usersRepository.findAll();

        String message = String.format(
            "Nouvelle mission d'escorte créée : %s - Navire: %s - Patrouilleur: %s - Du %s au %s",
            mission.getMissionNumber(),
            ship.getShipName(),
            vessel.getVesselName(),
            mission.getStartDate().format(DATE_FORMATTER),
            mission.getEndDate() != null ? mission.getEndDate().format(DATE_FORMATTER) : "N/A"
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("escort_missions");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de création: {}", e.getMessage());
            }
        }
    }

    private void notifyEscortMissionModification(EscortMissions mission, String changesMessage) {
        List<Users> allUsers = usersRepository.findAll();

        String broadcastMessage = String.format(
            "La mission d'escorte '%s' a été modifiée. Changements: %s",
            mission.getMissionNumber(),
            changesMessage
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest userNotification = new NotificationsRequest();
                userNotification.setMessage(broadcastMessage);
                userNotification.setNotificationType("escort_missions");
                userNotification.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(userNotification);
            } catch (Exception e) {
                log.error("❌ Erreur lors de la notification de modification: {}", e.getMessage());
            }
        }
    }

    private void notifyAllUsersOfEscortMissionDeletion(EscortMissions mission) {
        List<Users> allUsers = usersRepository.findAll();

        String message = String.format(
            "La mission d'escorte '%s' (Navire: %s) a été supprimée",
            mission.getMissionNumber(),
            mission.getCommercialShip().getShipName()
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("escort_missions");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de suppression: {}", e.getMessage());
            }
        }
    }
}
