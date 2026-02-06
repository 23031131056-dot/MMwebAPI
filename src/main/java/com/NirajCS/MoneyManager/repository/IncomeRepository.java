package com.NirajCS.MoneyManager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.NirajCS.MoneyManager.entity.IncomeEntity;

public interface IncomeRepository extends JpaRepository<IncomeEntity ,Long> {

    //select * from tbl_incomes where profileId = ? order by date desc
    @Query("SELECT i FROM IncomeEntity i WHERE i.profile.id = :profileId ORDER BY i.date DESC")
    List<IncomeEntity> findByProfileIdOrderByDateDescQuery(@Param("profileId") Long profileId);

    //select * from tbl_incomes where profileId = ? order by date desc limit 5
    @Query("SELECT i FROM IncomeEntity i WHERE i.profile.id = :profileId ORDER BY i.date DESC LIMIT 5")
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDescQuery(@Param("profileId") Long profileId);

    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId );

     //select * from tbl_incomes where profileId = ? and date between ?and ? and name like %?%
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate,
        String keyWord,
        Sort sort

    );
    //select * from tbl_incomes where profileId = ? and date between ?and ? 
    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);
} 