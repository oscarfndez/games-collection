package com.oscarfndez.users.core.services.auth;

import com.oscarfndez.users.adapters.rest.dtos.auth.SignUpRequest;
import com.oscarfndez.users.adapters.rest.dtos.auth.SigninRequest;
import com.oscarfndez.users.adapters.rest.dtos.auth.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
