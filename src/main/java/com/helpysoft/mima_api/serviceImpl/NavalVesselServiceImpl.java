package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.NavalVesselRequest;
import com.helpysoft.mima_api.dto.NavalVesselResponse;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.NavalVesselStatus;
import com.helpysoft.mima_api.entity.NavalVesselType;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.NavalVesselMapper;
import com.helpysoft.mima_api.repository.NavalVesselRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.NavalVesselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NavalVesselServiceImpl implements NavalVesselService {

    private final NavalVesselRepository navalVesselRepository;
    private final NavalVesselMapper navalVesselMapper;
    private final HistoriesServiceImpl historiesService;
    private final NotificationsServiceImpl notificationsService;
    private final UsersRepository usersRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public NavalVesselResponse create(NavalVesselRequest request) {
        NavalVessels vessel = navalVesselMapper.toEntity(request);
        NavalVessels savedVessel = navalVesselRepository.save(vessel);

        // Enregistrer dans l'historique
        try {
            String summary = String.format(
                "Création du moyen maritime %s - Numéro: %s - Type: %s - Statut: %s",
                savedVessel.getVesselName(),
                savedVessel.getVesselNumber(),
                savedVessel.getVesselType(),
                savedVessel.getOperationalStatus()
            );

            historiesService.recordHistory(
                null,
                "NAVAL_VESSEL",
                savedVessel.getTrackingId(),
                ActionType.CREATE,
                summary,
                null,
                savedVessel
            );
            log.info("Historique enregistre pour le moyen maritime {}", savedVessel.getVesselName());
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }

        // Notifier tous les utilisateurs de la création
        notifyAllUsersOfNavalVesselCreation(savedVessel);

        return navalVesselMapper.toResponse(savedVessel);
    }

    @Override
    public NavalVesselResponse update(UUID trackingId, NavalVesselRequest request) {
        NavalVessels vessel = navalVesselRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found with trackingId: " + trackingId));

        // Sauvegarder les anciennes valeurs pour l'historique
        String oldVesselName = vessel.getVesselName();
        String oldVesselNumber = vessel.getVesselNumber();
        NavalVesselType oldVesselType = vessel.getVesselType();
        NavalVesselStatus oldOperationalStatus = vessel.getOperationalStatus();
        String oldCurrentMission = vessel.getCurrentMission();

        vessel.setVesselNumber(request.getVesselNumber());
        vessel.setVesselType(request.getVesselType());
        vessel.setVesselName(request.getVesselName());
        vessel.setHullNumber(request.getHullNumber());
        vessel.setYearCommissioned(request.getYearCommissioned());
        vessel.setDateCommissioned(request.getDateCommissioned());
        vessel.setDateDecommissioned(request.getDateDecommissioned());
        vessel.setLength(request.getLength());
        vessel.setWidth(request.getWidth());
        vessel.setDraft(request.getDraft());
        vessel.setDisplacement(request.getDisplacement());
        vessel.setMaxSpeed(request.getMaxSpeed());
        vessel.setCrewCapacity(request.getCrewCapacity());
        vessel.setFuelCapacity(request.getFuelCapacity());
        vessel.setRange(request.getRange());
        vessel.setArmament(request.getArmament());
        vessel.setElectronics(request.getElectronics());
        vessel.setEngineType(request.getEngineType());
        vessel.setEnginePower(request.getEnginePower());
        vessel.setHomePort(request.getHomePort());
        vessel.setOperationalStatus(request.getOperationalStatus());
        vessel.setCurrentLocation(request.getCurrentLocation());
        vessel.setCurrentMission(request.getCurrentMission());
        vessel.setLastMaintenanceDate(request.getLastMaintenanceDate());
        vessel.setNextMaintenanceDate(request.getNextMaintenanceDate());
        vessel.setTotalOperationalHours(request.getTotalOperationalHours());
        vessel.setObservations(request.getObservations());
        vessel.setIsActive(request.getIsActive());

        NavalVessels updatedVessel = navalVesselRepository.save(vessel);

        // Détection des changements et enregistrement dans l'historique
        StringBuilder changes = new StringBuilder();
        boolean hasChanges = false;

        if (!oldVesselName.equals(request.getVesselName())) {
            changes.append(String.format("Nom: '%s' → '%s' | ", oldVesselName, request.getVesselName()));
            hasChanges = true;
        }

        if (!oldVesselNumber.equals(request.getVesselNumber())) {
            changes.append(String.format("Numéro: '%s' → '%s' | ", oldVesselNumber, request.getVesselNumber()));
            hasChanges = true;
        }

        if (!oldVesselType.equals(request.getVesselType())) {
            changes.append(String.format("Type: '%s' → '%s' | ", oldVesselType, request.getVesselType()));
            hasChanges = true;
        }

        if (!oldOperationalStatus.equals(request.getOperationalStatus())) {
            changes.append(String.format("Statut: '%s' → '%s' | ", oldOperationalStatus, request.getOperationalStatus()));
            hasChanges = true;
        }

        if (!Objects.equals(oldCurrentMission, request.getCurrentMission())) {
            changes.append(String.format("Mission: '%s' → '%s' | ",
                oldCurrentMission != null ? oldCurrentMission : "N/A",
                request.getCurrentMission() != null ? request.getCurrentMission() : "N/A"));
            hasChanges = true;
        }

        if (hasChanges) {
            String changesMessage = changes.substring(0, changes.length() - 3);

            try {
                String summary = "Modification du moyen maritime " + updatedVessel.getVesselName() + " - " +
                    changesMessage;

                historiesService.recordHistory(
                    null,
                    "NAVAL_VESSEL",
                    updatedVessel.getTrackingId(),
                    ActionType.UPDATE,
                    summary,
                    null,
                    updatedVessel
                );
                log.info("Historique de modification enregistre pour le moyen maritime {}", updatedVessel.getVesselName());
            } catch (Exception e) {
                log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
            }

            // Notifier tous les utilisateurs de la modification
            notifyNavalVesselModification(updatedVessel, changesMessage);
        }

        return navalVesselMapper.toResponse(updatedVessel);
    }

    @Override
    @Transactional(readOnly = true)
    public NavalVesselResponse findByTrackingId(UUID trackingId) {
        NavalVessels vessel = navalVesselRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found with trackingId: " + trackingId));
        return navalVesselMapper.toResponse(vessel);
    }

    @Override
    @Transactional(readOnly = true)
    public NavalVesselResponse findByVesselNumber(String vesselNumber) {
        NavalVessels vessel = navalVesselRepository.findByVesselNumber(vesselNumber)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found with vessel number: " + vesselNumber));
        return navalVesselMapper.toResponse(vessel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findByVesselType(NavalVesselType vesselType) {
        return navalVesselRepository.findByVesselType(vesselType)
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findByOperationalStatus(NavalVesselStatus status) {
        return navalVesselRepository.findByOperationalStatus(status)
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findAvailableVessels() {
        return navalVesselRepository.findAvailableVessels()
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findPatrolVessels() {
        return navalVesselRepository.findPatrolVessels()
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findAll() {
        return navalVesselRepository.findAll()
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> searchNavalVessels(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return navalVesselRepository.searchNavalVessels(searchTerm)
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        NavalVessels vessel = navalVesselRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found with trackingId: " + trackingId));

        // Sauvegarder les informations avant suppression
        String vesselName = vessel.getVesselName();
        String vesselNumber = vessel.getVesselNumber();
        NavalVesselType vesselType = vessel.getVesselType();

        // Notifier tous les utilisateurs avant la suppression
        notifyAllUsersOfNavalVesselDeletion(vessel);

        navalVesselRepository.delete(vessel);

        // Enregistrer dans l'historique après suppression
        try {
            String summary = String.format(
                "Suppression du moyen maritime %s - Numéro: %s - Type: %s",
                vesselName,
                vesselNumber,
                vesselType
            );

            historiesService.recordHistory(
                null,
                "NAVAL_VESSEL",
                trackingId,
                ActionType.DELETE,
                summary,
                null,
                null
            );
            log.info("Historique de suppression enregistre pour le moyen maritime {}", vesselName);
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }
    }

    // Helper methods pour les notifications
    private void notifyAllUsersOfNavalVesselCreation(NavalVessels vessel) {
        List<Users> allUsers = usersRepository.findAll();

        String message = String.format(
            "Nouveau moyen maritime enregistré : %s - Type: %s - Indicatif: %s",
            vessel.getVesselName(),
            vessel.getVesselType(),
            vessel.getCallSign()
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("naval_vessels");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de création: {}", e.getMessage());
            }
        }
    }

    private void notifyNavalVesselModification(NavalVessels vessel, String changesMessage) {
        List<Users> allUsers = usersRepository.findAll();

        String broadcastMessage = String.format(
            "Le moyen maritime '%s' a été modifié. Changements: %s",
            vessel.getVesselName(),
            changesMessage
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest userNotification = new NotificationsRequest();
                userNotification.setMessage(broadcastMessage);
                userNotification.setNotificationType("naval_vessels");
                userNotification.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(userNotification);
            } catch (Exception e) {
                log.error("❌ Erreur lors de la notification de modification: {}", e.getMessage());
            }
        }
    }

    private void notifyAllUsersOfNavalVesselDeletion(NavalVessels vessel) {
        List<Users> allUsers = usersRepository.findAll();

        String message = String.format(
            "Le moyen maritime '%s' (Type: %s) a été supprimé",
            vessel.getVesselName(),
            vessel.getVesselType()
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("naval_vessels");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de suppression: {}", e.getMessage());
            }
        }
    }
}
