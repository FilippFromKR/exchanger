package com.example.alpha_bank_t.code.staticClasses;

import java.util.Objects;

public class SmsCreate {
    private final String numberOfRecipient;
    private String message;

    public SmsCreate(String numberOfRecipient) {
        this.numberOfRecipient = numberOfRecipient;
    }

    public void setSms(String uuCode) {
        this.message = String.format(
                "To activate your exchange,\n" +
                        "call this code to the operator: %s ", uuCode);

    }

    public String getNumberOfRecipient() {
        return numberOfRecipient;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsCreate smsCreate = (SmsCreate) o;
        return Objects.equals(numberOfRecipient, smsCreate.numberOfRecipient) && Objects.equals(message, smsCreate.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfRecipient, message);
    }
}
