package com.banking.dto;

import com.banking.model.AccountType;
import jakarta.validation.constraints.NotNull;

public class AccountCreationDto {
    @NotNull(message = "Account type is required")
    private AccountType accountType;

    // Constructors
    public AccountCreationDto() {}

    public AccountCreationDto(AccountType accountType) {
        this.accountType = accountType;
    }

    // Getters and Setters
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}