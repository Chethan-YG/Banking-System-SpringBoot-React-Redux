package com.bank.system.controller;

import com.bank.system.entity.PaymentRequest;
import com.bank.system.entity.TransferRequest;
import com.bank.system.service.transact.TransactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transact")
public class TransactController {

    @Autowired
    private TransactService transactService;

    @PostMapping("/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@RequestBody Map<String, String> requestMap, @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(createErrorResponse("You must login first."));
        }
        String token = authorizationHeader.substring(7);
        Map<String, Object> response = transactService.deposit(requestMap, token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody TransferRequest request, @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(createErrorResponse("You must login first."));
        }
        String token = authorizationHeader.substring(7);
        Map<String, Object> response = transactService.transfer(request, token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@RequestBody Map<String, String> requestMap, @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(createErrorResponse("You must login first."));
        }
        String token = authorizationHeader.substring(7);
        Map<String, Object> response = transactService.withdraw(requestMap, token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> payment(@RequestBody PaymentRequest request, @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(createErrorResponse("You must login first."));
        }
        String token = authorizationHeader.substring(7);
        Map<String, Object> response = transactService.payment(request, token);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
}
