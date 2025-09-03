package com.itgirl.account_service_a.dto;

import com.itgirl.account_service_a.model.OperationType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AccountHistoryDTO {
    private Long id;
    private Long accountId;
    private OperationType type;
    private BigDecimal amount;
    private String currency;
    private UUID transferId;
    private LocalDateTime createdAt;
}