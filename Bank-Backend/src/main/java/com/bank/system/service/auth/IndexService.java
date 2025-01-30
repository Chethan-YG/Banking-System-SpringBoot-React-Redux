package com.bank.system.service.auth;

import org.springframework.http.ResponseEntity;


public interface IndexService {
    ResponseEntity<?> getVerify(String token, String code);
}
