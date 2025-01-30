package com.bank.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.system.dto.JwtAuthenticationResponse;
import com.bank.system.dto.RefreshTokenRequest;
import com.bank.system.entity.User;
import com.bank.system.service.auth.AuthService;
import com.bank.system.service.auth.RegisterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class RegisterController {

	private final RegisterService registerService;
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult,
			@RequestParam("confirm_password") String confirmPassword) {

		if (bindingResult.hasErrors()) {
			List<String> errorMessages = new ArrayList<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMessages.add(error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errorMessages);
		}

		String registrationResult = registerService.registerUser(user, confirmPassword);

		if (registrationResult != null) {
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Registration success. Please check your email and verify your account.");
			response.put("user", user);
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.badRequest().body(registrationResult);
		}
	}
	
	@PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

}
