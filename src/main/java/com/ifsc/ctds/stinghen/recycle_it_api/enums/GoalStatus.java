package com.ifsc.ctds.stinghen.recycle_it_api.enums;

public enum GoalStatus {
    ACTUAL,
    NEXT,
    INACTIVE;

    public String getValue() {
        return this.name();
    }
}
