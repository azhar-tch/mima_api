package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.CommercialShipRequest;
import com.helpysoft.mima_api.dto.CommercialShipResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ShipStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommercialShipMapper {

    public CommercialShips toEntity(CommercialShipRequest request) {
        CommercialShips ship = new CommercialShips();
        ship.setTrackingId(UUID.randomUUID());
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
        ship.setStatus(request.getStatus() != null ? request.getStatus() : ShipStatus.IN_PORT);
        ship.setObservations(request.getObservations());
        ship.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        return ship;
    }

    public CommercialShipResponse toResponse(CommercialShips ship) {
        CommercialShipResponse response = new CommercialShipResponse();
        response.setTrackingId(ship.getTrackingId());
        response.setImoNumber(ship.getImoNumber());
        response.setShipName(ship.getShipName());
        response.setShipType(ship.getShipType());
        response.setFlag(ship.getFlag());
        response.setMmsi(ship.getMmsi());
        response.setCallSign(ship.getCallSign());
        response.setGrossTonnage(ship.getGrossTonnage());
        response.setDeadWeight(ship.getDeadWeight());
        response.setLength(ship.getLength());
        response.setWidth(ship.getWidth());
        response.setDraft(ship.getDraft());
        response.setYearBuilt(ship.getYearBuilt());
        response.setShipOwner(ship.getShipOwner());
        response.setOperator(ship.getOperator());
        response.setLastPort(ship.getLastPort());
        response.setNextPort(ship.getNextPort());
        response.setCargoType(ship.getCargoType());
        response.setArrivalDate(ship.getArrivalDate());
        response.setDepartureDate(ship.getDepartureDate());
        response.setStatus(ship.getStatus());
        response.setObservations(ship.getObservations());
        response.setIsActive(ship.getIsActive());
        response.setCreateDate(ship.getCreateDate());
        return response;
    }
}
