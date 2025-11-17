package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.Helper;
import com.helpysoft.mima_api.dto.UsersRequest;
import com.helpysoft.mima_api.dto.UsersResponse;
import com.helpysoft.mima_api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UsersRequest request) {
        try {
            UsersResponse response = usersService.create(request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Utilisateur créé avec succès", response, ""),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la création de l'utilisateur", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{trackingId}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable UUID trackingId, @RequestBody UsersRequest request) {
        try {
            UsersResponse response = usersService.update(trackingId, request);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Utilisateur mis à jour avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la mise à jour de l'utilisateur", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/get/{trackingId}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable UUID trackingId) {
        try {
            UsersResponse response = usersService.findByTrackingId(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Utilisateur récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Utilisateur non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/get/email/{email}")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email) {
        try {
            UsersResponse response = usersService.findByEmail(email);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Utilisateur récupéré avec succès", response, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Utilisateur non trouvé", null, e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listUsers() {
        try {
            List<UsersResponse> responses = usersService.findAll();
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Utilisateurs récupérés avec succès", responses, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la récupération des utilisateurs", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/delete/{trackingId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable UUID trackingId) {
        try {
            usersService.delete(trackingId);
            return new ResponseEntity<>(
                    Helper.responseFormat(false, "Utilisateur supprimé avec succès", null, ""),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Helper.responseFormat(true, "Erreur lors de la suppression de l'utilisateur", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
