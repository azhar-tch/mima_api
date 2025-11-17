package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.ShipArrivalDepartureRequest;
import com.helpysoft.mima_api.dto.ShipArrivalDepartureResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ShipArrivalDeparture;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShipArrivalDepartureMapper {

    public ShipArrivalDeparture toEntity(ShipArrivalDepartureRequest request, CommercialShips ship) {
        ShipArrivalDeparture arrival = new ShipArrivalDeparture();
        arrival.setTrackingId(UUID.randomUUID());
        arrival.setCommercialShip(ship);

        // Données d'arrivée
        arrival.setArrivalDate(request.getArrivalDate());
        arrival.setPortOfOrigin(request.getPortOfOrigin());
        arrival.setCargoTypeArrival(request.getCargoTypeArrival());
        arrival.setCargoQuantityArrival(request.getCargoQuantityArrival());
        arrival.setPassengersArrival(request.getPassengersArrival());
        arrival.setCrewCount(request.getCrewCount());
        arrival.setCaptainName(request.getCaptainName());
        arrival.setShippingAgent(request.getShippingAgent());
        arrival.setBerthingPosition(request.getBerthingPosition());

        // Données de départ (optionnel)
        arrival.setDepartureDate(request.getDepartureDate());
        arrival.setPortOfDestination(request.getPortOfDestination());
        arrival.setCargoTypeDeparture(request.getCargoTypeDeparture());
        arrival.setCargoQuantityDeparture(request.getCargoQuantityDeparture());
        arrival.setPassengersDeparture(request.getPassengersDeparture());
        arrival.setServicesProvided(request.getServicesProvided());
        arrival.setPortDues(request.getPortDues());

        arrival.setIncidents(request.getIncidents());
        arrival.setObservations(request.getObservations());

        return arrival;
    }

    public ShipArrivalDepartureResponse toResponse(ShipArrivalDeparture arrival) {
        ShipArrivalDepartureResponse response = new ShipArrivalDepartureResponse();
        response.setTrackingId(arrival.getTrackingId().toString());
        response.setId(arrival.getId());

        // Commercial Ship info
        if (arrival.getCommercialShip() != null) {
            response.setCommercialShipTrackingId(arrival.getCommercialShip().getTrackingId().toString());
            response.setShipName(arrival.getCommercialShip().getShipName());
            response.setImoNumber(arrival.getCommercialShip().getImoNumber());
        }

        // Données d'arrivée
        response.setArrivalDate(arrival.getArrivalDate());
        response.setPortOfOrigin(arrival.getPortOfOrigin());
        response.setCargoTypeArrival(arrival.getCargoTypeArrival());
        response.setCargoQuantityArrival(arrival.getCargoQuantityArrival());
        response.setPassengersArrival(arrival.getPassengersArrival());
        response.setCrewCount(arrival.getCrewCount());
        response.setCaptainName(arrival.getCaptainName());
        response.setShippingAgent(arrival.getShippingAgent());
        response.setBerthingPosition(arrival.getBerthingPosition());

        // Données de départ
        response.setDepartureDate(arrival.getDepartureDate());
        response.setPortOfDestination(arrival.getPortOfDestination());
        response.setCargoTypeDeparture(arrival.getCargoTypeDeparture());
        response.setCargoQuantityDeparture(arrival.getCargoQuantityDeparture());
        response.setPassengersDeparture(arrival.getPassengersDeparture());
        response.setStayDurationHours(arrival.getStayDurationHours());
        response.setStayDurationDays(arrival.getStayDurationDays());
        response.setServicesProvided(arrival.getServicesProvided());
        response.setPortDues(arrival.getPortDues());

        response.setIncidents(arrival.getIncidents());
        response.setObservations(arrival.getObservations());
        response.setCreateDate(arrival.getCreateDate());

        return response;
    }

    public void updateEntity(ShipArrivalDeparture arrival, ShipArrivalDepartureRequest request, CommercialShips ship) {
        arrival.setCommercialShip(ship);

        // Données d'arrivée
        arrival.setArrivalDate(request.getArrivalDate());
        arrival.setPortOfOrigin(request.getPortOfOrigin());
        arrival.setCargoTypeArrival(request.getCargoTypeArrival());
        arrival.setCargoQuantityArrival(request.getCargoQuantityArrival());
        arrival.setPassengersArrival(request.getPassengersArrival());
        arrival.setCrewCount(request.getCrewCount());
        arrival.setCaptainName(request.getCaptainName());
        arrival.setShippingAgent(request.getShippingAgent());
        arrival.setBerthingPosition(request.getBerthingPosition());

        // Données de départ (optionnel)
        arrival.setDepartureDate(request.getDepartureDate());
        arrival.setPortOfDestination(request.getPortOfDestination());
        arrival.setCargoTypeDeparture(request.getCargoTypeDeparture());
        arrival.setCargoQuantityDeparture(request.getCargoQuantityDeparture());
        arrival.setPassengersDeparture(request.getPassengersDeparture());
        arrival.setServicesProvided(request.getServicesProvided());
        arrival.setPortDues(request.getPortDues());

        arrival.setIncidents(request.getIncidents());
        arrival.setObservations(request.getObservations());
    }
}
