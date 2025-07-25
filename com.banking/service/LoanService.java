package com.banking.service;

import com.banking.dto.LoanApplicationDto;
import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;
import com.banking.model.User;
import com.banking.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LoanService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private NotificationService notificationService;

    public LoanApplication submitApplication(User user, LoanApplicationDto loanDto) {
        LoanApplication application = new LoanApplication();
        application.setApplicationNumber(generateApplicationNumber());
        application.setUser(user);
        application.setLoanType(loanDto.getLoanType());
        application.setRequestedAmount(loanDto.getRequestedAmount());
        application.setTermInMonths(loanDto.getTermInMonths());
        application.setPurpose(loanDto.getPurpose());
        application.setMonthlyIncome(loanDto.getMonthlyIncome());
        application.setExistingDebt(loanDto.getExistingDebt());
        application.setInterestRate(calculateInterestRate(loanDto.getLoanType(), loanDto.getRequestedAmount()));
        
        LoanApplication savedApplication = loanApplicationRepository.save(application);
        notificationService.sendLoanApplicationNotification(user, savedApplication);
        
        return savedApplication;
    }
    
    public List<LoanApplication> getUserApplications(User user) {
        return loanApplicationRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<LoanApplication> getAllApplications() {
        return loanApplicationRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public LoanApplication reviewApplication(Long applicationId, LoanStatus status, String comments) {
        LoanApplication application = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Loan application not found"));
        
        application.setStatus(status);
        application.setManagerComments(comments);
        application.setReviewedAt(LocalDateTime.now());
        
        LoanApplication savedApplication = loanApplicationRepository.save(application);
        notificationService.sendLoanStatusNotification(application.getUser(), savedApplication);
        
        return savedApplication;
    }
    
    private String generateApplicationNumber() {
        return "LOAN" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
    
    private BigDecimal calculateInterestRate(com.banking.model.LoanType loanType, BigDecimal amount) {
        BigDecimal baseRate = switch (loanType) {
            case PERSONAL -> new BigDecimal("12.5");
            case HOME -> new BigDecimal("8.5");
            case CAR -> new BigDecimal("10.0");
            case EDUCATION -> new BigDecimal("9.5");
            case BUSINESS -> new BigDecimal("11.0");
        };
        
        // Adjust rate based on amount (higher amounts get better rates)
        if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            baseRate = baseRate.subtract(new BigDecimal("0.5"));
        }
        
        return baseRate;
    }
}