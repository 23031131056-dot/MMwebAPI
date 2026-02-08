package com.NirajCS.MoneyManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUserAndDismissedFalse(String user);
    List<Alert> findByUserOrderByCreatedAtDesc(String user);
    List<Alert> findByUserAndCreatedAtAfter(String user, LocalDateTime createdAt);
    List<Alert> findByUserAndCategory(String user, String category);
    long countByUserAndDismissedFalse(String user);
}