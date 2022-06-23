package com.hcmus.lovelybackend.constant;

public enum FilterSearchConstant {
    DATE("endAt"),
    PRICE("currentPrice");

    private final String sortBy;

    FilterSearchConstant(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public String toString() {
        return this.sortBy;
    }
}
