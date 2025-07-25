package com.banking.dto;

import com.banking.model.LoanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class LoanApplicationDto {
    @NotNull(message = "Loan type is required")
    private LoanType loanType;

    @NotNull(message = "Requested amount is required")
    @DecimalMin(value = "1000.0", message = "Minimum loan amount is ₹1000")
    private BigDecimal requestedAmount;

    @NotNull(message = "Term in months is required")
    @Min(value = 1, message = "Minimum term is 1 month")
    private Integer termInMonths;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    @NotNull(message = "Monthly income is required")
    @DecimalMin(value = "0.0", message = "Monthly income must be positive")
    private BigDecimal monthlyIncome;

    @DecimalMin(value = "0.0", message = "Existing debt must be positive")
    private BigDecimal existingDebt = BigDecimal.ZERO;

    // Constructors
    public LoanApplicationDto() {}

    // Getters and Setters
    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }

    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }

    public Integer getTermInMonths() { return termInMonths; }
    public void setTermInMonths(Integer termInMonths) { this.termInMonths = termInMonths; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public BigDecimal getExistingDebt() { return existingDebt; }
    public void setExistingDebt(BigDecimal existingDebt) { this.existingDebt = existingDebt; }
}