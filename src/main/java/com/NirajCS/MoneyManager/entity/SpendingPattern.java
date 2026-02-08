package com.NirajCS.MoneyManager.entity;

import java.util.Map;
import java.time.LocalDateTime;

public class SpendingPattern {
    private double totalIncome;
    private double totalExpense;
    private double savingsRate;
    private Map<String, Double> categoryBreakdown;
    private Map<LocalDateTime, Double> expenseTrends;
    private LocalDateTime timestamp;

    public SpendingPattern(double totalIncome, double totalExpense, double savingsRate, 
                           Map<String, Double> categoryBreakdown, Map<LocalDateTime, Double> expenseTrends) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.savingsRate = savingsRate;
        this.categoryBreakdown = categoryBreakdown;
        this.expenseTrends = expenseTrends;
        this.timestamp = LocalDateTime.now(); // set timestamp to current time
    }

    // Getters and Setters
    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double totalIncome) { this.totalIncome = totalIncome; }

    public double getTotalExpense() { return totalExpense; }
    public void setTotalExpense(double totalExpense) { this.totalExpense = totalExpense; }

    public double getSavingsRate() { return savingsRate; }
    public void setSavingsRate(double savingsRate) { this.savingsRate = savingsRate; }

    public Map<String, Double> getCategoryBreakdown() { return categoryBreakdown; }
    public void setCategoryBreakdown(Map<String, Double> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }

    public Map<LocalDateTime, Double> getExpenseTrends() { return expenseTrends; }
    public void setExpenseTrends(Map<LocalDateTime, Double> expenseTrends) { this.expenseTrends = expenseTrends; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}