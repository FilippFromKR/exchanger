package com.example.alpha_bank_t.code.enums;

public enum Status {
    ACTIVE(" ACTIVE"),
    COMPLETED(" COMPLETED");
    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
