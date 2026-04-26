package com.oscarfndez.framework.config;


import com.oscarfndez.framework.core.services.auth.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader)) {
            log.debug("Request without Authorization header: {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt;
        final String userEmail;
        final List<String> roles;
        try {
            jwt = extractToken(authHeader);
            userEmail = jwtService.extractUserName(jwt);
            roles = jwtService.extractRoles(jwt);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT received for {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.hasText(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = buildUserDetails(userEmail, roles);

            if (userDetails != null && jwtService.isTokenValid(jwt)) {
                log.debug("Authenticated JWT subject={} roles={} path={}", userEmail, roles, request.getRequestURI());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            } else {
                log.warn("JWT subject={} did not provide valid roles for path={}", userEmail, request.getRequestURI());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(String authHeader) {
        if (authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return authHeader.substring(7);
        }

        return authHeader;
    }

    private UserDetails buildUserDetails(String userEmail, List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }

        return User.withUsername(userEmail)
                .password("")
                .authorities(roles.stream().map(SimpleGrantedAuthority::new).toList())
                .build();
    }
}
