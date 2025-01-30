package com.bank.system.service.account;

import org.springframework.http.ResponseEntity;

public interface AccountService {
	 ResponseEntity<?> createAccount(Integer userId, String accountName, String accountType);
}