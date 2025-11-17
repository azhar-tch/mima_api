package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.ConservatorSeizureRequest;
import com.helpysoft.mima_api.dto.ConservatorSeizureResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ConservatorSeizure;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConservatorSeizureMapper {

    public ConservatorSeizure toEntity(ConservatorSeizureRequest request, CommercialShips ship) {
        ConservatorSeizure seizure = new ConservatorSeizure();
        seizure.setTrackingId(UUID.randomUUID());
        seizure.setCommercialShip(ship);

        // Données de saisie
        seizure.setSeizureDate(request.getSeizureDate());
        seizure.setSeizingAuthority(request.getSeizingAuthority());
        seizure.setSeizureOrderNumber(request.getSeizureOrderNumber());
        seizure.setSeizureReason(request.getSeizureReason());
        seizure.setSeizureType(request.getSeizureType());
        seizure.setClaimAmount(request.getClaimAmount());
        seizure.setSeizureLocation(request.getSeizureLocation());
        seizure.setCreditorName(request.getCreditorName());
        seizure.setCreditorLegalRepresentative(request.getCreditorLegalRepresentative());
        seizure.setBailiffName(request.getBailiffName());
        seizure.setShipGuardian(request.getShipGuardian());

        // Données de levée (optionnel)
        seizure.setReleaseDate(request.getReleaseDate());
        seizure.setReleaseReason(request.getReleaseReason());
        seizure.setReleaseOrderNumber(request.getReleaseOrderNumber());
        seizure.setAmountPaid(request.getAmountPaid());
        seizure.setRelatedDocuments(request.getRelatedDocuments());

        seizure.setObservations(request.getObservations());

        return seizure;
    }

    public ConservatorSeizureResponse toResponse(ConservatorSeizure seizure) {
        ConservatorSeizureResponse response = new ConservatorSeizureResponse();
        response.setTrackingId(seizure.getTrackingId().toString());
        response.setId(seizure.getId());

        // Commercial Ship info
        if (seizure.getCommercialShip() != null) {
            response.setCommercialShipTrackingId(seizure.getCommercialShip().getTrackingId().toString());
            response.setShipName(seizure.getCommercialShip().getShipName());
            response.setImoNumber(seizure.getCommercialShip().getImoNumber());
        }

        // Données de saisie
        response.setSeizureDate(seizure.getSeizureDate());
        response.setSeizingAuthority(seizure.getSeizingAuthority());
        response.setSeizureOrderNumber(seizure.getSeizureOrderNumber());
        response.setSeizureReason(seizure.getSeizureReason());
        response.setSeizureType(seizure.getSeizureType());
        response.setClaimAmount(seizure.getClaimAmount());
        response.setSeizureLocation(seizure.getSeizureLocation());
        response.setCreditorName(seizure.getCreditorName());
        response.setCreditorLegalRepresentative(seizure.getCreditorLegalRepresentative());
        response.setBailiffName(seizure.getBailiffName());
        response.setShipGuardian(seizure.getShipGuardian());

        // Données de levée
        response.setReleaseDate(seizure.getReleaseDate());
        response.setReleaseReason(seizure.getReleaseReason());
        response.setReleaseOrderNumber(seizure.getReleaseOrderNumber());
        response.setAmountPaid(seizure.getAmountPaid());
        response.setSeizureDurationHours(seizure.getSeizureDurationHours());
        response.setSeizureDurationDays(seizure.getSeizureDurationDays());
        response.setStatus(seizure.getStatus());
        response.setRelatedDocuments(seizure.getRelatedDocuments());

        response.setObservations(seizure.getObservations());
        response.setCreateDate(seizure.getCreateDate());

        return response;
    }

    public void updateEntity(ConservatorSeizure seizure, ConservatorSeizureRequest request, CommercialShips ship) {
        seizure.setCommercialShip(ship);

        // Données de saisie
        seizure.setSeizureDate(request.getSeizureDate());
        seizure.setSeizingAuthority(request.getSeizingAuthority());
        seizure.setSeizureOrderNumber(request.getSeizureOrderNumber());
        seizure.setSeizureReason(request.getSeizureReason());
        seizure.setSeizureType(request.getSeizureType());
        seizure.setClaimAmount(request.getClaimAmount());
        seizure.setSeizureLocation(request.getSeizureLocation());
        seizure.setCreditorName(request.getCreditorName());
        seizure.setCreditorLegalRepresentative(request.getCreditorLegalRepresentative());
        seizure.setBailiffName(request.getBailiffName());
        seizure.setShipGuardian(request.getShipGuardian());

        // Données de levée
        seizure.setReleaseDate(request.getReleaseDate());
        seizure.setReleaseReason(request.getReleaseReason());
        seizure.setReleaseOrderNumber(request.getReleaseOrderNumber());
        seizure.setAmountPaid(request.getAmountPaid());
        seizure.setRelatedDocuments(request.getRelatedDocuments());

        seizure.setObservations(request.getObservations());
    }
}
