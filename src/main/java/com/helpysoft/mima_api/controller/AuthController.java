package com.helpysoft.mima_api.controller;

import com.helpysoft.mima_api.config.JwtUtils;
import com.helpysoft.mima_api.dto.UsersRequest;
import com.helpysoft.mima_api.entity.Rules;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.repository.RulesRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.JwtBlacklistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Authentification")
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JwtBlacklistService jwtBlacklistService;
    private final RulesRepository rulesRepository;

    public AuthController(UsersRepository usersRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, JwtBlacklistService jwtBlacklistService, RulesRepository rulesRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.jwtBlacklistService = jwtBlacklistService;
        this.rulesRepository = rulesRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UsersRequest usersRequest) {
        // Vérifier si l'email existe déjà
        if (usersRequest.getEmail() != null && usersRepository.findByEmail(usersRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Cet email existe déjà"));
        }

        // Chercher le role "USER"
        Optional<Rules> ruleUserOpt = rulesRepository.findByTitle("USER");
        if (ruleUserOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Rôle USER non trouvé"));
        }

        Users user = new Users();
        user.setTrackingId(UUID.randomUUID());
        user.setEmail(usersRequest.getEmail());
        user.setFirstName(usersRequest.getFirstName());
        user.setLastName(usersRequest.getLastName());
        user.setPhoneNumber(usersRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(usersRequest.getPassword()));
        user.setRule(ruleUserOpt.get());
        user.setIsActive(true);

        usersRepository.save(user);

        return buildAuthResponse(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UsersRequest usersRequest) {
        try {
            Optional<Users> utilisateurOptional = Optional.empty();
            if (usersRequest.getEmail() != null && !usersRequest.getEmail().isBlank()) {
                utilisateurOptional = usersRepository.findByEmail(usersRequest.getEmail());
            }

            if (utilisateurOptional.isEmpty() ||
                    !passwordEncoder.matches(usersRequest.getPassword(), utilisateurOptional.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Identifiants invalides"));
            }

            Users user = utilisateurOptional.get();

            return buildAuthResponse(user);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Erreur d'authentification"));
        }
    }

    private ResponseEntity<?> buildAuthResponse(Users user) {
        String token = jwtUtils.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
                "email", user.getEmail(),
                "firstName", user.getFirstName() != null ? user.getFirstName() : "",
                "lastName", user.getLastName() != null ? user.getLastName() : "",
                "phoneNumber", user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
                "trackingId", user.getTrackingId(),
                "rule", user.getRule() != null ? user.getRule().getTitle() : null
        ));
        response.put("message", "Authentification réussie");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Mettre le JWT en blacklist
        long expirationTime = jwtUtils.extractExpirationDate(token).getTime() - System.currentTimeMillis();
        jwtBlacklistService.addToBlacklist(token, expirationTime);

        // Supprimer le FCM token côté backend
        try {
            String email = jwtUtils.extractUsername(token);
            Optional<Users> userOpt = usersRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                user.setFcmToken(null);
                usersRepository.save(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du FCM token");
        }

        return ResponseEntity.ok("Déconnexion réussie !");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // Extraire le token sans le préfixe "Bearer "
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Rafraichir le token avec la méthode définie dans JwtUtils
            String refreshedToken = jwtUtils.refreshJwtToken(token);

            if (refreshedToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Le token est invalide ou expiré"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("token", refreshedToken);

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors du rafraîchissement du token"));
        }
    }
}
