package com.itgirl.account_service_a.repository;

import com.itgirl.account_service_a.model.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long>, JpaSpecificationExecutor<AccountHistory> {
}
