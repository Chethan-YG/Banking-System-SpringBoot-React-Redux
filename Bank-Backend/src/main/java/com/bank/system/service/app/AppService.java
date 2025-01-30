package com.bank.system.service.app;

import com.bank.system.entity.PaymentHistory;
import com.bank.system.entity.TransactionHistory;

import java.util.List;
import java.util.Map;

public interface AppService {

    Map<String, Object> getDashboardData();
    Map<String, List<PaymentHistory>> getPaymentHistory();
    Map<String, List<TransactionHistory>> getTransactionHistory();
    Map<String, List<TransactionHistory>> getAccountTransactionHistory(Map<String, String> requestMap);
}
