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
public class CreateAccountDTO {

    @NotNull(message = "userId cannot be null")
    private Long userId;

    @NotNull(message = "accountNumber cannot be blank")
    private String accountNumber;

    @NotBlank(message = "currency cannot be blank")
    @Size(min = 3, max = 3, message = "currency must be 3 letters")
    private String currency;

    @NotNull(message = "balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "balance must be non-negative")
    private BigDecimal balance;
}
