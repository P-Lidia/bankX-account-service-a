package com.itgirl.account_service_a.service;

import com.itgirl.account_service_a.model.Account;
import com.itgirl.account_service_a.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new com.itgirl.account_service_a.exception.AccountNotFoundException(accountId));
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
}
