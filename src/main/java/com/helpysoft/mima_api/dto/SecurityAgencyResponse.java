package com.helpysoft.mima_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityAgencyResponse {
    private UUID trackingId;
    private String agencyNumber;
    private String agencyName;
    private String phoneNumber;
    private String phoneNumber2;
    private String email;
    private String address;
    private String city;
    private String country;
    private String contactPerson;
    private String contactPosition;
    private String registrationNumber;
    private String licenseNumber;
    private String website;
    private String observations;
    private Boolean isActive;
    private Integer totalEscortsRequested;
    private Integer totalArmedGuardsRequested;
    private LocalDateTime createDate;
}
