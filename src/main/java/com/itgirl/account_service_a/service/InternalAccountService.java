package com.itgirl.account_service_a.service;

import com.itgirl.account_service_a.exception.AccountNotFoundException;
import com.itgirl.account_service_a.exception.UnsufficientBalanceException;
import com.itgirl.account_service_a.model.Account;
import com.itgirl.account_service_a.model.AccountHistory;
import com.itgirl.account_service_a.model.OperationType;
import com.itgirl.account_service_a.repository.AccountHistoryRepository;
import com.itgirl.account_service_a.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InternalAccountService {

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AccountHistory reserveFunds(UUID accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new UnsufficientBalanceException();
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        AccountHistory history = AccountHistory.builder()
                .accountId(account.getId())
                .type(OperationType.RESERVE)
                .amount(amount)
                .currency(account.getCurrency())
                .transferId(UUID.randomUUID())
                .build();

        return accountHistoryRepository.save(history);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AccountHistory releaseFunds(UUID accountId, BigDecimal amount, UUID transferId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        AccountHistory history = AccountHistory.builder()
                .accountId(account.getId())
                .type(OperationType.RELEASE)
                .amount(amount)
                .currency(account.getCurrency())
                .transferId(transferId)
                .build();

        return accountHistoryRepository.save(history);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AccountHistory commitFunds(UUID accountId, BigDecimal amount, UUID transferId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        AccountHistory history = AccountHistory.builder()
                .accountId(accountId)
                .type(OperationType.DEBIT)
                .amount(amount)
                .currency(account.getCurrency())
                .transferId(transferId)
                .build();

        return accountHistoryRepository.save(history);
    }
}
