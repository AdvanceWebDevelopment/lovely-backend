package com.hcmus.lovelybackend.constant;

public enum TransactionStatus {
    SUCCESS("Success"),
    REJECT("Reject");

    private final String status;

    TransactionStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
