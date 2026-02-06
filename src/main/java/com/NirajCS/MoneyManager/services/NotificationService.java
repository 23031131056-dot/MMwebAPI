package com.NirajCS.MoneyManager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.NirajCS.MoneyManager.dto.ExpenseDTO;
import com.NirajCS.MoneyManager.entity.ProfileEntity;
import com.NirajCS.MoneyManager.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final ExpenseServices expenseServices;
    private final EmailService emailService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 2 14 * * *") // Everyday at 2:02 pm (server time)
    public void sendDailyIncomeExpenseReminder(){
        log.info("job started :sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
            Long profileId = profile.getId();
            String email = profile.getEmail();
            List<?> todaysExpenses = expenseServices.getExpenseForUserOnDate(profileId, java.time.LocalDate.now());
            if(todaysExpenses.isEmpty()){
                String subject = "Reminder to log your expenses/incomes";
                String body = "Dear " + profile.getFullname() + ",\n\n"
                    + "We noticed that you haven't logged any expenses or incomes today. "
                    + "Keeping track of your finances is important for effective money management.\n\n"
                    + "<a href=\"" + frontendUrl + "\" style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Click here to Go to Expense tracker</a>  \n\n"
                    + "Best regards,\n"
                    + "Money Manager Team";
                emailService.sendEmail(email, subject, body);
                
            }
            log.info("Job completed : sendDailyIncomeExpenseReminder()");
        }
    }
    @Scheduled(cron = "0 0 23 1 * *") // At 11 PM on the first day of every month (server time)
    public void sendDailyExpenseSummary(){
        log.info("Job started : sendDailyExpenseSummary");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile : profiles){
          List<ExpenseDTO> todaysExpense = expenseServices.getExpenseForUserOnDate(profile.getId(), java.time.LocalDate.now().minusDays(1));
          if(!todaysExpense.isEmpty()){
            StringBuilder table = new StringBuilder();
            table.append("<table style='border-collapse: collapse; width: 100%;'>");
            table.append("<tr style=background-color:#f2f2f2;'><th style='border: 1px solid #ddd; padding: 8px;'>Sr.no</th><th style='border: 1px solid #ddd; padding: 8px;'>Name</th><th style='border: 1px solid #ddd; padding: 8px;'>Amount</th><th style='border: 1px solid #ddd; padding: 8px;'>Category</th></tr>");
            int i = 1;
            for(ExpenseDTO expense : todaysExpense){
                table.append("<tr>");
                table.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(i++).append("</td>");
                table.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(expense.getName()).append("</td>");
                table.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(expense.getAmount()).append("</td>");
                table.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(expense.getCategoryId() != null ? expense.getCategoryName() :"N/A").append("</td>");
                table.append("</tr>");
            }
            table.append("</table>");
            String body = "Hi" + profile.getFullname() + "<br/><br/> Here is summary of your expenses for today : <br/><br/>" + table + "<br/><br/> Best Regards . ";
            String email = profile.getEmail();
            String subject = "Your daily expense Summary";
            emailService.sendEmail(email, subject, body);
        }
        }
        log.info("Job completed :sendDailyExpenseSummary");
    }
}