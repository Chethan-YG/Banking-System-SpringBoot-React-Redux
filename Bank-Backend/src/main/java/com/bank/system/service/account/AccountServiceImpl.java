package com.bank.system.service.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bank.system.entity.Account;
import com.bank.system.helpers.GenAccountNumber;
import com.bank.system.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final AccountRepository accountRepository;

	public ResponseEntity<?> createAccount(Integer userId, String accountName, String accountType) {
	    // Generate a new account number
	    int setAccountNumber = GenAccountNumber.generateAccountNumber();
	    String bankAccountNumber = Integer.toString(setAccountNumber);

	    // Create a new account object
	    Account newAccount = new Account();
	    newAccount.setAccountNumber(bankAccountNumber);
	    newAccount.setAccountName(accountName);
	    newAccount.setAccountType(accountType);
	    newAccount.setBalance(BigDecimal.ZERO);
	    newAccount.setCreateAt(LocalDateTime.now());
	    newAccount.setUser_id(userId);

	    try {
	        Account savedAccount = accountRepository.save(newAccount);

	        return ResponseEntity.status(201).body(savedAccount);
	    } catch (Exception e) {
	        System.out.println("Error creating account: " + e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Error creating account. Please try again later.");
	    }
	}

}
