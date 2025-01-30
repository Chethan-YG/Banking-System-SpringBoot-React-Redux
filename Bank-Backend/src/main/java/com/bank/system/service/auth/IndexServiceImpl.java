package com.bank.system.service.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bank.system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> getVerify(String token, String code) {
        try {
            String dbToken = userRepository.checkToken(token);

            if (dbToken == null) {
                return ResponseEntity.badRequest().body("This session has expired.");
            }

            userRepository.verifyAccount(token, code);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Verification success.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error verifying account: " + e.getMessage());
        }
    }
}
