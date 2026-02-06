package com.NirajCS.MoneyManager.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.NirajCS.MoneyManager.dto.ExpenseDTO;
import com.NirajCS.MoneyManager.entity.CategoryEntity;
import com.NirajCS.MoneyManager.entity.ExpenseEntity;
import com.NirajCS.MoneyManager.entity.ProfileEntity;
import com.NirajCS.MoneyManager.repository.CategoryRepository;
import com.NirajCS.MoneyManager.repository.ExpenseRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Service
@AllArgsConstructor

public class ExpenseServices {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileServices profileServices;


    public ExpenseDTO addExpense(ExpenseDTO dto){
        ProfileEntity profile = profileServices.getCurrentProfile();
        Long categoryId = dto.getCategoryId();
        if (categoryId == null) {
            throw new RuntimeException("Category ID cannot be null");
        }
        CategoryEntity category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found") );
        ExpenseEntity newExpense =toEntity(dto, profile, category);
        newExpense =expenseRepository.save(newExpense);
        return toDTO(newExpense);

    }
    //notification
    public List<ExpenseDTO> getExpenseForUserOnDate(Long profileId ,LocalDate date){
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDate( profileId, date);
        return list.stream().map(this::toDTO).toList();
    }
    //filter expenses based on start date , end date and keyword
    public List<ExpenseDTO> filterExpense(LocalDate startDate, LocalDate endDate,String keyword,Sort sort){
        ProfileEntity profile = profileServices.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword ,sort);
        return list.stream().map(this::toDTO).toList();
    }
// Retrive expenses for current month/based on the start date and end date.
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile = profileServices.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return list.stream().map(this::toDTO).toList();
    }
    //delete expense by id for current user
    public void deleteExpense(Long expenseId){
         ProfileEntity profile = profileServices.getCurrentProfile();
         ExpenseEntity entity = expenseRepository.findById(expenseId)
            .orElseThrow(()-> new RuntimeException("Expense not found"));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("UnAuthorized to delete this expense");
        }
        expenseRepository.delete(entity);
    }
    //Get Latest 5 expenses for current user
    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
        ProfileEntity profile = profileServices.getCurrentProfile();
        List<ExpenseEntity> list= expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    //Get total expense for current user
    public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile = profileServices.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total !=null ? total :BigDecimal.ZERO;
    }
    
    //helper method
    private ExpenseEntity toEntity(ExpenseDTO dto,ProfileEntity profile,CategoryEntity category){
        return ExpenseEntity.builder()
        .name(dto.getName())
        .icon(dto.getIcon())
        .amount(dto.getAmount())
        .date(dto.getDate())
        .profile(profile)
        .category(category)
        .build();
    }

    private ExpenseDTO toDTO(ExpenseEntity entity){
        return ExpenseDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .icon(entity.getIcon())
            .date(entity.getDate())
            .amount(entity.getAmount())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .categoryId(entity.getCategory()!= null ? entity.getCategory().getId():null)
            .categoryName(entity.getCategory()!= null ? entity.getCategory().getName():"N/A")
            .build();

    }

}
