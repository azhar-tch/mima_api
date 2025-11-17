package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.dto.NotificationsResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationsService {
    NotificationsResponse create(NotificationsRequest request);
    NotificationsResponse update(UUID trackingId, NotificationsRequest request);
    NotificationsResponse findByTrackingId(UUID trackingId);
    NotificationsResponse markAsRead(UUID trackingId);
    List<NotificationsResponse> findByRecipient(UUID recipientTrackingId);
    List<NotificationsResponse> findByIsRead(Boolean isRead);
    List<NotificationsResponse> findAll();
    void delete(UUID trackingId);
}
