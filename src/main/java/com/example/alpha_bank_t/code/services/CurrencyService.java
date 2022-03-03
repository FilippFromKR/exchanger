package com.example.alpha_bank_t.code.services;

import com.example.alpha_bank_t.code.dbEntityes.Currency;
import com.example.alpha_bank_t.code.domains.CurrantCoursePB;
import com.example.alpha_bank_t.code.repositoryes.CurrencyRepository;
import com.example.alpha_bank_t.code.enums.UsedCurrencies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CurrencyService {

    CurrencyRepository currencyRepository;

    private List<CurrantCoursePB> listOfCourses;

    @Autowired
    private void setCurrencyRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }


    public boolean contains(Currency currency) {
        return currencyRepository.findByCurrency(currency) != null;
    }

    public boolean saveIfAbsent(Currency currency) {
        if (!contains(currency)) {
            currencyRepository.save(currency);
            return true;
        }
        return false;
    }

    public List<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }

    public Set<Currency> setListOfCourses(CurrantCoursePB[] currantCourses) {
        listOfCourses = List.of(currantCourses);
        Set<Currency> currencies = new HashSet<>();
        for (UsedCurrencies usedCurrencies : UsedCurrencies.values())
            if (!usedCurrencies.getCurrency().equals(UsedCurrencies.UAH.getCurrency())) {
                CurrantCoursePB coursePB = listOfCourses.stream().filter(result ->
                        result.getCcy().equals(usedCurrencies.getCurrency())).findFirst().get();
                Currency currency = new Currency(usedCurrencies.getCurrency(), coursePB.getSale(), coursePB.getBuy());
                currencies.add(currency);
            }
        return currencies;
    }


}
