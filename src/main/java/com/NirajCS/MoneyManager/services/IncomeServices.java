package com.NirajCS.MoneyManager.services;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.NirajCS.MoneyManager.dto.IncomeDTO;
import com.NirajCS.MoneyManager.entity.CategoryEntity;
import com.NirajCS.MoneyManager.entity.IncomeEntity;
import com.NirajCS.MoneyManager.entity.ProfileEntity;
import com.NirajCS.MoneyManager.repository.CategoryRepository;
import com.NirajCS.MoneyManager.repository.IncomeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@Data
@AllArgsConstructor
public class IncomeServices {

    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileServices profileServices;


     public IncomeDTO addIncome(IncomeDTO dto){
        ProfileEntity profile = profileServices.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found") );
        IncomeEntity newIncome =toEntity(dto, profile, category);
        newIncome =incomeRepository.save(newIncome);
        return toDTO(newIncome);

    }
    //filter incomes based on start date , end date and keyword
    public List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate,String keyword,Sort sort){
        ProfileEntity profile = profileServices.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword ,sort);
        return list.stream().map(this::toDTO).toList();
    }

    // Retrive incomes for current month/based on the start date and end date.
    public List<IncomeDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile = profileServices.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return list.stream().map(this::toDTO).toList();
    }
     //delete income by id for current user
    public void deleteIncome(Long incomeId){
         ProfileEntity profile = profileServices.getCurrentProfile();
         IncomeEntity entity = incomeRepository.findById(incomeId)
            .orElseThrow(()-> new RuntimeException("Expense not found"));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("UnAuthorized to delete this income");
        }
        incomeRepository.delete(entity);
    }
    //Get Latest 5 expenses for current user
    public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
        ProfileEntity profile = profileServices.getCurrentProfile();
        List<IncomeEntity> list= incomeRepository.findTop5ByProfileIdOrderByDateDescQuery(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    //Get total income for current user
    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profile = profileServices.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalExpenseByProfileId(profile.getId());
        return total !=null ? total :BigDecimal.ZERO;
    }
    //helper method
    private IncomeEntity toEntity(IncomeDTO dto,ProfileEntity profile,CategoryEntity category){
        return IncomeEntity.builder()
        .name(dto.getName())
        .icon(dto.getIcon())
        .amount(dto.getAmount())
        .date(dto.getDate())
        .profile(profile)
        .category(category)
        .build();
    }

    private IncomeDTO toDTO(IncomeEntity entity){
        return IncomeDTO.builder()
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
