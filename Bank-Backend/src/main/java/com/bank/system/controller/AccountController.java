package com.bank.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bank.system.service.account.AccountService;
import com.bank.system.utils.JWTUtil;

import io.jsonwebtoken.Claims;

import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/create_account")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> requestMap, @RequestHeader("Authorization") String authorizationHeader) {
        String accountName = requestMap.get("accountName");
        String accountType = requestMap.get("accountType");

        if (accountName.isEmpty() || accountType.isEmpty()) {
            return ResponseEntity.badRequest().body("Account name cannot be Empty!");
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must login first.");
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            Integer userId = claims.get("userId", Integer.class);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User ID not found in token.");
            }

            return accountService.createAccount(userId, accountName, accountType);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }
}
