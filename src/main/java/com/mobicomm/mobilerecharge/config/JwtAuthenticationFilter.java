package com.mobicomm.mobilerecharge.config;

import com.mobicomm.mobilerecharge.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");
        logger.info("Processing request for: " + requestUri + ", Authorization: " + authHeader);

        // Skip JWT validation for /api/recharge/** endpoints
        if (requestUri.startsWith("/api/recharge/")) {
            logger.info("Skipping JWT validation for permitAll endpoint: " + requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warning("No valid Bearer token found in Authorization header for: " + requestUri);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(7); // Remove "Bearer "
            String username = jwtService.getUsernameFromToken(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtService.getJwtSecret().getBytes())
                        .parseClaimsJws(jwt)
                        .getBody();
                if (!claims.getExpiration().before(new java.util.Date())) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Authenticated user: " + username + " for: " + requestUri);
                } else {
                    logger.warning("Token expired for user: " + username + " for: " + requestUri);
                }
            }
        } catch (Exception e) {
            logger.severe("JWT validation failed for: " + requestUri + ": " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}