package com.banking.controller;

import com.banking.dto.TransactionDto;
import com.banking.model.Transaction;
import com.banking.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transferFunds(@Valid @RequestBody TransactionDto transactionDto) {
        try {
            Transaction transaction = transactionService.processTransfer(transactionDto);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody Map<String, Object> depositRequest) {
        try {
            String accountNumber = (String) depositRequest.get("accountNumber");
            BigDecimal amount = new BigDecimal(depositRequest.get("amount").toString());
            String description = (String) depositRequest.get("description");
            
            Transaction transaction = transactionService.processDeposit(accountNumber, amount, description);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String, Object> withdrawRequest) {
        try {
            String accountNumber = (String) withdrawRequest.get("accountNumber");
            BigDecimal amount = new BigDecimal(withdrawRequest.get("amount").toString());
            String description = (String) withdrawRequest.get("description");
            
            Transaction transaction = transactionService.processWithdrawal(accountNumber, amount, description);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<Page<Transaction>> getAccountTransactions(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionService.getAccountTransactions(accountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }
}