package com.itgirl.account_service_a.service;

import com.itgirl.account_service_a.exception.AccountNotFoundException;
import com.itgirl.account_service_a.model.Account;
import com.itgirl.account_service_a.model.AccountStatus;
import com.itgirl.account_service_a.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean hasRole(String role) {
        return getAuth().getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

     // Получение аккаунта: доступно USER (только свой) и ADMIN (любой)
    public Account getAccountById(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (hasRole("ADMIN")) {
            return account;
        }

        // USER может получить только свой аккаунт
        UUID currentUserId = (UUID) getAuth().getPrincipal();
        if (!account.getUserId().equals(currentUserId)) {
            throw new SecurityException("Access denied: user cannot access this account");
        }

        return account;
    }

    // Создание аккаунта: доступно USER (для себя) и ADMIN (для любого)
    public Account createAccount(Account account) {
        account.setStatus(AccountStatus.ACTIVE);

        if (hasRole("ADMIN")) {
            return accountRepository.save(account);
        }

        UUID currentUserId = (UUID) getAuth().getPrincipal();
        account.setUserId(currentUserId);
        return accountRepository.save(account);
    }

    // Закрытие аккаунта: только ADMIN
    public Account closeAccount(UUID accountId) {
        if (!hasRole("ADMIN")) {
            throw new SecurityException("Only ADMIN can close accounts");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.setStatus(AccountStatus.CLOSED);
        return accountRepository.save(account);
    }
}
