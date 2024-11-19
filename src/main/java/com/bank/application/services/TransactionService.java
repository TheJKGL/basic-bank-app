package com.bank.application.services;

import com.bank.application.models.dto.DepositRequest;
import com.bank.application.models.dto.TransactionResponse;
import com.bank.application.models.dto.TransferRequest;
import com.bank.application.models.dto.WithdrawRequest;

public interface TransactionService {
    TransactionResponse deposit(DepositRequest request);
    TransactionResponse withdraw(WithdrawRequest request);
    TransactionResponse transfer(TransferRequest request);
}