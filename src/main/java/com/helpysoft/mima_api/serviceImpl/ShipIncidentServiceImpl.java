package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ShipIncidentRequest;
import com.helpysoft.mima_api.dto.ShipIncidentResponse;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.ShipIncident;
import com.helpysoft.mima_api.mapper.ShipIncidentMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.NavalVesselRepository;
import com.helpysoft.mima_api.repository.ShipIncidentRepository;
import com.helpysoft.mima_api.service.ShipIncidentService;
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
public class ShipIncidentServiceImpl implements ShipIncidentService {

    private final ShipIncidentRepository shipIncidentRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final NavalVesselRepository navalVesselRepository;
    private final ShipIncidentMapper shipIncidentMapper;
    private final HistoriesServiceImpl historiesService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public ShipIncidentResponse create(ShipIncidentRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        NavalVessels assistingVessel = null;
        if (request.getAssistingNavalVesselTrackingId() != null) {
            assistingVessel = navalVesselRepository.findByTrackingId(UUID.fromString(request.getAssistingNavalVesselTrackingId()))
                    .orElse(null);
        }

        ShipIncident incident = shipIncidentMapper.toEntity(request, ship, assistingVessel);
        ShipIncident saved = shipIncidentRepository.save(incident);

        // Enregistrer dans l'historique
        try {
            String summary = String.format(
                "Création incident maritime - Navire: %s - Type: %s - Gravité: %s - Date: %s - Zone: %s",
                ship.getShipName(),
                saved.getEventType(),
                saved.getSeverity(),
                saved.getIncidentDate() != null ? saved.getIncidentDate().format(DATE_FORMATTER) : "N/A",
                saved.getMaritimeZone()
            );

            historiesService.recordHistory(
                null,
                "SHIP_INCIDENT",
                saved.getTrackingId(),
                ActionType.CREATE,
                summary,
                null,
                saved
            );
            log.info("Historique enregistre pour l'incident du navire {}", ship.getShipName());
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }

        return shipIncidentMapper.toResponse(saved);
    }

    @Override
    public ShipIncidentResponse update(UUID trackingId, ShipIncidentRequest request) {
        ShipIncident incident = shipIncidentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship incident not found"));

        // Sauvegarder les anciennes valeurs pour l'historique
        String oldShipName = incident.getCommercialShip().getShipName();
        String oldEventType = incident.getEventType();
        String oldSeverity = incident.getSeverity();
        String oldStatus = incident.getStatus();
        LocalDateTime oldIncidentDate = incident.getIncidentDate();
        String oldMaritimeZone = incident.getMaritimeZone();

        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        NavalVessels assistingVessel = null;
        if (request.getAssistingNavalVesselTrackingId() != null) {
            assistingVessel = navalVesselRepository.findByTrackingId(UUID.fromString(request.getAssistingNavalVesselTrackingId()))
                    .orElse(null);
        }

        shipIncidentMapper.updateEntity(incident, request, ship, assistingVessel);
        ShipIncident updated = shipIncidentRepository.save(incident);

        // Détection des changements et enregistrement dans l'historique
        StringBuilder changes = new StringBuilder();
        boolean hasChanges = false;

        if (!oldShipName.equals(ship.getShipName())) {
            changes.append(String.format("Navire: '%s' → '%s' | ", oldShipName, ship.getShipName()));
            hasChanges = true;
        }

        if (!Objects.equals(oldEventType, updated.getEventType())) {
            changes.append(String.format("Type: '%s' → '%s' | ",
                oldEventType != null ? oldEventType : "N/A",
                updated.getEventType() != null ? updated.getEventType() : "N/A"));
            hasChanges = true;
        }

        if (!Objects.equals(oldSeverity, updated.getSeverity())) {
            changes.append(String.format("Gravité: '%s' → '%s' | ",
                oldSeverity != null ? oldSeverity : "N/A",
                updated.getSeverity() != null ? updated.getSeverity() : "N/A"));
            hasChanges = true;
        }

        if (!Objects.equals(oldStatus, updated.getStatus())) {
            changes.append(String.format("Statut: '%s' → '%s' | ",
                oldStatus != null ? oldStatus : "N/A",
                updated.getStatus() != null ? updated.getStatus() : "N/A"));
            hasChanges = true;
        }

        if (!Objects.equals(oldIncidentDate, updated.getIncidentDate())) {
            changes.append(String.format("Date: '%s' → '%s' | ",
                oldIncidentDate != null ? oldIncidentDate.format(DATE_FORMATTER) : "N/A",
                updated.getIncidentDate() != null ? updated.getIncidentDate().format(DATE_FORMATTER) : "N/A"));
            hasChanges = true;
        }

        if (!Objects.equals(oldMaritimeZone, updated.getMaritimeZone())) {
            changes.append(String.format("Zone: '%s' → '%s' | ",
                oldMaritimeZone != null ? oldMaritimeZone : "N/A",
                updated.getMaritimeZone() != null ? updated.getMaritimeZone() : "N/A"));
            hasChanges = true;
        }

        if (hasChanges) {
            try {
                String summary = "Modification incident maritime - Navire: " + ship.getShipName() + " - " +
                    changes.substring(0, changes.length() - 3);

                historiesService.recordHistory(
                    null,
                    "SHIP_INCIDENT",
                    updated.getTrackingId(),
                    ActionType.UPDATE,
                    summary,
                    null,
                    updated
                );
                log.info("Historique de modification enregistre pour l'incident du navire {}", ship.getShipName());
            } catch (Exception e) {
                log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
            }
        }

        return shipIncidentMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipIncidentResponse findByTrackingId(UUID trackingId) {
        ShipIncident incident = shipIncidentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship incident not found"));
        return shipIncidentMapper.toResponse(incident);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findByCommercialShip(UUID shipTrackingId) {
        return shipIncidentRepository.findByCommercialShipTrackingId(shipTrackingId)
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findOngoingIncidents() {
        return shipIncidentRepository.findOngoingIncidents()
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findByStatus(String status) {
        return shipIncidentRepository.findByStatus(status)
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findByIncidentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return shipIncidentRepository.findByIncidentDateBetween(startDate, endDate)
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findByEventType(String eventType) {
        return shipIncidentRepository.findByEventType(eventType)
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findBySeverity(String severity) {
        return shipIncidentRepository.findBySeverity(severity)
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findIncidentsWithPollution() {
        return shipIncidentRepository.findIncidentsWithPollution()
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findByMaritimeZone(String maritimeZone) {
        return shipIncidentRepository.findByMaritimeZone(maritimeZone)
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> findAll() {
        return shipIncidentRepository.findAll()
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipIncidentResponse> searchShipIncidents(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return shipIncidentRepository.searchShipIncidents(searchTerm)
                .stream()
                .map(shipIncidentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ShipIncident incident = shipIncidentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship incident not found"));

        // Sauvegarder les informations avant suppression
        String shipName = incident.getCommercialShip().getShipName();
        String eventType = incident.getEventType();
        LocalDateTime incidentDate = incident.getIncidentDate();
        String severity = incident.getSeverity();

        shipIncidentRepository.delete(incident);

        // Enregistrer dans l'historique après suppression
        try {
            String summary = String.format(
                "Suppression incident maritime - Navire: %s - Type: %s - Gravité: %s - Date: %s",
                shipName,
                eventType,
                severity,
                incidentDate != null ? incidentDate.format(DATE_FORMATTER) : "N/A"
            );

            historiesService.recordHistory(
                null,
                "SHIP_INCIDENT",
                trackingId,
                ActionType.DELETE,
                summary,
                null,
                null
            );
            log.info("Historique de suppression enregistre pour l'incident du navire {}", shipName);
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'historique: {}", e.getMessage());
        }
    }
}
