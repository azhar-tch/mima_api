package com.helpysoft.mima_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpysoft.mima_api.entity.AgentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentsResponse {
    private UUID trackingId;
    private String firstName;
    private String lastName;
    private String registrationNo;
    private String rank;
    private String unitName;
    private UUID unitTrackingId;
    private Boolean availability;
    private AgentStatus status;
    private LocalDateTime createDate;

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
