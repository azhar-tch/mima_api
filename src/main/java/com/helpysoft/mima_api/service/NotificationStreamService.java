package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.NotificationsResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface NotificationStreamService {
    SseEmitter subscribe(UUID userTrackingId);
    void sendNotificationToUser(UUID userTrackingId, NotificationsResponse notification);
    void removeEmitter(UUID userTrackingId);
}
