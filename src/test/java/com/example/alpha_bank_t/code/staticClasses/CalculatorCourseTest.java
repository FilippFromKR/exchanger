package com.example.alpha_bank_t.code.staticClasses;

import com.example.alpha_bank_t.code.dbEntityes.Currency;
import com.example.alpha_bank_t.code.dbEntityes.Operation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
class CalculatorCourseTest {

    @Test
    void calculateAmountOfMoneyToIssued() {
        Operation operation = new Operation();
        operation.setAmount(2.0);
        operation.setCurrencyToBuy("EUR");
        operation.setCurrencyToSell("USD");
        CalculatorCourse calculatorCourse =
                new CalculatorCourse(operation, List.of(new Currency("EUR", 1, 1),
                        new Currency("USD", 2.5, 2)));
        Assert.assertTrue(4.0 == calculatorCourse.calculateAmountOfMoneyToIssued());

    }
}