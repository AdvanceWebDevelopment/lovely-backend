package com.hcmus.lovelybackend.constant;

public enum TransactionType {
    BID("Bid"),
    SELL("Sell");

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
