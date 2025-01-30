package com.bank.system.entity;

import lombok.Data;

@Data
public class PaymentRequest {
    private String beneficiary;
    private String accountNumber;
    private String accountId;
    private String reference;
    private String paymentAmount;
}
