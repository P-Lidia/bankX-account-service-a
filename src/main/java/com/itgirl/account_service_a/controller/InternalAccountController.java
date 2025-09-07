package com.itgirl.account_service_a.controller;

import com.itgirl.account_service_a.dto.AccountOperationDTO;
import com.itgirl.account_service_a.model.AccountHistory;
import com.itgirl.account_service_a.service.InternalAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/internal/accounts")
@RequiredArgsConstructor
public class InternalAccountController {

    private final InternalAccountService internalAccountService;

    // Резервирование средств
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/reserve")
    public ResponseEntity<AccountHistory> reserveFunds(@Valid @RequestBody AccountOperationDTO dto) {
        return ResponseEntity.ok(
                internalAccountService.reserveFunds(dto.getAccountId(), dto.getAmount())
        );
    }

    // Подтверждение списания (commit)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/commit")
    public ResponseEntity<AccountHistory> commitFunds(@Valid @RequestBody AccountOperationDTO dto) {
        if (dto.getTransferId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                internalAccountService.commitFunds(dto.getAccountId(), dto.getAmount(), dto.getTransferId())
        );
    }

    // Release — освобождение зарезервированных средств
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/release")
    public ResponseEntity<AccountHistory> releaseFunds(@Valid @RequestBody AccountOperationDTO dto) {
        if (dto.getTransferId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                internalAccountService.releaseFunds(dto.getAccountId(), dto.getAmount(), dto.getTransferId())
        );
    }
}
