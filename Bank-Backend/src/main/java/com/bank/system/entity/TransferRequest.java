package com.bank.system.entity;

import lombok.Data;

@Data
public class TransferRequest {
    private String sourceAccount;
    private String targetAccount;
    private String amount;

    
}

