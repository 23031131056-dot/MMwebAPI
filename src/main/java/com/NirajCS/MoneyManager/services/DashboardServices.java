package com.NirajCS.MoneyManager.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.NirajCS.MoneyManager.dto.IncomeDTO;
import com.NirajCS.MoneyManager.dto.RecentTransactionDTO;
import com.NirajCS.MoneyManager.dto.ExpenseDTO;
import org.springframework.stereotype.Service;
import com.NirajCS.MoneyManager.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServices {
    private final IncomeServices incomeServices;
    private final ExpenseServices expenseServices;
    private final ProfileServices profileServices;

    public Map<String,Object> getDashboardData(){
        ProfileEntity profile = profileServices.getCurrentProfile();
        Map<String,Object> returnValue = new LinkedHashMap<>();
        List<ExpenseDTO> latestExpenses = expenseServices.getLatest5ExpensesForCurrentUser();
        List<IncomeDTO> latestIncomes =incomeServices.getLatest5IncomesForCurrentUser();  
        List<RecentTransactionDTO> recentTransaction =  Stream.concat(
            latestIncomes.stream().map((IncomeDTO income) -> 
                RecentTransactionDTO.builder()
                    .id(income.getId())
                    .profileId(profile.getId())
                    .icon(income.getIcon())
                    .name(income.getName())
                    .amount(income.getAmount())
                    .date(income.getDate())
                    .createdAt(income.getCreatedAt())
                    .updatedAt(income.getUpdatedAt())
                    .type("income")
                    .build()
        ),
            latestExpenses.stream().map((ExpenseDTO expense) ->
                RecentTransactionDTO.builder()
                    .id(expense.getId())
                    .profileId(profile.getId())
                    .icon(expense.getIcon())
                    .name(expense.getName())
                    .amount(expense.getAmount())
                    .date(expense.getDate())
                    .createdAt(expense.getCreatedAt())
                    .updatedAt(expense.getUpdatedAt())
                    .type("expense")
                    .build()))
                .sorted((RecentTransactionDTO a, RecentTransactionDTO b)->{
                    int cmp = b.getDate().compareTo(a.getDate());
                    if(cmp ==0 && a.getCreatedAt()!=null && b.getCreatedAt()!=null){
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).collect(Collectors.toList());

                returnValue.put("totalBalance", incomeServices.getTotalIncomeForCurrentUser().subtract(expenseServices.getTotalExpenseForCurrentUser()));
                returnValue.put("totalIncome", incomeServices.getTotalIncomeForCurrentUser());
                returnValue.put("totalExpense", expenseServices.getTotalExpenseForCurrentUser());
                returnValue.put("latestIncomes", latestIncomes);
                returnValue.put("latestExpenses", latestExpenses);
                returnValue.put("recentTransactions", recentTransaction);
                return returnValue;
    }

}
