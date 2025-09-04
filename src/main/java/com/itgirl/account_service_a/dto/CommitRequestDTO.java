package com.itgirl.account_service_a.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommitRequestDTO {
    private Long id;
    private String currency;
    private BigDecimal amount;
    private UUID transferId;
}
