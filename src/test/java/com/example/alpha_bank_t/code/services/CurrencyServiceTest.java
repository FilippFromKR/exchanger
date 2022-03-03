package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.dbEntityes.Currency;
import com.example.alpha_bank_t.code.domains.CurrantCoursePB;
import com.example.alpha_bank_t.code.repositoryes.CurrencyRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest
class CurrencyServiceTest {

    @MockBean
    private CurrencyRepository currencyRepository;
    private CurrencyService currencyService;

    @Autowired
    private void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Test
    void contains() {
        Currency currency = new Currency();

        Mockito.doReturn(currency)
                .when(currencyRepository)
                .findByCurrency(currency);
        Assert.assertTrue(currencyService.contains(currency));
    }

    @Test
    void saveIfAbsent() {
        Mockito.doReturn(new Currency())
                .when(currencyRepository)
                .findByCurrency(ArgumentMatchers.any());
        currencyService.saveIfAbsent(new Currency());
        Mockito.verify(currencyRepository, Mockito.times(0))
                .save(ArgumentMatchers.any());
    }

    @Test
    void setListOfCourses() {
        CurrantCoursePB[] coursePB = {new CurrantCoursePB("USD", "UAH", 1.0, 1.5),
                new CurrantCoursePB("EUR", "UAH", 1.5, 2.0)};
        Set<Currency> courses = currencyService.setListOfCourses(coursePB);
        Assert.assertNotNull(courses);
    }
}