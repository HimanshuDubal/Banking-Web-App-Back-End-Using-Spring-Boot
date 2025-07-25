package com.banking.controller;

import com.banking.dto.LoanApplicationDto;
import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;
import com.banking.model.User;
import com.banking.service.LoanService;
import com.banking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:4200")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;

    @PostMapping("/apply")
    public ResponseEntity<?> applyForLoan(@Valid @RequestBody LoanApplicationDto loanDto, 
                                        Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName()).orElseThrow();
            LoanApplication application = loanService.submitApplication(user, loanDto);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<LoanApplication>> getUserApplications(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        List<LoanApplication> applications = loanService.getUserApplications(user);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<LoanApplication>> getAllApplications() {
        List<LoanApplication> applications = loanService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{applicationId}/review")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> reviewApplication(@PathVariable Long applicationId,
                                             @RequestBody Map<String, String> reviewRequest) {
        try {
            LoanStatus status = LoanStatus.valueOf(reviewRequest.get("status"));
            String comments = reviewRequest.get("comments");
            
            LoanApplication application = loanService.reviewApplication(applicationId, status, comments);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}