package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.ShipProvisioningRequest;
import com.helpysoft.mima_api.dto.ShipProvisioningResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ShipProvisioning;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShipProvisioningMapper {

    public ShipProvisioning toEntity(ShipProvisioningRequest request, CommercialShips ship) {
        ShipProvisioning provisioning = new ShipProvisioning();
        provisioning.setTrackingId(UUID.randomUUID());
        provisioning.setCommercialShip(ship);

        provisioning.setProvisioningDate(request.getProvisioningDate());
        provisioning.setProvisioningType(request.getProvisioningType());
        provisioning.setSupplierName(request.getSupplierName());
        provisioning.setSupplyVesselName(request.getSupplyVesselName());
        provisioning.setSupplyVesselImo(request.getSupplyVesselImo());
        provisioning.setProductType(request.getProductType());
        provisioning.setQuantity(request.getQuantity());
        provisioning.setUnit(request.getUnit());
        provisioning.setAmount(request.getAmount());
        provisioning.setStartTime(request.getStartTime());
        provisioning.setEndTime(request.getEndTime());
        provisioning.setProvisioningPoint(request.getProvisioningPoint());

        // Retard
        provisioning.setHasDelay(request.getHasDelay());
        provisioning.setDelayDurationHours(request.getDelayDurationHours());
        provisioning.setDelayReason(request.getDelayReason());
        provisioning.setDelayPenalty(request.getDelayPenalty());
        provisioning.setCorrectiveActions(request.getCorrectiveActions());

        provisioning.setIncidents(request.getIncidents());
        provisioning.setObservations(request.getObservations());

        return provisioning;
    }

    public ShipProvisioningResponse toResponse(ShipProvisioning provisioning) {
        ShipProvisioningResponse response = new ShipProvisioningResponse();
        response.setTrackingId(provisioning.getTrackingId().toString());
        response.setId(provisioning.getId());

        // Commercial Ship info
        if (provisioning.getCommercialShip() != null) {
            response.setCommercialShipTrackingId(provisioning.getCommercialShip().getTrackingId().toString());
            response.setShipName(provisioning.getCommercialShip().getShipName());
            response.setImoNumber(provisioning.getCommercialShip().getImoNumber());
        }

        response.setProvisioningDate(provisioning.getProvisioningDate());
        response.setProvisioningType(provisioning.getProvisioningType());
        response.setSupplierName(provisioning.getSupplierName());
        response.setSupplyVesselName(provisioning.getSupplyVesselName());
        response.setSupplyVesselImo(provisioning.getSupplyVesselImo());
        response.setProductType(provisioning.getProductType());
        response.setQuantity(provisioning.getQuantity());
        response.setUnit(provisioning.getUnit());
        response.setAmount(provisioning.getAmount());
        response.setStartTime(provisioning.getStartTime());
        response.setEndTime(provisioning.getEndTime());
        response.setOperationDurationHours(provisioning.getOperationDurationHours());
        response.setProvisioningPoint(provisioning.getProvisioningPoint());

        // Retard
        response.setHasDelay(provisioning.getHasDelay());
        response.setDelayDurationHours(provisioning.getDelayDurationHours());
        response.setDelayReason(provisioning.getDelayReason());
        response.setDelayPenalty(provisioning.getDelayPenalty());
        response.setCorrectiveActions(provisioning.getCorrectiveActions());
        response.setIsOnTime(provisioning.isOnTime());

        response.setIncidents(provisioning.getIncidents());
        response.setObservations(provisioning.getObservations());
        response.setCreateDate(provisioning.getCreateDate());

        return response;
    }

    public void updateEntity(ShipProvisioning provisioning, ShipProvisioningRequest request, CommercialShips ship) {
        provisioning.setCommercialShip(ship);

        provisioning.setProvisioningDate(request.getProvisioningDate());
        provisioning.setProvisioningType(request.getProvisioningType());
        provisioning.setSupplierName(request.getSupplierName());
        provisioning.setSupplyVesselName(request.getSupplyVesselName());
        provisioning.setSupplyVesselImo(request.getSupplyVesselImo());
        provisioning.setProductType(request.getProductType());
        provisioning.setQuantity(request.getQuantity());
        provisioning.setUnit(request.getUnit());
        provisioning.setAmount(request.getAmount());
        provisioning.setStartTime(request.getStartTime());
        provisioning.setEndTime(request.getEndTime());
        provisioning.setProvisioningPoint(request.getProvisioningPoint());

        // Retard
        provisioning.setHasDelay(request.getHasDelay());
        provisioning.setDelayDurationHours(request.getDelayDurationHours());
        provisioning.setDelayReason(request.getDelayReason());
        provisioning.setDelayPenalty(request.getDelayPenalty());
        provisioning.setCorrectiveActions(request.getCorrectiveActions());

        provisioning.setIncidents(request.getIncidents());
        provisioning.setObservations(request.getObservations());
    }
}
