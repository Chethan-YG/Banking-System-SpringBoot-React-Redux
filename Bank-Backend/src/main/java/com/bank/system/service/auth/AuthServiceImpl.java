package com.bank.system.service.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.bank.system.dto.JwtAuthenticationResponse;
import com.bank.system.dto.RefreshTokenRequest;
import com.bank.system.dto.SignIn;
import com.bank.system.entity.User;
import com.bank.system.repository.UserRepository;
import com.bank.system.utils.JWTUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtService;

    @Override
    public boolean hasCustomerWithEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    
    @Override
    public JwtAuthenticationResponse signIn(SignIn signin) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signin.getEmail(), signin.getPassword()));

        var user = userRepository.findByEmail(signin.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Email or Password"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getFirstname());
        claims.put("email", user.getEmail());
        claims.put("userId", user.getUser_id());
        

        String jwt = jwtService.generateToken(user.getEmail() ,claims);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), claims);
        return new JwtAuthenticationResponse(jwt, refreshToken);
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String email = jwtService.extractUserName(refreshTokenRequest.getToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for the given token"));

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("name", user.getFirstname());
            claims.put("email", user.getEmail());
            claims.put("userId", user.getUser_id());

            String newJwt = jwtService.generateToken(user.getEmail(),claims);

            return new JwtAuthenticationResponse(newJwt, refreshTokenRequest.getToken());
        } else {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
    }
}
