package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UsersRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String fcmToken;
    private UUID ruleTrackingId;
    private Boolean isActive;
}
