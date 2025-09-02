package com.itgirl.account_service_a.repository;

import com.itgirl.account_service_a.model.AccountHistory;
import com.itgirl.account_service_a.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {
    List<AccountHistory> findByAccountIdOrderByCreatedAtDesc(Long accountId);
    boolean existsByAccountIdAndTransferIdAndType(Long accountId, UUID transferId, OperationType type);
    List<AccountHistory> findByTransferId(UUID transferId);
}
