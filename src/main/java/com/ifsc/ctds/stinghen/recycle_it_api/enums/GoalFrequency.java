package com.ifsc.ctds.stinghen.recycle_it_api.enums;

public enum GoalFrequency {
    weekly,
    monthly,
    quarterly,
    yearly;

    public String getValue() {
        return this.name();
    }
}
