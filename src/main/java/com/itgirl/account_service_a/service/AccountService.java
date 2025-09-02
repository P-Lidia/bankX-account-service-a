package com.itgirl.account_service_a.service;

import com.itgirl.account_service_a.exception.UnsufficientBalanceException;
import com.itgirl.account_service_a.model.Account;
import com.itgirl.account_service_a.model.AccountHistory;
import com.itgirl.account_service_a.model.OperationType;
import com.itgirl.account_service_a.model.Status;
import com.itgirl.account_service_a.repository.AccountHistoryRepository;
import com.itgirl.account_service_a.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepo;
    private final AccountHistoryRepository historyRepo;

    @Transactional
    public void reserve(Long accountId, BigDecimal amount, String currency, UUID transferId) {
        Account acc = accountRepo.findById(accountId).orElseThrow(() ->new com.itgirl.account_service_a.exception.AccountNotFoundException(accountId));;
        if (!acc.getCurrency().equalsIgnoreCase(currency)) throw new IllegalArgumentException("Currency mismatch");
        if (historyRepo.existsByAccountIdAndTransferIdAndType(accountId, transferId, OperationType.RESERVE)) return;
        if (acc.getStatus() != Status.ACTIVE) throw new IllegalStateException("Account not ACTIVE");
        if (acc.getBalance().compareTo(amount) < 0) throw new UnsufficientBalanceException();
        acc.setBalance(acc.getBalance().subtract(amount));
        accountRepo.saveAndFlush(acc);

        AccountHistory hist = new AccountHistory();
        hist.setAccountId(accountId);
        hist.setType(OperationType.RESERVE);
        hist.setAmount(amount);
        hist.setCurrency(currency);
        hist.setTransferId(transferId);
        historyRepo.save(hist);
    }

    @Transactional
    public void commit(Long accountId, UUID transferId) {
        if (historyRepo.existsByAccountIdAndTransferIdAndType(accountId, transferId, OperationType.COMMIT)) return;
        historyRepo.save(buildHistory(accountId, OperationType.COMMIT, BigDecimal.ZERO, "N/A", transferId));
    }

    @Transactional
    public void release(Long accountId, UUID transferId) {
        // find reserve by transferId & accountId
        List<AccountHistory> byTransfer = historyRepo.findByTransferId(transferId);
        Optional<AccountHistory> reserve = byTransfer.stream()
                .filter(h -> h.getAccountId().equals(accountId) && h.getType() == OperationType.RESERVE)
                .findFirst();
        if (reserve.isEmpty()) return;
        Account acc = accountRepo.findById(accountId).orElseThrow(() -> new com.itgirl.account_service_a.exception.AccountNotFoundException(accountId));
        acc.setBalance(acc.getBalance().add(reserve.get().getAmount()));
        accountRepo.saveAndFlush(acc);
        historyRepo.save(buildHistory(accountId, OperationType.RELEASE, reserve.get().getAmount(), reserve.get().getCurrency(), transferId));
    }

    private AccountHistory buildHistory(Long accountId, OperationType type, BigDecimal amount, String currency, UUID transferId) {
        AccountHistory h = new AccountHistory();
        h.setAccountId(accountId);
        h.setType(type);
        h.setAmount(amount);
        h.setCurrency(currency);
        h.setTransferId(transferId);
        return h;
    }

    public Map<String, Object> getAccountWithHistory(Long accountId) {
        Account acc = accountRepo.findById(accountId).orElseThrow(() -> new com.itgirl.account_service_a.exception.AccountNotFoundException(accountId));
        List<AccountHistory> hist = historyRepo.findByAccountIdOrderByCreatedAtDesc(accountId);
        return Map.of("account", acc, "history", hist.stream().map(h -> /* map to DTO using mapper */ h).collect(Collectors.toList()));
    }
}

