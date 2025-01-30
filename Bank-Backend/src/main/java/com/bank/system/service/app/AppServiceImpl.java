package com.bank.system.service.app;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bank.system.entity.Account;
import com.bank.system.entity.PaymentHistory;
import com.bank.system.entity.TransactionHistory;
import com.bank.system.entity.User;
import com.bank.system.repository.AccountRepository;
import com.bank.system.repository.PaymentHistoryRepository;
import com.bank.system.repository.TransactHistoryRepository;
import com.bank.system.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService{

    private final AccountRepository accountRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final TransactHistoryRepository transactHistoryRepository;
    private final UserRepository userRepository;

    private UserDetails getAuthenticatedUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Map<String, Object> getDashboardData() {
        UserDetails userDetails = getAuthenticatedUser();
        String userEmail = userDetails.getUsername();
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        User user = userOpt.get();
        int userId = user.getUser_id();
        List<Account> getUserAccounts = accountRepository.getUserAccountsById(userId);
        BigDecimal totalAccountsBalance = accountRepository.getTotalBalance(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userAccounts", getUserAccounts);
        response.put("totalBalance", totalAccountsBalance);

        return response;
    }

    public Map<String, List<PaymentHistory>> getPaymentHistory() {
        UserDetails userDetails = getAuthenticatedUser();
        String userEmail = userDetails.getUsername();
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        User user = userOpt.get();
        int userId = user.getUser_id();

        List<PaymentHistory> userPaymentHistory = paymentHistoryRepository.getPaymentsRecordsById(userId);

        Map<String, List<PaymentHistory>> response = new HashMap<>();
        response.put("payment_history", userPaymentHistory);

        return response;
    }

    public Map<String, List<TransactionHistory>> getTransactionHistory() {
        UserDetails userDetails = getAuthenticatedUser();
        String userEmail = userDetails.getUsername();
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        User user = userOpt.get();
        int userId = user.getUser_id();

        List<TransactionHistory> userTransactionHistory = transactHistoryRepository.getTransactionRecordsById(userId);

        Map<String, List<TransactionHistory>> response = new HashMap<>();
        response.put("transaction_history", userTransactionHistory);

        return response;
    }

    public Map<String, List<TransactionHistory>> getAccountTransactionHistory(Map<String, String> requestMap) {
        UserDetails userDetails = getAuthenticatedUser();
        String accountIdStr = requestMap.get("account_id");
        int accountId = Integer.parseInt(accountIdStr);

        List<TransactionHistory> accountTransactionHistory = transactHistoryRepository.getTransactionRecordsByAccountId(accountId);

        Map<String, List<TransactionHistory>> response = new HashMap<>();
        response.put("transaction_history", accountTransactionHistory);

        return response;
    }
}
