package com.bank.application.services.impl;

import com.bank.application.exceptions.ResourceAlreadyExistException;
import com.bank.application.exceptions.ResourceNotFoundException;
import com.bank.application.models.Account;
import com.bank.application.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_shouldCreateNewAccount_whenEmailDoesNotExist() {
        String email = "user@example.com";
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Account result = accountService.createAccount(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(accountRepository).findByEmail(email);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_shouldThrowException_whenEmailAlreadyExists() {
        String email = "user@example.com";
        Account existingAccount = new Account();
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(existingAccount));

        ResourceAlreadyExistException exception = assertThrows(
                ResourceAlreadyExistException.class,
                () -> accountService.createAccount(email)
        );

        assertEquals("Account with the same email {user@example.com} already exist", exception.getMessage());
        verify(accountRepository).findByEmail(email);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void findAccountByNumber_shouldReturnAccount_whenAccountNumberExists() {
        String accountNumber = "1234567890123456";
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        Account result = accountService.findAccountByNumber(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        verify(accountRepository).findByAccountNumber(accountNumber);
    }

    @Test
    void findAccountByNumber_shouldThrowException_whenAccountNumberDoesNotExist() {
        String accountNumber = "1234567890123456";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> accountService.findAccountByNumber(accountNumber)
        );

        assertEquals("Account not found with accountNumber: 1234567890123456", exception.getMessage());
        verify(accountRepository).findByAccountNumber(accountNumber);
    }

    @Test
    void findAllAccounts_shouldReturnListOfAccounts() {
        List<Account> accounts = List.of(new Account(), new Account());
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.findAllAccounts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository).findAll();
    }
}
