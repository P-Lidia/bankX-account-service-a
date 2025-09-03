package com.itgirl.account_service_a.service;

import com.itgirl.account_service_a.model.Account;
import com.itgirl.account_service_a.model.AccountHistory;
import com.itgirl.account_service_a.model.OperationType;
import com.itgirl.account_service_a.repository.AccountHistoryRepository;
import com.itgirl.account_service_a.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InternalAccountService {

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    @Transactional
    public AccountHistory reserveFunds(Long accountId, BigDecimal amount) { //исправить тип accountId, если поменяем на UUID
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds for reservation");
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

    @Transactional
    public AccountHistory releaseFunds(Long accountId, BigDecimal amount, UUID transferId) { //исправить тип accountId, если поменяем на UUID
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found: " + accountId));

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

    @Transactional
    public AccountHistory[] completeTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount) { //исправить тип accountId, если поменяем на UUID
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found: " + fromAccountId));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found: " + fromAccountId));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds for transfer");
        }

        UUID transferId = UUID.randomUUID();

        // DEBIT с отправителя
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);
        AccountHistory debitHistory = AccountHistory.builder()
                .accountId(fromAccount.getId())
                .type(OperationType.DEBIT)
                .amount(amount)
                .currency(fromAccount.getCurrency())
                .transferId(transferId)
                .build();
       accountHistoryRepository.save(debitHistory);

       // CREDIT получателю
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);
        AccountHistory creditHistory = AccountHistory.builder()
                .accountId(toAccount.getId())
                .type(OperationType.CREDIT)
                .amount(amount)
                .currency(toAccount.getCurrency())
                .transferId(transferId)
                .build();
        accountHistoryRepository.save(creditHistory);

        return new AccountHistory[]{debitHistory, creditHistory};
    }
}
