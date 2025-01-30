package com.bank.system.service.transact;

import com.bank.system.entity.PaymentRequest;
import com.bank.system.entity.TransferRequest;
import com.bank.system.repository.AccountRepository;
import com.bank.system.repository.PaymentRepository;
import com.bank.system.repository.TransactRepository;
import com.bank.system.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TransactServiceImpl  implements TransactService{

    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final TransactRepository transactRepository;
    private final JWTUtil jwtUtil;

    public Map<String, Object> deposit(Map<String, String> requestMap, String token) {
        Claims claims = jwtUtil.extractAllClaims(token);
        Integer userId = claims.get("userId", Integer.class);
        if (userId == null) {
            return createErrorResponse("Invalid token. Please log in again.");
        }

        String depositAmountStr = requestMap.get("depositAmount");
        String accountIDStr = requestMap.get("accountId");
        if (depositAmountStr == null || accountIDStr == null || depositAmountStr.isEmpty() || accountIDStr.isEmpty()) {
            return createErrorResponse("Deposit amount and account ID cannot be empty.");
        }

        double depositAmount;
        int accountId;
        try {
            depositAmount = Double.parseDouble(depositAmountStr);
            accountId = Integer.parseInt(accountIDStr);
        } catch (NumberFormatException e) {
            return createErrorResponse("Invalid number format for deposit amount or account ID.");
        }

        if (depositAmount <= 0) {
            return createErrorResponse("Deposit amount must be greater than zero.");
        }

        Double currentBalance = accountRepository.getAccountBalance(userId, accountId);
        currentBalance = (currentBalance != null) ? currentBalance : 0.0;
        Double newBalance = currentBalance + depositAmount;

        accountRepository.changeAccountsBalanceById(newBalance, accountId);

        transactRepository.logTransaction(accountId, "deposit", depositAmount, "online", "success", "Deposit transaction successful", LocalDateTime.now(), userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Amount deposited successfully.");
        response.put("accounts", accountRepository.getUserAccountsById(userId));

        return response;
    }

    public Map<String, Object> transfer(TransferRequest request, String token) {
        Claims claims = jwtUtil.extractAllClaims(token);
        Integer userId = claims.get("userId", Integer.class);
        if (userId == null) {
            return createErrorResponse("Invalid token. Please log in again.");
        }

        String transfer_from = request.getSourceAccount();
        String transfer_to = request.getTargetAccount();
        String transfer_amount = request.getAmount();

        if (transfer_from.isEmpty() || transfer_to.isEmpty() || transfer_amount.isEmpty()) {
            return createErrorResponse("The account transferring from and to along with the amount cannot be empty!");
        }

        int transferFromId = Integer.parseInt(transfer_from);
        int transferToId = Integer.parseInt(transfer_to);
        double transferAmount = Double.parseDouble(transfer_amount);

        if (transferFromId == transferToId) {
            return createErrorResponse("Cannot transfer into the same account.");
        }

        if (transferAmount == 0) {
            return createErrorResponse("Cannot transfer an amount of 0 (zero) value.");
        }

        double currentBalanceOfAccountTransferringFrom = accountRepository.getAccountBalance(userId, transferFromId);

        if (currentBalanceOfAccountTransferringFrom < transferAmount) {
            transactRepository.logTransaction(transferFromId, "transfer", transferAmount, "online", "failed", "Insufficient funds.", LocalDateTime.now(), userId);
            return createErrorResponse("Insufficient funds.");
        }

        double newBalanceOfAccountTransferringFrom = currentBalanceOfAccountTransferringFrom - transferAmount;
        accountRepository.changeAccountsBalanceById(newBalanceOfAccountTransferringFrom, transferFromId);

        double currentBalanceOfAccountTransferringTo = accountRepository.getAccountBalance(userId, transferToId);
        double newBalanceOfAccountTransferringTo = currentBalanceOfAccountTransferringTo + transferAmount;
        accountRepository.changeAccountsBalanceById(newBalanceOfAccountTransferringTo, transferToId);

        transactRepository.logTransaction(transferFromId, "transfer", transferAmount, "online", "success", "Transfer transaction successful", LocalDateTime.now(), userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer completed successfully.");
        response.put("accounts", accountRepository.getUserAccountsById(userId));

        return response;
    }

    public Map<String, Object> withdraw(Map<String, String> requestMap, String token) {
        Claims claims = jwtUtil.extractAllClaims(token);
        Integer userId = claims.get("userId", Integer.class);
        if (userId == null) {
            return createErrorResponse("Invalid token. Please log in again.");
        }

        String withdrawalAmount = requestMap.get("withdrawalAmount");
        String accountId = requestMap.get("accountId");

        if (withdrawalAmount.isEmpty() || accountId.isEmpty()) {
            return createErrorResponse("Account withdrawing from and withdrawal amount cannot be empty!");
        }

        int account_id = Integer.parseInt(accountId);
        double withdrawal_amount = Double.parseDouble(withdrawalAmount);

        if (withdrawal_amount == 0) {
            return createErrorResponse("Withdrawal amount cannot be 0.");
        }

        double currentBalance = accountRepository.getAccountBalance(userId, account_id);

        if (currentBalance < withdrawal_amount) {
            transactRepository.logTransaction(account_id, "withdrawal", withdrawal_amount, "online", "failed", "Insufficient funds.", LocalDateTime.now(), userId);
            return createErrorResponse("Insufficient funds.");
        }

        double newBalance = currentBalance - withdrawal_amount;
        accountRepository.changeAccountsBalanceById(newBalance, account_id);

        transactRepository.logTransaction(account_id, "withdrawal", withdrawal_amount, "online", "success", "Withdrawal transaction successful", LocalDateTime.now(), userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Withdrawal successful!");
        response.put("accounts", accountRepository.getUserAccountsById(userId));

        return response;
    }

    public Map<String, Object> payment(PaymentRequest request, String token) {
        Claims claims = jwtUtil.extractAllClaims(token);
        Integer userId = claims.get("userId", Integer.class);
        if (userId == null) {
            return createErrorResponse("Invalid token. Please log in again.");
        }

        String beneficiary = request.getBeneficiary();
        String account_number = request.getAccountNumber();
        String account_id = request.getAccountId();
        String reference = request.getReference();
        String payment_amount = request.getPaymentAmount();

        if (beneficiary.isEmpty() || account_number.isEmpty() || account_id.isEmpty() || payment_amount.isEmpty()) {
            return createErrorResponse("Beneficiary, account number, account paying from and account payment amount cannot be empty.");
        }

        int accountID = Integer.parseInt(account_id);
        double paymentAmount = Double.parseDouble(payment_amount);

        if (paymentAmount == 0) {
            return createErrorResponse("Payment amount cannot be 0.");
        }

        double currentBalance = accountRepository.getAccountBalance(userId, accountID);

        if (currentBalance < paymentAmount) {
            String reasonCode = "Could not process payment due to insufficient funds.";
            paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "failed", reasonCode, LocalDateTime.now());
            transactRepository.logTransaction(accountID, "payment", paymentAmount, "online", "failed", "Insufficient funds.", LocalDateTime.now(), userId);
            return createErrorResponse("Insufficient funds.");
        }

        double newBalance = currentBalance - paymentAmount;
        accountRepository.changeAccountsBalanceById(newBalance, accountID);

        String reasonCode = "Payment processed successfully!";
        paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "success", reasonCode, LocalDateTime.now());

        transactRepository.logTransaction(accountID, "payment", paymentAmount, "online", "success", "Payment transaction successful", LocalDateTime.now(), userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", reasonCode);
        response.put("accounts", accountRepository.getUserAccountsById(userId));

        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
}
