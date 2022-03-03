package com.example.alpha_bank_t.code.dbEntityes;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "course")
public class CurrantCourses {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    Set<Currency> currencies;

    @Temporal(TemporalType.DATE)
    @Column(name = "data")
    private Date date;

    public CurrantCourses() {
    }

    public Set<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Set<Currency> currencies) {
        this.currencies = currencies;
    }

    public void addCurrency(Currency currency) {
        currencies.add(currency);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}

