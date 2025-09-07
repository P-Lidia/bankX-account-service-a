package com.itgirl.account_service_a.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountOperationDTO {
    @NotNull private UUID accountId;
    @NotNull @DecimalMin(value="0.0", inclusive=false) private BigDecimal amount;
    private UUID toAccountId;  // Для перевода
    private UUID transferId;   // Для release
}