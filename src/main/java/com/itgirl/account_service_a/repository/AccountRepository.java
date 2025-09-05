package com.itgirl.account_service_a.repository;

import com.itgirl.account_service_a.model.Account;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUserIdAndCurrency(UUID userId, String currency);

    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a WHERE a.userId = :userId AND a.currency = :currency")
    BigDecimal totalByUserAndCurrency(@Param("userId") UUID userId, @Param("currency") String currency);

    Optional<Account> findByAccountNumber(String accountNumber);
}
