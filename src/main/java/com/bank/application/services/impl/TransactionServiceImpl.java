package com.bank.application.services.impl;

import com.bank.application.models.Account;
import com.bank.application.models.dto.DepositRequest;
import com.bank.application.models.Transaction;
import com.bank.application.models.dto.TransactionResponse;
import com.bank.application.models.TransactionType;
import com.bank.application.models.dto.TransferRequest;
import com.bank.application.models.dto.WithdrawRequest;
import com.bank.application.exceptions.ResourceNotFoundException;
import com.bank.application.repositories.AccountRepository;
import com.bank.application.repositories.TransactionRepository;
import com.bank.application.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.bank.application.utils.GlobalConstants.DEPOSIT_SUCCESSFUL;
import static com.bank.application.utils.GlobalConstants.TRANSFER_SUCCESSFUL;
import static com.bank.application.utils.GlobalConstants.WITHDRAWAL_SUCCESSFUL;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public TransactionResponse deposit(DepositRequest request) {
        Account account = findAccountByInputNumber(request.getAccountNumber());
        account.setBalance(account.getBalance().add(request.getAmount()));
        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .comment(request.getComment())
                .build();
        Long transactionId = transactionRepository.save(transaction).getId();
        return TransactionResponse.builder()
                .id(transactionId)
                .amount(request.getAmount())
                .currentBalance(account.getBalance())
                .message(DEPOSIT_SUCCESSFUL)
                .build();
    }

    @Transactional
    @Override
    public TransactionResponse withdraw(WithdrawRequest request) {
        Account account = findAccountByInputNumber(request.getAccountNumber());
        validateBalance(account.getBalance(), request.getAmount());
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(request.getAmount())
                .comment(request.getComment())
                .build();
        Long transactionId = transactionRepository.save(transaction).getId();
        return TransactionResponse.builder()
                .id(transactionId)
                .amount(request.getAmount())
                .currentBalance(account.getBalance())
                .message(WITHDRAWAL_SUCCESSFUL)
                .build();
    }

    private void validateBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new RuntimeException("You don't have enough money");
        }
    }

    @Transactional
    @Override
    public TransactionResponse transfer(TransferRequest request) {
        BigDecimal transferAmount = request.getAmount();
        Account sourceAccount = findAccountByInputNumber(request.getSourceAccountNumber());
        Account targerAccount = findAccountByInputNumber(request.getTargetAccountNumber());

        validateBalance(sourceAccount.getBalance(), request.getAmount());

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
        targerAccount.setBalance(targerAccount.getBalance().add(transferAmount));

        Transaction transaction = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .comment(request.getComment())
                .build();
        Long transactionId = transactionRepository.save(transaction).getId();
        return TransactionResponse.builder()
                .id(transactionId)
                .amount(request.getAmount())
                .currentBalance(sourceAccount.getBalance())
                .message(TRANSFER_SUCCESSFUL)
                .build();
    }

    private Account findAccountByInputNumber(String accountNumber) {
        return accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with accountNumber: " + accountNumber));
    }
}