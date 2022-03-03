package com.example.alpha_bank_t.code.dbEntityes;

import javax.persistence.*;

@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "sale")
    private double sale;

    @Column(name = "buy")
    private double buy;


    public Currency() {
    }

    public Currency(String currency, double sale, double buy) {
        this.currency = currency;
        this.sale = sale;
        this.buy = buy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }
}
