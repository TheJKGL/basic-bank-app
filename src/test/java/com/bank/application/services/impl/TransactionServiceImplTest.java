package com.bank.application.services.impl;

import com.bank.application.models.Account;
import com.bank.application.models.dto.DepositRequest;
import com.bank.application.models.dto.TransactionResponse;
import com.bank.application.models.dto.TransferRequest;
import com.bank.application.models.dto.WithdrawRequest;
import com.bank.application.models.Transaction;
import com.bank.application.repositories.AccountRepository;
import com.bank.application.repositories.TransactionRepository;
import com.bank.application.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static com.bank.application.utils.GlobalConstants.DEPOSIT_SUCCESSFUL;
import static com.bank.application.utils.GlobalConstants.TRANSFER_SUCCESSFUL;
import static com.bank.application.utils.GlobalConstants.WITHDRAWAL_SUCCESSFUL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account sourceAccount;
    private Account targetAccount;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sourceAccount = new Account();
        sourceAccount.setAccountNumber("123456789");
        sourceAccount.setBalance(new BigDecimal("1000"));
        
        targetAccount = new Account();
        targetAccount.setAccountNumber("987654321");
        targetAccount.setBalance(new BigDecimal("500"));
    }

    @Test
    void deposit_shouldReturnTransactionResponse_whenDepositIsSuccessful() {
        DepositRequest depositRequest = new DepositRequest("123456789", new BigDecimal("200"), "Deposit comment");
        when(accountRepository.findByAccountNumber(depositRequest.getAccountNumber()))
                .thenReturn(Optional.of(sourceAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setId(1L);
            return transaction;
        });

        TransactionResponse response = transactionService.deposit(depositRequest);

        assertNotNull(response);
        assertEquals("200", response.getAmount().toString());
        assertEquals(new BigDecimal("1200"), response.getCurrentBalance());
        assertEquals(DEPOSIT_SUCCESSFUL, response.getMessage());
        verify(accountRepository).findByAccountNumber(depositRequest.getAccountNumber());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void withdraw_shouldReturnTransactionResponse_whenWithdrawIsSuccessful() {
        WithdrawRequest withdrawRequest = new WithdrawRequest("123456789", new BigDecimal("200"), "Withdraw comment");
        when(accountRepository.findByAccountNumber(withdrawRequest.getAccountNumber()))
                .thenReturn(Optional.of(sourceAccount));
        
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setId(1L);
            return transaction;
        });

        TransactionResponse response = transactionService.withdraw(withdrawRequest);

        assertNotNull(response);
        assertEquals("200", response.getAmount().toString());
        assertEquals(new BigDecimal("800"), response.getCurrentBalance());
        assertEquals(WITHDRAWAL_SUCCESSFUL, response.getMessage());
        verify(accountRepository).findByAccountNumber(withdrawRequest.getAccountNumber());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void withdraw_shouldThrowException_whenNotEnoughBalance() {
        WithdrawRequest withdrawRequest = new WithdrawRequest("123456789", new BigDecimal("1500"), "Withdraw comment");
        when(accountRepository.findByAccountNumber(withdrawRequest.getAccountNumber()))
                .thenReturn(Optional.of(sourceAccount));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.withdraw(withdrawRequest);
        });

        assertEquals("You don't have enough money", exception.getMessage());
        verify(accountRepository).findByAccountNumber(withdrawRequest.getAccountNumber());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void transfer_shouldReturnTransactionResponse_whenTransferIsSuccessful() {
        TransferRequest transferRequest = new TransferRequest("123456789", "987654321", new BigDecimal("200"), "Transfer comment");
        when(accountRepository.findByAccountNumber(transferRequest.getSourceAccountNumber()))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(transferRequest.getTargetAccountNumber()))
                .thenReturn(Optional.of(targetAccount));
        
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setId(1L);
            return transaction;
        });

        TransactionResponse response = transactionService.transfer(transferRequest);

        assertNotNull(response);
        assertEquals("200", response.getAmount().toString());
        assertEquals(new BigDecimal("800"), response.getCurrentBalance()); // Source account balance
        assertEquals(TRANSFER_SUCCESSFUL, response.getMessage());
        assertEquals(new BigDecimal("700"), targetAccount.getBalance()); // Target account balance
        verify(accountRepository).findByAccountNumber(transferRequest.getSourceAccountNumber());
        verify(accountRepository).findByAccountNumber(transferRequest.getTargetAccountNumber());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void transfer_shouldThrowException_whenSourceAccountNotFound() {
        TransferRequest transferRequest = new TransferRequest("123456789", "987654321", new BigDecimal("200"), "Transfer comment");
        when(accountRepository.findByAccountNumber(transferRequest.getSourceAccountNumber()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transfer(transferRequest);
        });

        assertEquals("Account not found with accountNumber: 123456789", exception.getMessage());
        verify(accountRepository).findByAccountNumber(transferRequest.getSourceAccountNumber());
        verify(accountRepository, never()).findByAccountNumber(transferRequest.getTargetAccountNumber());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void transfer_shouldThrowException_whenTargetAccountNotFound() {
        TransferRequest transferRequest = new TransferRequest("123456789", "987654321", new BigDecimal("200"), "Transfer comment");
        when(accountRepository.findByAccountNumber(transferRequest.getSourceAccountNumber()))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(transferRequest.getTargetAccountNumber()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transfer(transferRequest);
        });

        assertEquals("Account not found with accountNumber: 987654321", exception.getMessage());
        verify(accountRepository).findByAccountNumber(transferRequest.getSourceAccountNumber());
        verify(accountRepository).findByAccountNumber(transferRequest.getTargetAccountNumber());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
