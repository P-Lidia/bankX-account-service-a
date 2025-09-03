package com.itgirl.account_service_a.mapper;


import com.itgirl.account_service_a.dto.AccountDTO;
import com.itgirl.account_service_a.dto.AccountHistoryDTO;
import com.itgirl.account_service_a.model.Account;
import com.itgirl.account_service_a.model.AccountHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO accountToDto(Account account);
    AccountHistoryDTO historyToDto(AccountHistory history);
}
