package com.example.alpha_bank_t.code.domains;

import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class DateFilter {
    private Date from;
    private Date to;
    private String currency;

    public DateFilter() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
