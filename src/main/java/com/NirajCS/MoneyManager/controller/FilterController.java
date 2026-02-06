package com.NirajCS.MoneyManager.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NirajCS.MoneyManager.dto.ExpenseDTO;
import com.NirajCS.MoneyManager.dto.FilterDTO;
import com.NirajCS.MoneyManager.dto.IncomeDTO;
import com.NirajCS.MoneyManager.services.ExpenseServices;
import com.NirajCS.MoneyManager.services.IncomeServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor

public class FilterController {
    private final ExpenseServices expenseServices;
    private final IncomeServices incomeServices;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filter){
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField= filter.getSortField() != null ? filter.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        if("expense".equals(filter.getType())){
            List<ExpenseDTO> expenses = expenseServices.filterExpense(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expenses);
        } else if("income".equalsIgnoreCase(filter.getType())){
            List<IncomeDTO> incomes = incomeServices.filterIncome(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(incomes);
        }else{
            return ResponseEntity.badRequest().body("Invalid type. Must be 'expense' or 'income'.");
        }

       
    }

}
