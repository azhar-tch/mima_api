package com.helpysoft.mima_api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationsRequest {
    private String message;
    private String notificationType;
    private Boolean isRead;
    private UUID recipientTrackingId;
}
