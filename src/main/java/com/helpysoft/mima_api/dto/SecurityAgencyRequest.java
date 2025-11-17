package com.helpysoft.mima_api.dto;

import lombok.Data;

@Data
public class SecurityAgencyRequest {
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
}
