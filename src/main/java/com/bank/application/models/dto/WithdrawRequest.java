package com.bank.application.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest {

    @NotBlank(message = "Account number is required")
    @Length(min = 16, max = 16)
    private String accountNumber;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount can't be negative")
    private BigDecimal amount;

    private String comment;
}