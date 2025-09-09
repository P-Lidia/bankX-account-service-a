package com.itgirl.account_service_a.controller;

import com.itgirl.account_service_a.dto.CreateAccountDTO;
import com.itgirl.account_service_a.model.Account;
import com.itgirl.account_service_a.model.AccountHistory;
import com.itgirl.account_service_a.model.AccountStatus;
import com.itgirl.account_service_a.repository.AccountHistoryRepository;
import com.itgirl.account_service_a.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountHistoryRepository accountHistoryRepository;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID accountId) {
        Account account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(account.getBalance());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{accountId}/history")
    public ResponseEntity<List<AccountHistory>> getAccountHistory(@PathVariable UUID accountId) {
        Account account = accountService.getAccountById(accountId);
        List<AccountHistory> history = accountHistoryRepository.findByAccountId(account.getId());
        return ResponseEntity.ok(history);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDTO dto) {
        Account account = Account.builder()
                .userId(dto.getUserId())
                .accountNumber(dto.getAccountNumber())
                .currency(dto.getCurrency())
                .balance(dto.getBalance())
                .Status(AccountStatus.ACTIVE)
                .build();
        Account created = accountService.createAccount(account);

        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{accountId}/close")
    public ResponseEntity<Account> closeAccount(@PathVariable UUID accountId) {
        Account closed = accountService.closeAccount(accountId);
        return ResponseEntity.ok(closed);
    }
}
