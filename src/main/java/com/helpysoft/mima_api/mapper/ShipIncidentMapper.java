package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.ShipIncidentRequest;
import com.helpysoft.mima_api.dto.ShipIncidentResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.ShipIncident;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShipIncidentMapper {

    public ShipIncident toEntity(ShipIncidentRequest request, CommercialShips ship, NavalVessels assistingVessel) {
        ShipIncident incident = new ShipIncident();
        incident.setTrackingId(UUID.randomUUID());
        incident.setCommercialShip(ship);

        incident.setIncidentDate(request.getIncidentDate());
        incident.setEventType(request.getEventType());
        incident.setIncidentType(request.getIncidentType());
        incident.setSeverity(request.getSeverity());
        incident.setLocation(request.getLocation());
        incident.setLatitude(request.getLatitude());
        incident.setLongitude(request.getLongitude());
        incident.setMaritimeZone(request.getMaritimeZone());
        incident.setDescription(request.getDescription());
        incident.setCauses(request.getCauses());
        incident.setCasualties(request.getCasualties());
        incident.setMaterialDamage(request.getMaterialDamage());

        // Pollution
        incident.setPollutionOccurred(request.getPollutionOccurred());
        incident.setPollutionType(request.getPollutionType());

        incident.setRespondingAgencies(request.getRespondingAgencies());
        incident.setAssistingNavalVessel(assistingVessel);
        incident.setImmediateMeasures(request.getImmediateMeasures());

        // Résolution
        incident.setResolutionDate(request.getResolutionDate());
        incident.setStatus(request.getStatus() != null ? request.getStatus() : "EN_COURS");
        incident.setReportEstablished(request.getReportEstablished());
        incident.setReportReference(request.getReportReference());
        incident.setNotifiedAuthorities(request.getNotifiedAuthorities());
        incident.setInvestigationOngoing(request.getInvestigationOngoing());
        incident.setRecommendations(request.getRecommendations());

        incident.setObservations(request.getObservations());

        return incident;
    }

    public ShipIncidentResponse toResponse(ShipIncident incident) {
        ShipIncidentResponse response = new ShipIncidentResponse();
        response.setTrackingId(incident.getTrackingId().toString());
        response.setId(incident.getId());

        // Commercial Ship info
        if (incident.getCommercialShip() != null) {
            response.setCommercialShipTrackingId(incident.getCommercialShip().getTrackingId().toString());
            response.setShipName(incident.getCommercialShip().getShipName());
            response.setImoNumber(incident.getCommercialShip().getImoNumber());
        }

        response.setIncidentDate(incident.getIncidentDate());
        response.setEventType(incident.getEventType());
        response.setIncidentType(incident.getIncidentType());
        response.setSeverity(incident.getSeverity());
        response.setLocation(incident.getLocation());
        response.setLatitude(incident.getLatitude());
        response.setLongitude(incident.getLongitude());
        response.setMaritimeZone(incident.getMaritimeZone());
        response.setDescription(incident.getDescription());
        response.setCauses(incident.getCauses());
        response.setCasualties(incident.getCasualties());
        response.setMaterialDamage(incident.getMaterialDamage());

        // Pollution
        response.setPollutionOccurred(incident.getPollutionOccurred());
        response.setPollutionType(incident.getPollutionType());

        response.setRespondingAgencies(incident.getRespondingAgencies());

        // Assisting Naval Vessel
        if (incident.getAssistingNavalVessel() != null) {
            response.setAssistingNavalVesselTrackingId(incident.getAssistingNavalVessel().getTrackingId().toString());
            response.setAssistingVesselName(incident.getAssistingNavalVessel().getVesselName());
        }

        response.setImmediateMeasures(incident.getImmediateMeasures());

        // Résolution
        response.setResolutionDate(incident.getResolutionDate());
        response.setResolutionDurationHours(incident.getResolutionDurationHours());
        response.setStatus(incident.getStatus());
        response.setIsResolved(incident.isResolved());
        response.setReportEstablished(incident.getReportEstablished());
        response.setReportReference(incident.getReportReference());
        response.setNotifiedAuthorities(incident.getNotifiedAuthorities());
        response.setInvestigationOngoing(incident.getInvestigationOngoing());
        response.setRecommendations(incident.getRecommendations());

        response.setObservations(incident.getObservations());
        response.setCreateDate(incident.getCreateDate());

        return response;
    }

    public void updateEntity(ShipIncident incident, ShipIncidentRequest request, CommercialShips ship, NavalVessels assistingVessel) {
        incident.setCommercialShip(ship);

        incident.setIncidentDate(request.getIncidentDate());
        incident.setEventType(request.getEventType());
        incident.setIncidentType(request.getIncidentType());
        incident.setSeverity(request.getSeverity());
        incident.setLocation(request.getLocation());
        incident.setLatitude(request.getLatitude());
        incident.setLongitude(request.getLongitude());
        incident.setMaritimeZone(request.getMaritimeZone());
        incident.setDescription(request.getDescription());
        incident.setCauses(request.getCauses());
        incident.setCasualties(request.getCasualties());
        incident.setMaterialDamage(request.getMaterialDamage());

        // Pollution
        incident.setPollutionOccurred(request.getPollutionOccurred());
        incident.setPollutionType(request.getPollutionType());

        incident.setRespondingAgencies(request.getRespondingAgencies());
        incident.setAssistingNavalVessel(assistingVessel);
        incident.setImmediateMeasures(request.getImmediateMeasures());

        // Résolution
        incident.setResolutionDate(request.getResolutionDate());
        incident.setStatus(request.getStatus() != null ? request.getStatus() : "EN_COURS");
        incident.setReportEstablished(request.getReportEstablished());
        incident.setReportReference(request.getReportReference());
        incident.setNotifiedAuthorities(request.getNotifiedAuthorities());
        incident.setInvestigationOngoing(request.getInvestigationOngoing());
        incident.setRecommendations(request.getRecommendations());

        incident.setObservations(request.getObservations());
    }
}
