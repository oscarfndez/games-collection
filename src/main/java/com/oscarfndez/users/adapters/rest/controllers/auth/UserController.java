package com.oscarfndez.users.adapters.rest.controllers.auth;


import com.oscarfndez.users.adapters.rest.dtos.WhoAmIDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/api/whoami")
    public ResponseEntity<WhoAmIDto> whoAmI(Authentication authentication) {
        String email = authentication.getName();

        String role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("UNKNOWN");

        return ResponseEntity.ok(new WhoAmIDto(email, role));
    }
}