package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.PALEntryExitRequest;
import com.helpysoft.mima_api.dto.PALEntryExitResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.PALEntryExit;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PALEntryExitMapper {

    public PALEntryExit toEntity(PALEntryExitRequest request, CommercialShips ship) {
        PALEntryExit pal = new PALEntryExit();
        pal.setTrackingId(UUID.randomUUID());
        pal.setCommercialShip(ship);

        // Données d'entrée
        pal.setEntryDate(request.getEntryDate());
        pal.setEntryReason(request.getEntryReason());
        pal.setAnchorageZone(request.getAnchorageZone());
        pal.setEntryAuthorizationNumber(request.getEntryAuthorizationNumber());
        pal.setAuthorizingAuthority(request.getAuthorizingAuthority());

        // Données de sortie (optionnel)
        pal.setExitDate(request.getExitDate());
        pal.setExitReason(request.getExitReason());
        pal.setExitAuthorizationNumber(request.getExitAuthorizationNumber());
        pal.setServicesProvided(request.getServicesProvided());

        pal.setIncidents(request.getIncidents());
        pal.setObservations(request.getObservations());

        return pal;
    }

    public PALEntryExitResponse toResponse(PALEntryExit pal) {
        PALEntryExitResponse response = new PALEntryExitResponse();
        response.setTrackingId(pal.getTrackingId().toString());
        response.setId(pal.getId());

        // Commercial Ship info
        if (pal.getCommercialShip() != null) {
            response.setCommercialShipTrackingId(pal.getCommercialShip().getTrackingId().toString());
            response.setShipName(pal.getCommercialShip().getShipName());
            response.setImoNumber(pal.getCommercialShip().getImoNumber());
        }

        // Données d'entrée
        response.setEntryDate(pal.getEntryDate());
        response.setEntryReason(pal.getEntryReason());
        response.setAnchorageZone(pal.getAnchorageZone());
        response.setEntryAuthorizationNumber(pal.getEntryAuthorizationNumber());
        response.setAuthorizingAuthority(pal.getAuthorizingAuthority());

        // Données de sortie
        response.setExitDate(pal.getExitDate());
        response.setExitReason(pal.getExitReason());
        response.setExitAuthorizationNumber(pal.getExitAuthorizationNumber());
        response.setStayDurationHours(pal.getStayDurationHours());
        response.setStayDurationDays(pal.getStayDurationDays());
        response.setServicesProvided(pal.getServicesProvided());

        response.setIncidents(pal.getIncidents());
        response.setObservations(pal.getObservations());
        response.setCreateDate(pal.getCreateDate());

        return response;
    }

    public void updateEntity(PALEntryExit pal, PALEntryExitRequest request, CommercialShips ship) {
        pal.setCommercialShip(ship);

        // Données d'entrée
        pal.setEntryDate(request.getEntryDate());
        pal.setEntryReason(request.getEntryReason());
        pal.setAnchorageZone(request.getAnchorageZone());
        pal.setEntryAuthorizationNumber(request.getEntryAuthorizationNumber());
        pal.setAuthorizingAuthority(request.getAuthorizingAuthority());

        // Données de sortie
        pal.setExitDate(request.getExitDate());
        pal.setExitReason(request.getExitReason());
        pal.setExitAuthorizationNumber(request.getExitAuthorizationNumber());
        pal.setServicesProvided(request.getServicesProvided());

        pal.setIncidents(request.getIncidents());
        pal.setObservations(request.getObservations());
    }
}
