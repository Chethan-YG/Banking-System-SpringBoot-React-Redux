package com.bank.system.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.system.dto.JwtAuthenticationResponse;
import com.bank.system.dto.SignIn;
import com.bank.system.entity.User;
import com.bank.system.repository.UserRepository;
import com.bank.system.service.auth.AuthService;
import com.bank.system.service.auth.IndexService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository repository;
    private final IndexService indexService;


    @PostMapping("/signin")
    public ResponseEntity<?> signInCustomer(@RequestBody SignIn signInRequest) {
        Optional<User> user = repository.findByEmail(signInRequest.getEmail());
        User user1=user.get();

        if (user.get() == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        if (user1.getVerified() == 0) {
            return ResponseEntity.badRequest().body("User not verified. Please verify your email.");
        }

        JwtAuthenticationResponse response = authService.signIn(signInRequest);
        return ResponseEntity.ok(response);
    }


    
    @GetMapping("/login")
    public String login() {
        return "redirect:http://localhost:3000";
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
    	return ResponseEntity.ok("Logged out successfully.");
    }
    
    @GetMapping("/verify")
    public ResponseEntity<?> getVerify(@RequestParam("token")String token, @RequestParam("code")String code){
        return indexService.getVerify(token, code);
    }
}
