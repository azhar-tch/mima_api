package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.CommercialShipRequest;
import com.helpysoft.mima_api.dto.CommercialShipResponse;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ShipStatus;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.CommercialShipMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.service.CommercialShipService;
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
public class CommercialShipServiceImpl implements CommercialShipService {

    private final CommercialShipRepository commercialShipRepository;
    private final CommercialShipMapper commercialShipMapper;
    private final HistoriesServiceImpl historiesService;
    private final NotificationsServiceImpl notificationsService;
    private final UsersRepository usersRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public CommercialShipResponse create(CommercialShipRequest request) {
        CommercialShips ship = commercialShipMapper.toEntity(request);
        CommercialShips savedShip = commercialShipRepository.save(ship);

        // Enregistrer dans l'historique
        try {
            String summary = String.format(
                "Création du navire commercial %s - IMO: %s - Type: %s - Pavillon: %s",
                savedShip.getShipName(),
                savedShip.getImoNumber(),
                savedShip.getShipType(),
                savedShip.getFlag()
            );

            historiesService.recordHistory(
                null,
                "COMMERCIAL_SHIP",
                savedShip.getTrackingId(),
                ActionType.CREATE,
                summary,
                null,
                savedShip
            );
            log.info("Historique enregistre pour le navire commercial {}", savedShip.getShipName());
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }

        // Notifier tous les utilisateurs de la création
        notifyAllUsersOfCommercialShipCreation(savedShip);

        return commercialShipMapper.toResponse(savedShip);
    }

    @Override
    public CommercialShipResponse update(UUID trackingId, CommercialShipRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found with trackingId: " + trackingId));

        // Sauvegarder les anciennes valeurs pour l'historique
        String oldShipName = ship.getShipName();
        String oldImoNumber = ship.getImoNumber();
        String oldShipType = ship.getShipType();
        ShipStatus oldStatus = ship.getStatus();
        String oldFlag = ship.getFlag();

        ship.setImoNumber(request.getImoNumber());
        ship.setShipName(request.getShipName());
        ship.setShipType(request.getShipType());
        ship.setFlag(request.getFlag());
        ship.setMmsi(request.getMmsi());
        ship.setCallSign(request.getCallSign());
        ship.setGrossTonnage(request.getGrossTonnage());
        ship.setDeadWeight(request.getDeadWeight());
        ship.setLength(request.getLength());
        ship.setWidth(request.getWidth());
        ship.setDraft(request.getDraft());
        ship.setYearBuilt(request.getYearBuilt());
        ship.setShipOwner(request.getShipOwner());
        ship.setOperator(request.getOperator());
        ship.setLastPort(request.getLastPort());
        ship.setNextPort(request.getNextPort());
        ship.setCargoType(request.getCargoType());
        ship.setArrivalDate(request.getArrivalDate());
        ship.setDepartureDate(request.getDepartureDate());
        ship.setStatus(request.getStatus());
        ship.setObservations(request.getObservations());
        ship.setIsActive(request.getIsActive());

        CommercialShips updatedShip = commercialShipRepository.save(ship);

        // Détection des changements et enregistrement dans l'historique
        StringBuilder changes = new StringBuilder();
        boolean hasChanges = false;

        if (!oldShipName.equals(request.getShipName())) {
            changes.append(String.format("Nom: '%s' → '%s' | ", oldShipName, request.getShipName()));
            hasChanges = true;
        }

        if (!oldImoNumber.equals(request.getImoNumber())) {
            changes.append(String.format("IMO: '%s' → '%s' | ", oldImoNumber, request.getImoNumber()));
            hasChanges = true;
        }

        if (!oldShipType.equals(request.getShipType())) {
            changes.append(String.format("Type: '%s' → '%s' | ", oldShipType, request.getShipType()));
            hasChanges = true;
        }

        if (!Objects.equals(oldStatus, request.getStatus())) {
            changes.append(String.format("Statut: '%s' → '%s' | ",
                oldStatus != null ? oldStatus.name() : "N/A",
                request.getStatus() != null ? request.getStatus().name() : "N/A"));
            hasChanges = true;
        }

        if (!oldFlag.equals(request.getFlag())) {
            changes.append(String.format("Pavillon: '%s' → '%s' | ", oldFlag, request.getFlag()));
            hasChanges = true;
        }

        if (hasChanges) {
            String changesMessage = changes.substring(0, changes.length() - 3);
            try {
                String summary = "Modification du navire commercial " + updatedShip.getShipName() + " - " + changesMessage;

                historiesService.recordHistory(
                    null,
                    "COMMERCIAL_SHIP",
                    updatedShip.getTrackingId(),
                    ActionType.UPDATE,
                    summary,
                    null,
                    updatedShip
                );
                log.info("Historique de modification enregistre pour le navire commercial {}", updatedShip.getShipName());
            } catch (Exception e) {
                log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
            }

            // Notifier tous les utilisateurs de la modification
            notifyCommercialShipModification(updatedShip, changesMessage);
        }

        return commercialShipMapper.toResponse(updatedShip);
    }

    @Override
    @Transactional(readOnly = true)
    public CommercialShipResponse findByTrackingId(UUID trackingId) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found with trackingId: " + trackingId));
        return commercialShipMapper.toResponse(ship);
    }

    @Override
    @Transactional(readOnly = true)
    public CommercialShipResponse findByImoNumber(String imoNumber) {
        CommercialShips ship = commercialShipRepository.findByImoNumber(imoNumber)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found with IMO number: " + imoNumber));
        return commercialShipMapper.toResponse(ship);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findByShipType(String shipType) {
        return commercialShipRepository.findByShipType(shipType)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findByStatus(String status) {
        return commercialShipRepository.findByStatus(status)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findByFlag(String flag) {
        return commercialShipRepository.findByFlag(flag)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return commercialShipRepository.findByArrivalDateBetween(startDate, endDate)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findAll() {
        return commercialShipRepository.findAll()
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> searchCommercialShips(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return commercialShipRepository.searchCommercialShips(searchTerm)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found with trackingId: " + trackingId));

        // Sauvegarder les informations avant suppression
        String shipName = ship.getShipName();
        String imoNumber = ship.getImoNumber();
        String shipType = ship.getShipType();

        // Notifier tous les utilisateurs avant la suppression
        notifyAllUsersOfCommercialShipDeletion(ship);

        commercialShipRepository.delete(ship);

        // Enregistrer dans l'historique après suppression
        try {
            String summary = String.format(
                "Suppression du navire commercial %s - IMO: %s - Type: %s",
                shipName,
                imoNumber,
                shipType
            );

            historiesService.recordHistory(
                null,
                "COMMERCIAL_SHIP",
                trackingId,
                ActionType.DELETE,
                summary,
                null,
                null
            );
            log.info("Historique de suppression enregistre pour le navire commercial {}", shipName);
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }
    }

    // Helper methods pour les notifications
    private void notifyAllUsersOfCommercialShipCreation(CommercialShips ship) {
        List<Users> allUsers = usersRepository.findAll();

        String message = String.format(
            "Nouveau navire commercial enregistré : %s - IMO: %s - Type: %s - Pavillon: %s",
            ship.getShipName(),
            ship.getImoNumber(),
            ship.getShipType(),
            ship.getFlag()
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("commercial_ships");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de création: {}", e.getMessage());
            }
        }
    }

    private void notifyCommercialShipModification(CommercialShips ship, String changesMessage) {
        List<Users> allUsers = usersRepository.findAll();

        String broadcastMessage = String.format(
            "Le navire commercial '%s' (IMO: %s) a été modifié. Changements: %s",
            ship.getShipName(),
            ship.getImoNumber(),
            changesMessage
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest userNotification = new NotificationsRequest();
                userNotification.setMessage(broadcastMessage);
                userNotification.setNotificationType("commercial_ships");
                userNotification.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(userNotification);
            } catch (Exception e) {
                log.error("❌ Erreur lors de la notification de modification: {}", e.getMessage());
            }
        }
    }

    private void notifyAllUsersOfCommercialShipDeletion(CommercialShips ship) {
        List<Users> allUsers = usersRepository.findAll();

        String message = String.format(
            "Le navire commercial '%s' (IMO: %s) a été supprimé",
            ship.getShipName(),
            ship.getImoNumber()
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("commercial_ships");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de suppression: {}", e.getMessage());
            }
        }
    }
}
