package com.bank.application.services.impl;

import com.bank.application.models.Account;
import com.bank.application.exceptions.ResourceAlreadyExistException;
import com.bank.application.exceptions.ResourceNotFoundException;
import com.bank.application.repositories.AccountRepository;
import com.bank.application.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.bank.application.utils.Utils.generateAccountNumber;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            throw new ResourceAlreadyExistException(
                    String.format("Account with the same email {%s} already exist", email)
            );
        }

        return accountRepository.save(Account.builder()
                .email(email)
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .build());
    }

    @Override
    public Account findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with accountNumber: " + accountNumber));
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }
}
