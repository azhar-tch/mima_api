package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.NavalVesselStatus;
import com.helpysoft.mima_api.entity.NavalVesselType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NavalVesselRequest {
    private String vesselNumber;
    private NavalVesselType vesselType;
    private String vesselName;
    private String hullNumber;
    private Integer yearCommissioned;
    private LocalDate dateCommissioned;
    private LocalDate dateDecommissioned;
    private Double length;
    private Double width;
    private Double draft;
    private Double displacement;
    private Double maxSpeed;
    private Integer crewCapacity;
    private Double fuelCapacity;
    private Double range;
    private String armament;
    private String electronics;
    private String engineType;
    private Integer enginePower;
    private String homePort;
    private NavalVesselStatus operationalStatus;
    private String currentLocation;
    private String currentMission;
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;
    private Double totalOperationalHours;
    private String observations;
    private Boolean isActive;
}
