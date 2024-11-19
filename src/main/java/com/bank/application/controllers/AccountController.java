package com.bank.application.controllers;

import com.bank.application.models.Account;
import com.bank.application.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.bank.application.utils.Utils.validateEmail;

@Tag(name = "Account Controller", description = "The Account API")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create new account", description = "Create account and return uuid of created entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created")
    })
    @PostMapping
    public ResponseEntity<String> createAccount(@RequestParam String email) {
        validateEmail(email);
        Account createdAccount = accountService.createAccount(email);
        return ResponseEntity.ok(createdAccount.getAccountNumber());
    }

    @Operation(summary = "Find account", description = "Get account by account number specified in path")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Account.class)),
                    description = "Account successfully found"),
            @ApiResponse(responseCode = "404",
                    content = @Content(),
                    description = "Not found - Account was not found")
    })
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> findAccountByNumber(@PathVariable String accountNumber) {
        Account account = accountService.findAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "List all account", description = "Find all available account from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Account.class))),
                    description = "All accounts successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<List<Account>> findAllAccounts() {
        List<Account> accounts = accountService.findAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}