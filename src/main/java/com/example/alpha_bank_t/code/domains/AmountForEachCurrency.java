package com.example.alpha_bank_t.code.domains;

import com.example.alpha_bank_t.code.dbEntityes.Operation;
import com.example.alpha_bank_t.code.enums.UsedCurrencies;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmountForEachCurrency {

    class Currency {
        private String currency;
        private double sumToSale;
        private double sumToBuy;

        public Currency(String currency) {
            this.currency = currency;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getSumToSale() {
            return sumToSale;
        }

        public void setSumToSale(double sumToSale) {
            this.sumToSale = sumToSale;
        }

        public double getSumToBuy() {
            return sumToBuy;
        }

        public void setSumToBuy(double sumToBuy) {
            this.sumToBuy = sumToBuy;
        }
    }

    private Set<Currency> currencies;

    private AmountForEachCurrency() {
        UsedCurrencies[] enumCurrencies = UsedCurrencies.values();
        this.currencies = new HashSet<>();
        for (UsedCurrencies currenciesName : enumCurrencies) {
            currencies.add(new Currency(currenciesName.getCurrency()));
        }
    }

    private void setCurrencyToSell(String currency, double result) {
        currencies.forEach(object -> {
            if (object.getCurrency().equals(currency))
                object.setSumToSale(result);
        });
    }

    private void setCurrencyToBuy(String currency, double result) {
        currencies.forEach(object -> {
            if (object.getCurrency().equals(currency))
                object.setSumToBuy(result);
        });

    }

    public static Set<Currency> counterSum(List<Operation> operations) {
        AmountForEachCurrency eachCurrency = new AmountForEachCurrency();
        for (UsedCurrencies usedCurrencies : UsedCurrencies.values()) {
            double sumToSale = 0;
            double sumToBuy = 0;
            for (Operation operation : operations) {
                if (operation.getCurrencyToBuy().equals(usedCurrencies.getCurrency()))
                    sumToBuy += operation.getMoneyToIssued();
                if (operation.getCurrencyToSell().equals(usedCurrencies.getCurrency()))
                    sumToSale += operation.getAmount();
                eachCurrency.setCurrencyToBuy(usedCurrencies.getCurrency(), sumToBuy);
                eachCurrency.setCurrencyToSell(usedCurrencies.getCurrency(), sumToSale);
            }
        }
        return eachCurrency.getCurrencies();
    }


    private Set<Currency> getCurrencies() {
        return currencies;
    }
}
