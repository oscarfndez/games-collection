package com.oscarfndez.inventory.core.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {


    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails loggedInUser)) {
            log.debug("Role check failed. No authenticated UserDetails principal found. requiredRole={}", role);
            return false;
        }

        boolean authorized = hasRole(loggedInUser.getAuthorities(), role);
        log.debug("Role check subject={} requiredRole={} authorities={} authorized={}",
                loggedInUser.getUsername(), role, loggedInUser.getAuthorities(), authorized);
        return authorized;
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        if (authorities.isEmpty()) {
            return false;
        }

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(role)
                        || auth.equals("ROLE_" + role)
                        || auth.equals("ADMIN")
                        || auth.equals("ROLE_ADMIN"));
    }
}
