package com.oscarfndez.framework.core.services.auth;


import com.oscarfndez.framework.adapters.persistence.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;

    public boolean hasRole(String role) {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();

        return hasRole(loggedInUser.getAuthorities(), role)
                && userRepository.findByEmail(loggedInUser.getUsername()).isPresent();
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return (!authorities.isEmpty() && authorities.stream().map(GrantedAuthority::getAuthority).anyMatch(auth -> auth.equals(role)));
    }
}
