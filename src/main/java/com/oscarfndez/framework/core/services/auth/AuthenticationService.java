package com.oscarfndez.framework.core.services.auth;

import com.oscarfndez.framework.adapters.rest.dtos.auth.SignUpRequest;
import com.oscarfndez.framework.adapters.rest.dtos.auth.SigninRequest;
import com.oscarfndez.framework.adapters.rest.dtos.auth.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
