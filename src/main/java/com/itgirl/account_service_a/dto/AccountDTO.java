package com.itgirl.account_service_a.dto;

import com.itgirl.account_service_a.model.AccountStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor @Builder

public class AccountDTO {
    private Long id;
    private Long userId;
    private String currency;
    private BigDecimal balance;
    private AccountStatus Status;
    private Long version;
    private LocalDateTime createdAt;
}
