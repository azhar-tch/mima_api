package com.helpysoft.mima_api.config;

import com.helpysoft.mima_api.service.JwtBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final JwtBlacklistService jwtBlacklistService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, JwtBlacklistService jwtBlacklistService, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.jwtBlacklistService = jwtBlacklistService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = null;
        String requestPath = request.getRequestURI();

        // 1. Essayer de récupérer le token depuis le header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            logger.debug("Token trouvé dans le header Authorization pour {}", requestPath);
        }

        // 2. Si pas de token dans le header, essayer depuis les paramètres de requête (pour SSE)
        if (token == null) {
            token = request.getParameter("token");
            if (token != null) {
                logger.debug("Token trouvé dans les paramètres de requête pour {}", requestPath);
            }
        }

        // 3. Valider et authentifier le token
        if (token != null) {
            try {
                if (jwtBlacklistService.isBlacklisted(token)) {
                    logger.warn("Token blacklisté pour {}", requestPath);
                } else if (!jwtUtils.validateToken(token)) {
                    logger.warn("Token invalide ou expiré pour {}", requestPath);
                } else {
                    String email = jwtUtils.extractUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authentification réussie pour {} (user: {})", requestPath, email);
                }
            } catch (Exception e) {
                logger.error("Erreur lors de la validation du token pour {}: {}", requestPath, e.getMessage());
            }
        } else {
            logger.warn("Aucun token trouvé pour {}", requestPath);
        }

        chain.doFilter(request, response);
    }
}
