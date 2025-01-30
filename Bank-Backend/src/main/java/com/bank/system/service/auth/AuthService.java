package com.bank.system.service.auth;

import com.bank.system.dto.JwtAuthenticationResponse;
import com.bank.system.dto.RefreshTokenRequest;
import com.bank.system.dto.SignIn;

public interface AuthService {
	
	boolean hasCustomerWithEmail(String email);
	JwtAuthenticationResponse signIn(SignIn signin);
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
