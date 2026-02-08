package com.NirajCS.MoneyManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpendingPatternRepository extends JpaRepository<SpendingPattern, Long> {
    List<SpendingPattern> findByUserAndMonth(String user, String month);
    List<SpendingPattern> findByUserOrderByMonthDesc(String user);
    List<SpendingPattern> findTop12ByUserOrderByMonthDesc(String user);
}