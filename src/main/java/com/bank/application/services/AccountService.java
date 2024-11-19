package com.bank.application.services;

import com.bank.application.models.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(String email);
    Account findAccountByNumber(String accountNumber);
    List<Account> findAllAccounts();
}