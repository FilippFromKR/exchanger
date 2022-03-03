package com.example.alpha_bank_t.code.dbEntityes;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Operation")
public class Operation {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "currencyToBuy")
    private String currencyToBuy;

    @Column(name = "currencyToSell")
    private String currencyToSell;

    @Column(name = "amount")
    private double amount;

    @Column(name = "customerPhoneNumber", length = 15, nullable = false)
    private String customerPhoneNumber;

    @Column(name = "customerName", length = 50, nullable = true)
    private String customerName;

    @Column(name = "customerSurName", length = 50, nullable = true)
    private String customerSurName;

    @Column(name = "status")
    private String status;

    @Column(name = "activationCode")
    private String activationCode;

    @Column(name = "dateOfOperation")
    private Date dateOfOperation;

    @Column(name = "issued")
    private double moneyToIssued;

    @Transient
    private Set<String> exceptionMessage;


    public Operation() {
    }

    public Operation(Set<String> exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public Set<String> getExceptionMessage() {
        return exceptionMessage;
    }

    public void addErrorMessage(String message) {
        if (exceptionMessage == null)
            exceptionMessage = new HashSet<>();
        exceptionMessage.add(message);
    }

    public void setExceptionMessage(Set<String> exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrencyToBuy() {
        return currencyToBuy;
    }

    public void setCurrencyToBuy(String currencyToBuy) {
        this.currencyToBuy = currencyToBuy;
    }

    public String getCurrencyToSell() {
        return currencyToSell;
    }

    public void setCurrencyToSell(String currencyToSell) {
        this.currencyToSell = currencyToSell;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @NonNull
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(@NonNull String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public double getMoneyToIssued() {
        return moneyToIssued;
    }

    public void setMoneyToIssued(double moneyToIssued) {
        this.moneyToIssued = moneyToIssued;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurName() {
        return customerSurName;
    }

    public void setCustomerSurName(String customerSurName) {
        this.customerSurName = customerSurName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Date getDateOfOperation() {
        return dateOfOperation;
    }

    public void setDateOfOperation(Date dateOfOperation) {
        this.dateOfOperation = dateOfOperation;
    }
}
