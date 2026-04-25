package com.oscarfndez.inventory.core.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthorizationService {


    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails loggedInUser)) {
            return false;
        }

        return hasRole(loggedInUser.getAuthorities(), role);
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        if (authorities.isEmpty()) {
            return false;
        }

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(role) || auth.equals("ROLE_" + role));
    }
}
