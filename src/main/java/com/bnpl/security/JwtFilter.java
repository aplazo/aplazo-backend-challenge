package com.bnpl.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");

        if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("JWT recibido: " + token);

            try {
                Claims claims = jwtUtil.extractClaims(token);
                String username = claims.getSubject();
                logger.info("JWT válido para usuario: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    logger.info("Autenticación configurada correctamente en SecurityContextHolder.");
                }
            } catch (Exception e) {
                logger.error("JWT inválido: " + e.getMessage());
            }
        } else {
            logger.info("No se recibió JWT en la solicitud.");
        }

        filterChain.doFilter(request, response);
    }

}
