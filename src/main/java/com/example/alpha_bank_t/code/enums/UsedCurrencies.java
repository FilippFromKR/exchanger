package com.example.alpha_bank_t.code.enums;

/**
 * to expand the list of currencies used,
 * just add a new enum.
 * IMPORTANT- the field 'currency' should be equals field 'ccy' from pb json
 */
public enum UsedCurrencies {
    UAH("UAH"), USD("USD"), EURO("EUR");
    private final String currency;

    UsedCurrencies(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

}
