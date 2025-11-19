package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.dto.NotificationsResponse;
import com.helpysoft.mima_api.service.NotificationsService;
import com.helpysoft.mima_api.service.NotificationStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificationsService notificationsService;
    private final NotificationStreamService notificationStreamService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody NotificationsRequest request) {
        try {
            NotificationsResponse response = notificationsService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notification créée avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de la notification", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID trackingId, @RequestBody NotificationsRequest request) {
        try {
            NotificationsResponse response = notificationsService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notification mise à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de la notification", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> findByTrackingId(@PathVariable UUID trackingId) {
        try {
            NotificationsResponse response = notificationsService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notification récupérée avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Notification non trouvée", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/recipient/{recipientTrackingId}")
    public ResponseEntity<Map<String, Object>> findByRecipient(@PathVariable UUID recipientTrackingId) {
        try {
            List<NotificationsResponse> responses = notificationsService.findByRecipient(recipientTrackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notifications récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des notifications", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/read/{isRead}")
    public ResponseEntity<Map<String, Object>> findByIsRead(@PathVariable Boolean isRead) {
        try {
            List<NotificationsResponse> responses = notificationsService.findByIsRead(isRead);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notifications récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des notifications", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/type/{notificationType}")
    public ResponseEntity<Map<String, Object>> findByNotificationType(@PathVariable String notificationType) {
        try {
            List<NotificationsResponse> responses = notificationsService.findByNotificationType(notificationType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notifications récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des notifications", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/recipient/{recipientTrackingId}/type/{notificationType}")
    public ResponseEntity<Map<String, Object>> findByRecipientAndNotificationType(
            @PathVariable UUID recipientTrackingId,
            @PathVariable String notificationType) {
        try {
            List<NotificationsResponse> responses = notificationsService.findByRecipientAndNotificationType(recipientTrackingId, notificationType);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notifications récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des notifications", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/recipient/{recipientTrackingId}/type/{notificationType}/read/{isRead}")
    public ResponseEntity<Map<String, Object>> findByRecipientAndNotificationTypeAndIsRead(
            @PathVariable UUID recipientTrackingId,
            @PathVariable String notificationType,
            @PathVariable Boolean isRead) {
        try {
            List<NotificationsResponse> responses = notificationsService.findByRecipientAndNotificationTypeAndIsRead(recipientTrackingId, notificationType, isRead);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notifications récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des notifications", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<NotificationsResponse> responses = notificationsService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notifications récupérées avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des notifications", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID trackingId) {
        try {
            notificationsService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notification supprimée avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de la notification", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * ✅ Endpoint SSE pour le streaming de notifications en temps réel
     * L'utilisateur se connecte à ce endpoint et reçoit les notifications en temps réel
     * Le token JWT est passé en paramètre de requête car EventSource ne supporte pas les headers
     */
    @GetMapping(value = "/stream/{userTrackingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications(
            @PathVariable UUID userTrackingId,
            @RequestParam(required = false) String token) {
        // Le token est validé par le filtre JWT si présent dans les paramètres
        // ou par le header Authorization standard
        return notificationStreamService.subscribe(userTrackingId);
    }

    /**
     * ✅ Endpoint pour marquer une notification comme lue
     */
    @PutMapping("/read/{trackingId}")
    public ResponseEntity<Map<String, Object>> markAsRead(@PathVariable UUID trackingId) {
        try {
            NotificationsResponse response = notificationsService.markAsRead(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Notification marquée comme lue", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors du marquage de la notification", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}