package com.example.alpha_bank_t.code.staticClasses;

import com.example.alpha_bank_t.code.dbEntityes.Currency;
import com.example.alpha_bank_t.code.dbEntityes.Operation;
import com.example.alpha_bank_t.code.enums.UsedCurrencies;

import java.util.List;

public class CalculatorCourse {
    private double amount;
    private String targetCurrency;
    private double courseOfBankSaleCurrency;
    private String saleCurrency;
    private double courseOfBankBuyCurrency;
    private List<Currency> courseList;

    public CalculatorCourse(Operation operation, List<Currency> courseList) {
        this.amount = operation.getAmount();
        this.targetCurrency = operation.getCurrencyToBuy();
        this.saleCurrency = operation.getCurrencyToSell();
        this.courseList = courseList;
    }

    private boolean setCourses() {
        if (courseList.size() == 0)
            return false;
        if (targetCurrency.equals(UsedCurrencies.UAH.getCurrency()))
            courseOfBankSaleCurrency = 1.0;
        else
            courseOfBankSaleCurrency = courseList.stream().filter(value -> value.getCurrency().equals(targetCurrency)).findFirst().get().getSale();
        if (saleCurrency.equals(UsedCurrencies.UAH.getCurrency()))
            courseOfBankBuyCurrency = 1.0;
        else
            courseOfBankBuyCurrency = courseList.stream().filter(value -> value.getCurrency().equals(saleCurrency)).findFirst().get().getBuy();
        return true;
    }


    public double calculateAmountOfMoneyToIssued() {
        if (setCourses())
            return (courseOfBankBuyCurrency / courseOfBankSaleCurrency) * amount;
        return 0;
    }

}
