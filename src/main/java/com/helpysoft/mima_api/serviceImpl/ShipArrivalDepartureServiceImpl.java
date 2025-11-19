package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ShipArrivalDepartureRequest;
import com.helpysoft.mima_api.dto.ShipArrivalDepartureResponse;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ShipArrivalDeparture;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.ShipArrivalDepartureMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.ShipArrivalDepartureRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.ShipArrivalDepartureService;
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
public class ShipArrivalDepartureServiceImpl implements ShipArrivalDepartureService {

    private final ShipArrivalDepartureRepository arrivalDepartureRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final ShipArrivalDepartureMapper arrivalDepartureMapper;
    private final HistoriesServiceImpl historiesService;
    private final NotificationsServiceImpl notificationsService;
    private final UsersRepository usersRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public ShipArrivalDepartureResponse create(ShipArrivalDepartureRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        ShipArrivalDeparture arrivalDeparture = arrivalDepartureMapper.toEntity(request, ship);
        ShipArrivalDeparture saved = arrivalDepartureRepository.save(arrivalDeparture);

        // Enregistrer dans l'historique
        try {
            String summary = String.format(
                "Enregistrement sortie/entrée - Navire: %s - Arrivée: %s - Origine: %s - Destination: %s",
                ship.getShipName(),
                saved.getArrivalDate() != null ? saved.getArrivalDate().format(DATE_FORMATTER) : "N/A",
                saved.getPortOfOrigin(),
                saved.getPortOfDestination()
            );

            historiesService.recordHistory(
                null,
                "SHIP_ARRIVAL_DEPARTURE",
                saved.getTrackingId(),
                ActionType.CREATE,
                summary,
                null,
                saved
            );
            log.info("Historique enregistre pour la sortie/entree du navire {}", ship.getShipName());
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }

        // Notifier tous les utilisateurs de la création
        notifyAllUsersOfShipArrivalDepartureCreation(saved);

        return arrivalDepartureMapper.toResponse(saved);
    }

    @Override
    public ShipArrivalDepartureResponse update(UUID trackingId, ShipArrivalDepartureRequest request) {
        ShipArrivalDeparture arrivalDeparture = arrivalDepartureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship arrival/departure record not found"));

        // Sauvegarder les anciennes valeurs pour l'historique
        String oldShipName = arrivalDeparture.getCommercialShip().getShipName();
        LocalDateTime oldArrivalDate = arrivalDeparture.getArrivalDate();
        LocalDateTime oldDepartureDate = arrivalDeparture.getDepartureDate();
        String oldPortOfOrigin = arrivalDeparture.getPortOfOrigin();
        String oldPortOfDestination = arrivalDeparture.getPortOfDestination();

        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        arrivalDepartureMapper.updateEntity(arrivalDeparture, request, ship);
        ShipArrivalDeparture updated = arrivalDepartureRepository.save(arrivalDeparture);

        // Détection des changements et enregistrement dans l'historique
        StringBuilder changes = new StringBuilder();
        boolean hasChanges = false;

        if (!oldShipName.equals(ship.getShipName())) {
            changes.append(String.format("Navire: '%s' → '%s' | ", oldShipName, ship.getShipName()));
            hasChanges = true;
        }

        if (!Objects.equals(oldArrivalDate, updated.getArrivalDate())) {
            changes.append(String.format("Date arrivée: '%s' → '%s' | ",
                oldArrivalDate != null ? oldArrivalDate.format(DATE_FORMATTER) : "N/A",
                updated.getArrivalDate() != null ? updated.getArrivalDate().format(DATE_FORMATTER) : "N/A"));
            hasChanges = true;
        }

        if (!Objects.equals(oldDepartureDate, updated.getDepartureDate())) {
            changes.append(String.format("Date départ: '%s' → '%s' | ",
                oldDepartureDate != null ? oldDepartureDate.format(DATE_FORMATTER) : "N/A",
                updated.getDepartureDate() != null ? updated.getDepartureDate().format(DATE_FORMATTER) : "N/A"));
            hasChanges = true;
        }

        if (!Objects.equals(oldPortOfOrigin, updated.getPortOfOrigin())) {
            changes.append(String.format("Port origine: '%s' → '%s' | ",
                oldPortOfOrigin != null ? oldPortOfOrigin : "N/A",
                updated.getPortOfOrigin() != null ? updated.getPortOfOrigin() : "N/A"));
            hasChanges = true;
        }

        if (!Objects.equals(oldPortOfDestination, updated.getPortOfDestination())) {
            changes.append(String.format("Port destination: '%s' → '%s' | ",
                oldPortOfDestination != null ? oldPortOfDestination : "N/A",
                updated.getPortOfDestination() != null ? updated.getPortOfDestination() : "N/A"));
            hasChanges = true;
        }

        if (hasChanges) {
            String changesMessage = changes.substring(0, changes.length() - 3);

            try {
                String summary = "Modification de sortie/entrée - Navire: " + ship.getShipName() + " - " +
                    changesMessage;

                historiesService.recordHistory(
                    null,
                    "SHIP_ARRIVAL_DEPARTURE",
                    updated.getTrackingId(),
                    ActionType.UPDATE,
                    summary,
                    null,
                    updated
                );
                log.info("Historique de modification enregistre pour la sortie/entree du navire {}", ship.getShipName());
            } catch (Exception e) {
                log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
            }

            // Notifier tous les utilisateurs de la modification
            notifyShipArrivalDepartureModification(updated, changesMessage);
        }

        return arrivalDepartureMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipArrivalDepartureResponse findByTrackingId(UUID trackingId) {
        ShipArrivalDeparture arrivalDeparture = arrivalDepartureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship arrival/departure record not found"));
        return arrivalDepartureMapper.toResponse(arrivalDeparture);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByCommercialShip(UUID shipTrackingId) {
        return arrivalDepartureRepository.findByCommercialShipTrackingId(shipTrackingId)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findShipsCurrentlyInPort() {
        return arrivalDepartureRepository.findShipsCurrentlyInPort()
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return arrivalDepartureRepository.findByArrivalDateBetween(startDate, endDate)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByDepartureDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return arrivalDepartureRepository.findByDepartureDateBetween(startDate, endDate)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByPortOfOrigin(String portOfOrigin) {
        return arrivalDepartureRepository.findByPortOfOrigin(portOfOrigin)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByNextDestination(String nextDestination) {
        return arrivalDepartureRepository.findByPortOfDestination(nextDestination)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findAll() {
        return arrivalDepartureRepository.findAll()
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> searchShipArrivalDepartures(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return arrivalDepartureRepository.searchShipArrivalDepartures(searchTerm)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ShipArrivalDeparture arrivalDeparture = arrivalDepartureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship arrival/departure record not found"));

        // Sauvegarder les informations avant suppression
        String shipName = arrivalDeparture.getCommercialShip().getShipName();
        LocalDateTime arrivalDate = arrivalDeparture.getArrivalDate();
        String portOfOrigin = arrivalDeparture.getPortOfOrigin();

        // Notifier tous les utilisateurs avant la suppression
        notifyAllUsersOfShipArrivalDepartureDeletion(arrivalDeparture);

        arrivalDepartureRepository.delete(arrivalDeparture);

        // Enregistrer dans l'historique après suppression
        try {
            String summary = String.format(
                "Suppression sortie/entrée - Navire: %s - Arrivée: %s - Origine: %s",
                shipName,
                arrivalDate != null ? arrivalDate.format(DATE_FORMATTER) : "N/A",
                portOfOrigin
            );

            historiesService.recordHistory(
                null,
                "SHIP_ARRIVAL_DEPARTURE",
                trackingId,
                ActionType.DELETE,
                summary,
                null,
                null
            );
            log.info("Historique de suppression enregistre pour la sortie/entree du navire {}", shipName);
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }
    }

    // Helper methods pour les notifications
    private void notifyAllUsersOfShipArrivalDepartureCreation(ShipArrivalDeparture movement) {
        List<Users> allUsers = usersRepository.findAll();

        String message = String.format(
            "Nouveau mouvement de navire : %s - Arrivée: %s - Port d'origine: %s",
            movement.getCommercialShip() != null ? movement.getCommercialShip().getShipName() : "N/A",
            movement.getArrivalDate() != null ? movement.getArrivalDate().format(DATE_FORMATTER) : "N/A",
            movement.getPortOfOrigin() != null ? movement.getPortOfOrigin() : "N/A"
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("ship_arrivals_departures");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de création: {}", e.getMessage());
            }
        }
    }

    private void notifyShipArrivalDepartureModification(ShipArrivalDeparture movement, String changesMessage) {
        List<Users> allUsers = usersRepository.findAll();

        String broadcastMessage = String.format(
            "Le mouvement de navire '%s' a été modifié. Changements: %s",
            movement.getCommercialShip() != null ? movement.getCommercialShip().getShipName() : "N/A",
            changesMessage
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest userNotification = new NotificationsRequest();
                userNotification.setMessage(broadcastMessage);
                userNotification.setNotificationType("ship_arrivals_departures");
                userNotification.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(userNotification);
            } catch (Exception e) {
                log.error("❌ Erreur lors de la notification de modification: {}", e.getMessage());
            }
        }
    }

    private void notifyAllUsersOfShipArrivalDepartureDeletion(ShipArrivalDeparture movement) {
        List<Users> allUsers = usersRepository.findAll();

        String message = String.format(
            "Le mouvement de navire '%s' (Arrivée: %s) a été supprimé",
            movement.getCommercialShip() != null ? movement.getCommercialShip().getShipName() : "N/A",
            movement.getArrivalDate() != null ? movement.getArrivalDate().format(DATE_FORMATTER) : "N/A"
        );

        for (Users user : allUsers) {
            try {
                NotificationsRequest notificationRequest = new NotificationsRequest();
                notificationRequest.setMessage(message);
                notificationRequest.setNotificationType("ship_arrivals_departures");
                notificationRequest.setRecipientTrackingId(user.getTrackingId());

                notificationsService.create(notificationRequest);
            } catch (Exception e) {
                log.error("❌ Erreur lors de l'envoi de la notification de suppression: {}", e.getMessage());
            }
        }
    }
}
