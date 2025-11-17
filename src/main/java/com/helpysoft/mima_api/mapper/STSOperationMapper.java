package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.STSOperationRequest;
import com.helpysoft.mima_api.dto.STSOperationResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.STSOperation;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class STSOperationMapper {

    public STSOperation toEntity(STSOperationRequest request, CommercialShips motherVessel,
                                  CommercialShips receivingVessel, NavalVessels supervisingVessel) {
        STSOperation sts = new STSOperation();
        sts.setTrackingId(UUID.randomUUID());
        sts.setMotherVessel(motherVessel);
        sts.setReceivingVessel(receivingVessel);

        sts.setStartDate(request.getStartDate());
        sts.setEndDate(request.getEndDate());
        sts.setCargoType(request.getCargoType());
        sts.setQuantityTransferred(request.getQuantityTransferred());
        sts.setUnit(request.getUnit());
        sts.setLocation(request.getLocation());
        sts.setLatitude(request.getLatitude());
        sts.setLongitude(request.getLongitude());
        sts.setMaritimeZone(request.getMaritimeZone());

        // Conditions
        sts.setWeatherConditions(request.getWeatherConditions());
        sts.setSeaState(request.getSeaState());

        // Autorisation
        sts.setStsOperator(request.getStsOperator());
        sts.setAuthorizationNumber(request.getAuthorizationNumber());
        sts.setAuthorizingAuthority(request.getAuthorizingAuthority());

        // Supervision
        sts.setSupervisingNavalVessel(supervisingVessel);
        sts.setSurveyCompany(request.getSurveyCompany());
        sts.setEmergencyPlanEstablished(request.getEmergencyPlanEstablished());
        sts.setPollutionPreventionEquipment(request.getPollutionPreventionEquipment());

        // Incidents
        sts.setIncidents(request.getIncidents());
        sts.setPollutionOccurred(request.getPollutionOccurred());
        sts.setPollutionType(request.getPollutionType());
        sts.setIncidentMeasures(request.getIncidentMeasures());

        sts.setStatus(request.getStatus() != null ? request.getStatus() : "PLANIFIEE");
        sts.setReportEstablished(request.getReportEstablished());
        sts.setReportReference(request.getReportReference());
        sts.setCompliantWithStandards(request.getCompliantWithStandards());

        sts.setObservations(request.getObservations());

        return sts;
    }

    public STSOperationResponse toResponse(STSOperation sts) {
        STSOperationResponse response = new STSOperationResponse();
        response.setTrackingId(sts.getTrackingId().toString());
        response.setId(sts.getId());
        response.setOperationNumber(sts.getOperationNumber());

        // Mother Vessel
        if (sts.getMotherVessel() != null) {
            response.setMotherVesselTrackingId(sts.getMotherVessel().getTrackingId().toString());
            response.setMotherVesselName(sts.getMotherVessel().getShipName());
            response.setMotherVesselImo(sts.getMotherVessel().getImoNumber());
        }

        // Receiving Vessel
        if (sts.getReceivingVessel() != null) {
            response.setReceivingVesselTrackingId(sts.getReceivingVessel().getTrackingId().toString());
            response.setReceivingVesselName(sts.getReceivingVessel().getShipName());
            response.setReceivingVesselImo(sts.getReceivingVessel().getImoNumber());
        }

        response.setStartDate(sts.getStartDate());
        response.setEndDate(sts.getEndDate());
        response.setOperationDurationHours(sts.getOperationDurationHours());
        response.setCargoType(sts.getCargoType());
        response.setQuantityTransferred(sts.getQuantityTransferred());
        response.setUnit(sts.getUnit());
        response.setLocation(sts.getLocation());
        response.setLatitude(sts.getLatitude());
        response.setLongitude(sts.getLongitude());
        response.setMaritimeZone(sts.getMaritimeZone());

        // Conditions
        response.setWeatherConditions(sts.getWeatherConditions());
        response.setSeaState(sts.getSeaState());

        // Autorisation
        response.setStsOperator(sts.getStsOperator());
        response.setAuthorizationNumber(sts.getAuthorizationNumber());
        response.setAuthorizingAuthority(sts.getAuthorizingAuthority());

        // Supervision
        if (sts.getSupervisingNavalVessel() != null) {
            response.setSupervisingNavalVesselTrackingId(sts.getSupervisingNavalVessel().getTrackingId().toString());
            response.setSupervisingVesselName(sts.getSupervisingNavalVessel().getVesselName());
        }
        response.setSurveyCompany(sts.getSurveyCompany());
        response.setEmergencyPlanEstablished(sts.getEmergencyPlanEstablished());
        response.setPollutionPreventionEquipment(sts.getPollutionPreventionEquipment());

        // Incidents
        response.setIncidents(sts.getIncidents());
        response.setPollutionOccurred(sts.getPollutionOccurred());
        response.setPollutionType(sts.getPollutionType());
        response.setIncidentMeasures(sts.getIncidentMeasures());

        response.setStatus(sts.getStatus());
        response.setIsCompleted(sts.isCompleted());
        response.setReportEstablished(sts.getReportEstablished());
        response.setReportReference(sts.getReportReference());
        response.setCompliantWithStandards(sts.getCompliantWithStandards());

        response.setObservations(sts.getObservations());
        response.setCreateDate(sts.getCreateDate());

        return response;
    }

    public void updateEntity(STSOperation sts, STSOperationRequest request, CommercialShips motherVessel,
                              CommercialShips receivingVessel, NavalVessels supervisingVessel) {
        sts.setMotherVessel(motherVessel);
        sts.setReceivingVessel(receivingVessel);

        sts.setStartDate(request.getStartDate());
        sts.setEndDate(request.getEndDate());
        sts.setCargoType(request.getCargoType());
        sts.setQuantityTransferred(request.getQuantityTransferred());
        sts.setUnit(request.getUnit());
        sts.setLocation(request.getLocation());
        sts.setLatitude(request.getLatitude());
        sts.setLongitude(request.getLongitude());
        sts.setMaritimeZone(request.getMaritimeZone());

        // Conditions
        sts.setWeatherConditions(request.getWeatherConditions());
        sts.setSeaState(request.getSeaState());

        // Autorisation
        sts.setStsOperator(request.getStsOperator());
        sts.setAuthorizationNumber(request.getAuthorizationNumber());
        sts.setAuthorizingAuthority(request.getAuthorizingAuthority());

        // Supervision
        sts.setSupervisingNavalVessel(supervisingVessel);
        sts.setSurveyCompany(request.getSurveyCompany());
        sts.setEmergencyPlanEstablished(request.getEmergencyPlanEstablished());
        sts.setPollutionPreventionEquipment(request.getPollutionPreventionEquipment());

        // Incidents
        sts.setIncidents(request.getIncidents());
        sts.setPollutionOccurred(request.getPollutionOccurred());
        sts.setPollutionType(request.getPollutionType());
        sts.setIncidentMeasures(request.getIncidentMeasures());

        sts.setStatus(request.getStatus() != null ? request.getStatus() : "PLANIFIEE");
        sts.setReportEstablished(request.getReportEstablished());
        sts.setReportReference(request.getReportReference());
        sts.setCompliantWithStandards(request.getCompliantWithStandards());

        sts.setObservations(request.getObservations());
    }
}
