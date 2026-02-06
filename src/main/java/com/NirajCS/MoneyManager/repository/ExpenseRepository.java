package com.NirajCS.MoneyManager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.NirajCS.MoneyManager.entity.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    @Query("SELECT e FROM ExpenseEntity e WHERE e.profile.id = :profileId ORDER BY e.date DESC")
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(@Param("profileId") Long profileId);

    //select * from tbl_expenses where profileId = ? order by date desc limit 5
    @Query("SELECT e FROM ExpenseEntity e WHERE e.profile.id = :profileId ORDER BY e.date DESC LIMIT 5")
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId );

     //select * from tbl_expenses where profileId = ? and date between ?and ? and name like %?%
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate,
        String keyWord, 
        Sort sort

    );
    //select * from tbl_expenses where profileId = ? and date between ?and ? 
    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);
    
    //select * from tbl_expenses where profileId = ? and date = ?
    List<ExpenseEntity> findByProfileIdAndDate(Long profileId , LocalDate date);
}
