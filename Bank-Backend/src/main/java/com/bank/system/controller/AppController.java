package com.bank.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.system.service.app.AppService;

import java.util.Map;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        try {
            return ResponseEntity.ok(appService.getDashboardData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
        }
    }

    @GetMapping("/payment_history")
    public ResponseEntity<?> getPaymentHistory() {
        try {
            return ResponseEntity.ok(appService.getPaymentHistory());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
        }
    }

    @GetMapping("/transaction_history")
    public ResponseEntity<?> getTransactionHistory() {
        try {
            return ResponseEntity.ok(appService.getTransactionHistory());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
        }
    }

    @PostMapping("/account_transaction_history")
    public ResponseEntity<?> getAccountTransactionHistory(@RequestBody Map<String, String> requestMap) {
        try {
            return ResponseEntity.ok(appService.getAccountTransactionHistory(requestMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
        }
    }
}
