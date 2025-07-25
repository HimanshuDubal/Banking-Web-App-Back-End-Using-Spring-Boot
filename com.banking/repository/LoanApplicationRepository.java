package com.banking.repository;

import com.banking.model.LoanApplication;
import com.banking.model.LoanStatus;
import com.banking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByUserOrderByCreatedAtDesc(User user);
    List<LoanApplication> findByStatusOrderByCreatedAtDesc(LoanStatus status);
    List<LoanApplication> findAllByOrderByCreatedAtDesc();
}