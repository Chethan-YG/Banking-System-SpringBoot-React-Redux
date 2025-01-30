package com.bank.system.service.transact;

import com.bank.system.entity.PaymentRequest;
import com.bank.system.entity.TransferRequest;

import java.util.Map;

public interface TransactService {

    Map<String, Object> deposit(Map<String, String> requestMap, String token);
    Map<String, Object> transfer(TransferRequest request, String token);
    Map<String, Object> withdraw(Map<String, String> requestMap, String token);
    Map<String, Object> payment(PaymentRequest request, String token);
}
