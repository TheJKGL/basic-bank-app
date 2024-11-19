package com.bank.application.controllers;

import com.bank.application.models.dto.DepositRequest;
import com.bank.application.models.dto.TransactionResponse;
import com.bank.application.models.dto.TransferRequest;
import com.bank.application.models.dto.WithdrawRequest;
import com.bank.application.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bank.application.utils.GlobalConstants.DEPOSIT_SUCCESSFUL;
import static com.bank.application.utils.GlobalConstants.TRANSFER_SUCCESSFUL;
import static com.bank.application.utils.GlobalConstants.WITHDRAWAL_SUCCESSFUL;

@Tag(name = "Transaction Controller", description = "The Transaction API")
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Perform deposit", description = "Perform deposit of fund to account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = DEPOSIT_SUCCESSFUL)
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.deposit(request));
    }

    @Operation(summary = "Perform withdraw", description = "Perform withdraw of fund frm account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = WITHDRAWAL_SUCCESSFUL)
    })
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(request));
    }

    @Operation(summary = "Perform transfer", description = "Perform transfer of fund from one to another account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = TRANSFER_SUCCESSFUL)
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }
}