package com.banking.service;

import com.banking.model.LoanApplication;
import com.banking.model.Transaction;
import com.banking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendTransactionNotification(User user, Transaction transaction, String type) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "TRANSACTION");
        notification.put("message", String.format("Your account has been %s with ₹%s", type, transaction.getAmount()));
        notification.put("transactionId", transaction.getTransactionId());
        notification.put("timestamp", transaction.getProcessedAt());
        
        messagingTemplate.convertAndSendToUser(
            user.getUsername(), 
            "/queue/notifications", 
            notification
        );
    }
    
    public void sendLoanApplicationNotification(User user, LoanApplication application) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "LOAN_APPLICATION");
        notification.put("message", "Your loan application has been submitted successfully");
        notification.put("applicationNumber", application.getApplicationNumber());
        notification.put("timestamp", application.getCreatedAt());
        
        messagingTemplate.convertAndSendToUser(
            user.getUsername(), 
            "/queue/notifications", 
            notification
        );
    }
    
    public void sendLoanStatusNotification(User user, LoanApplication application) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "LOAN_STATUS");
        notification.put("message", String.format("Your loan application %s has been %s", 
            application.getApplicationNumber(), application.getStatus().toString().toLowerCase()));
        notification.put("applicationNumber", application.getApplicationNumber());
        notification.put("timestamp", application.getReviewedAt());
        
        messagingTemplate.convertAndSendToUser(
            user.getUsername(), 
            "/queue/notifications", 
            notification
        );
    }
}