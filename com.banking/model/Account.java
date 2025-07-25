package com.banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0")
    private BigDecimal interestRate;

    @DecimalMin(value = "0.0")
    private BigDecimal overdraftLimit = BigDecimal.ZERO;

    private boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastTransactionAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> incomingTransactions;

    // Constructors
    public Account() {}

    public Account(String accountNumber, AccountType accountType, User user, BigDecimal interestRate) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.user = user;
        this.interestRate = interestRate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(BigDecimal overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastTransactionAt() { return lastTransactionAt; }
    public void setLastTransactionAt(LocalDateTime lastTransactionAt) { this.lastTransactionAt = lastTransactionAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<Transaction> getOutgoingTransactions() { return outgoingTransactions; }
    public void setOutgoingTransactions(Set<Transaction> outgoingTransactions) { this.outgoingTransactions = outgoingTransactions; }

    public Set<Transaction> getIncomingTransactions() { return incomingTransactions; }
    public void setIncomingTransactions(Set<Transaction> incomingTransactions) { this.incomingTransactions = incomingTransactions; }
}