package com.itgirl.account_service_a.dto;

import com.itgirl.account_service_a.model.AccountStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AccountDTO {
    private UUID id;
    private Long userId;
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private AccountStatus status;
    private Long version;
    private LocalDateTime createdAt;
}
