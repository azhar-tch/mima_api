package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.AgentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AgentsRequest {
    private String registrationNo;
    private String firstName;
    private String lastName;
    private String rank;
    private UUID unitTrackingId;
    private Boolean availability;
    private AgentStatus status;

    private String nationality;
    private String city;
    private String emergencyContact;
    private String maritalStatus;
    private LocalDate recruitmentDate;
    private LocalDate contractEndDate;
    private String idCardNumber;
    private String passportNumber;
    private LocalDate idExpiryDate;
    private String insuranceNumber;
    private String bankAccount;
}
