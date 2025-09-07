package com.itgirl.account_service_a.mapper;


import com.itgirl.account_service_a.dto.AccountDTO;
import com.itgirl.account_service_a.dto.AccountHistoryDTO;
import com.itgirl.account_service_a.model.Account;
import com.itgirl.account_service_a.model.AccountHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    // Account <-> DTO
    AccountDTO accountToDto(Account account);
    Account dtoToAccount(AccountDTO dto);

    // AccountHistory <-> DTO
    AccountHistoryDTO historyToDto(AccountHistory history);
    AccountHistory dtoToHistory(AccountHistoryDTO dto);

    // Для коллекций
    List<AccountDTO> accountsToDto(List<Account> accounts);
    List<AccountHistoryDTO> historiesToDto(List<AccountHistory> histories);
}
