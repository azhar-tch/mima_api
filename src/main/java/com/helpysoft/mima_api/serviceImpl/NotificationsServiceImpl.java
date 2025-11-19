package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.dto.NotificationsResponse;
import com.helpysoft.mima_api.entity.Notifications;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.NotificationsMapper;
import com.helpysoft.mima_api.repository.NotificationsRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.NotificationStreamService;
import com.helpysoft.mima_api.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationsServiceImpl implements NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private final UsersRepository usersRepository;
    private final NotificationsMapper notificationsMapper;
    private final NotificationStreamService notificationStreamService;

    @Override
    @Transactional
    public NotificationsResponse create(NotificationsRequest request) {
        // Créer la notification
        Users recipient = usersRepository.findByTrackingId(request.getRecipientTrackingId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Notifications notification = notificationsMapper.toEntity(request, recipient);
        notification.setTrackingId(UUID.randomUUID());
        notification.setIsRead(false);
        notification.setCreateDate(LocalDateTime.now());

        Notifications saved = notificationsRepository.save(notification);
        NotificationsResponse response = notificationsMapper.toResponse(saved);

        // ✅ Envoyer la notification en temps réel via SSE
        notificationStreamService.sendNotificationToUser(request.getRecipientTrackingId(), response);

        return response;
    }

    @Override
    public NotificationsResponse update(UUID trackingId, NotificationsRequest request) {
        Notifications notification = notificationsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Notification not found with trackingId: " + trackingId));

        Users recipient = usersRepository.findByTrackingId(request.getRecipientTrackingId())
                .orElseThrow(() -> new RuntimeException("User not found with trackingId: " + request.getRecipientTrackingId()));

        notification.setNotificationType(request.getNotificationType());
        notification.setMessage(request.getMessage());
        notification.setIsRead(request.getIsRead());
        notification.setRecipient(recipient);

        Notifications updatedNotification = notificationsRepository.save(notification);
        return notificationsMapper.toResponse(updatedNotification);
    }

    @Override
    public NotificationsResponse markAsRead(UUID trackingId) {
        Notifications notification = notificationsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));

        notification.setIsRead(true);
        notification.setUpdateDate(LocalDateTime.now());

        Notifications updated = notificationsRepository.save(notification);
        return notificationsMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationsResponse findByTrackingId(UUID trackingId) {
        Notifications notification = notificationsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Notification not found with trackingId: " + trackingId));
        return notificationsMapper.toResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationsResponse> findByRecipient(UUID recipientTrackingId) {
        Users recipient = usersRepository.findByTrackingId(recipientTrackingId)
                .orElseThrow(() -> new RuntimeException("User not found with trackingId: " + recipientTrackingId));
        return notificationsRepository.findByRecipient(recipient)
                .stream()
                .map(notificationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationsResponse> findByIsRead(Boolean isRead) {
        return notificationsRepository.findByIsRead(isRead)
                .stream()
                .map(notificationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationsResponse> findByNotificationType(String notificationType) {
        return notificationsRepository.findByNotificationType(notificationType)
                .stream()
                .map(notificationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationsResponse> findByRecipientAndNotificationType(UUID recipientTrackingId, String notificationType) {
        Users recipient = usersRepository.findByTrackingId(recipientTrackingId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec trackingId: " + recipientTrackingId));
        return notificationsRepository.findByRecipientAndNotificationType(recipient, notificationType)
                .stream()
                .map(notificationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationsResponse> findByRecipientAndNotificationTypeAndIsRead(UUID recipientTrackingId, String notificationType, Boolean isRead) {
        Users recipient = usersRepository.findByTrackingId(recipientTrackingId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec trackingId: " + recipientTrackingId));
        return notificationsRepository.findByRecipientAndNotificationTypeAndIsRead(recipient, notificationType, isRead)
                .stream()
                .map(notificationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationsResponse> findAll() {
        return notificationsRepository.findAllOrderByCreateDateDesc()
                .stream()
                .map(notificationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Notifications notification = notificationsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Notification not found with trackingId: " + trackingId));
        notificationsRepository.delete(notification);
    }
}
