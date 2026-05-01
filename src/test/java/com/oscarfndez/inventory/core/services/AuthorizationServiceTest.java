package com.oscarfndez.inventory.core.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationServiceTest {

    private final AuthorizationService authorizationService = new AuthorizationService();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void hasRoleReturnsTrueForPlainRole() {
        setAuthenticatedUserWithAuthority("USER");

        assertThat(authorizationService.hasRole("USER")).isTrue();
    }

    @Test
    void hasRoleReturnsTrueForSpringRolePrefix() {
        setAuthenticatedUserWithAuthority("ROLE_USER");

        assertThat(authorizationService.hasRole("USER")).isTrue();
    }

    @Test
    void hasRoleReturnsTrueWhenAdminRequiresUserRole() {
        setAuthenticatedUserWithAuthority("ADMIN");

        assertThat(authorizationService.hasRole("USER")).isTrue();
    }

    @Test
    void hasRoleReturnsFalseWhenNonAdminRoleDoesNotMatch() {
        setAuthenticatedUserWithAuthority("USER");

        assertThat(authorizationService.hasRole("ADMIN")).isFalse();
    }

    @Test
    void hasRoleReturnsFalseWhenRoleDoesNotMatch() {
        setAuthenticatedUserWithAuthority("PLATFORM_MANAGER");

        assertThat(authorizationService.hasRole("USER")).isFalse();
    }

    @Test
    void hasRoleReturnsFalseWithoutAuthenticatedUser() {
        assertThat(authorizationService.hasRole("USER")).isFalse();
    }

    private static void setAuthenticatedUserWithAuthority(String authority) {
        User user = new User("oscar@example.com", "password", List.of(new SimpleGrantedAuthority(authority)));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
