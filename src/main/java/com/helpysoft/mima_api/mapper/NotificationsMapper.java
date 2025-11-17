package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.dto.NotificationsResponse;
import com.helpysoft.mima_api.entity.Notifications;
import com.helpysoft.mima_api.entity.Users;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationsMapper {

    public Notifications toEntity(NotificationsRequest request, Users recipient) {
        Notifications notification = new Notifications();
        notification.setTrackingId(UUID.randomUUID());
        notification.setMessage(request.getMessage());
        notification.setNotificationType(request.getNotificationType());
        notification.setIsRead(request.getIsRead() != null ? request.getIsRead() : false);
        notification.setRecipient(recipient);
        return notification;
    }

    public NotificationsResponse toResponse(Notifications notification) {
        NotificationsResponse response = new NotificationsResponse();
        response.setTrackingId(notification.getTrackingId());
        response.setMessage(notification.getMessage());
        response.setNotificationType(notification.getNotificationType());
        response.setIsRead(notification.getIsRead());
        response.setRecipientName(notification.getRecipient() != null ?
            notification.getRecipient().getFirstName() + " " + notification.getRecipient().getLastName() : null);
        response.setCreateDate(notification.getCreateDate());
        return response;
    }
}
