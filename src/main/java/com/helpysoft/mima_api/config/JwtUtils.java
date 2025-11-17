package com.helpysoft.mima_api.config;

import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.expiration-time}")
    private long expirationTime;

    private final UserDetailsService userDetailsService;
    private final UsersRepository usersRepository;

    public JwtUtils(UserDetailsService userDetailsService, UsersRepository usersRepository) {
        this.userDetailsService = userDetailsService;
        this.usersRepository = usersRepository;
    }

    /**
     * Générer un token JWT pour un utilisateur
     */
    public String generateToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        if (user.getRule() != null) {
            claims.put("role", user.getRule().getTitle());
        }

        if (user.getTrackingId() != null) {
            claims.put("trackingId", user.getTrackingId().toString());
        }

        return createToken(claims, user.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Méthode de validation avec UserDetails (utilisée par le filtre)
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            boolean usernameMatches = username.equals(userDetails.getUsername());
            boolean tokenNotExpired = !isTokenExpired(token);
            return usernameMatches && tokenNotExpired;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Méthode de validation simple (pour compatibilité)
     */
    public Boolean validateToken(String token) {
        try {
            String username = getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extraire le username du token
     */
    public String getUsernameFromToken(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Alias pour compatibilité
     */
    public String extractUsername(String token) {
        return getUsernameFromToken(token);
    }

    /**
     * Vérifier si le token est expiré
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpirationDate(token);
            Date now = new Date();
            return expiration.before(now);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Extraire la date d'expiration du token
     */
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extraire le rôle du token
     */
    @SuppressWarnings("unchecked")
    public String extractRole(String token) {
        try {
            Claims claims = extractAllClaim(token);
            return (String) claims.get("role");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extraire le trackingId du token
     */
    public UUID extractTrackingId(String token) {
        try {
            Claims claims = extractAllClaim(token);
            String trackingIdStr = (String) claims.get("trackingId");

            if (trackingIdStr != null && !trackingIdStr.isEmpty()) {
                return UUID.fromString(trackingIdStr);
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extraire le trackingId depuis la base de données (fallback)
     */
    public UUID extractTrackingIdFromDatabase(String token) {
        try {
            String username = getUsernameFromToken(token);

            return usersRepository.findByEmail(username)
                    .map(Users::getTrackingId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + username));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Extraire des claims spécifiques du token
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extraire tous les claims du token
     */
    private Claims extractAllClaim(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Rafraîchir un token JWT
     */
    public String refreshJwtToken(String token) {
        try {
            // Extraire le token sans le préfixe "Bearer "
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Vérifie si le token est valide
            String username = getUsernameFromToken(token);

            Optional<Users> userOpt = usersRepository.findByEmail(username);
            if (userOpt.isEmpty()) {
                return null;
            }

            Users user = userOpt.get();

            // Si le token n'est pas encore expiré
            if (!isTokenExpired(token)) {
                return generateToken(user);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
