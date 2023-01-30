package com.pot.common.enums;

public enum PayrollFrequency
{
    WEEKLY(7),
    MONTHLY(30);
    
    private final int days;

    PayrollFrequency(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
