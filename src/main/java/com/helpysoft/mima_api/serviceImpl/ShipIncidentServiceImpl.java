package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ShipIncidentRequest;
import com.helpysoft.mima_api.dto.ShipIncidentResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.ShipIncident;
import com.helpysoft.mima_api.mapper.ShipIncidentMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.NavalVesselRepository;
import com.helpysoft.mima_api.repository.ShipIncidentRepository;
import com.helpysoft.mima_api.service.ShipIncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipIncidentServiceImpl implements ShipIncidentService {

    private final ShipIncidentRepository shipIncidentRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final NavalVesselRepository navalVesselRepository;
    private final ShipIncidentMapper shipIncidentMapper;

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
        return shipIncidentMapper.toResponse(saved);
    }

    @Override
    public ShipIncidentResponse update(UUID trackingId, ShipIncidentRequest request) {
        ShipIncident incident = shipIncidentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship incident not found"));

        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        NavalVessels assistingVessel = null;
        if (request.getAssistingNavalVesselTrackingId() != null) {
            assistingVessel = navalVesselRepository.findByTrackingId(UUID.fromString(request.getAssistingNavalVesselTrackingId()))
                    .orElse(null);
        }

        shipIncidentMapper.updateEntity(incident, request, ship, assistingVessel);
        ShipIncident updated = shipIncidentRepository.save(incident);
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
        CommercialShips ship = commercialShipRepository.findByTrackingId(shipTrackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));
        return shipIncidentRepository.findByCommercialShipId(ship.getId())
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
    public void delete(UUID trackingId) {
        ShipIncident incident = shipIncidentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship incident not found"));
        shipIncidentRepository.delete(incident);
    }
}
