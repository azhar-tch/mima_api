package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.NotificationsResponse;
import com.helpysoft.mima_api.service.NotificationStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class NotificationStreamServiceImpl implements NotificationStreamService {

    // Stocke les connexions SSE actives pour chaque utilisateur
    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(UUID userTrackingId) {
        // Timeout de 30 minutes
        SseEmitter emitter = new SseEmitter(1800000L);

        // Ajouter l'emitter à la map
        emitters.put(userTrackingId, emitter);

        // Callback quand la connexion est fermée ou expire
        emitter.onCompletion(() -> {
            log.info("SSE connection completed for user: {}", userTrackingId);
            emitters.remove(userTrackingId);
        });

        emitter.onTimeout(() -> {
            log.info("SSE connection timeout for user: {}", userTrackingId);
            emitters.remove(userTrackingId);
        });

        emitter.onError((ex) -> {
            log.error("SSE connection error for user: {}", userTrackingId, ex);
            emitters.remove(userTrackingId);
        });

        // Envoyer un message initial de connexion
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("Connected to notification stream"));
            log.info("User {} subscribed to notifications", userTrackingId);
        } catch (IOException e) {
            log.error("Error sending initial message to user: {}", userTrackingId, e);
            emitters.remove(userTrackingId);
        }

        return emitter;
    }

    @Override
    public void sendNotificationToUser(UUID userTrackingId, NotificationsResponse notification) {
        SseEmitter emitter = emitters.get(userTrackingId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification));
                log.info("Notification sent to user: {}", userTrackingId);
            } catch (IOException e) {
                log.error("Error sending notification to user: {}", userTrackingId, e);
                emitters.remove(userTrackingId);
            }
        } else {
            log.debug("No active connection for user: {}", userTrackingId);
        }
    }

    @Override
    public void removeEmitter(UUID userTrackingId) {
        SseEmitter emitter = emitters.remove(userTrackingId);
        if (emitter != null) {
            emitter.complete();
            log.info("Emitter removed for user: {}", userTrackingId);
        }
    }
}
