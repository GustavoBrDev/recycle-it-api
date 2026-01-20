package com.ifsc.ctds.stinghen.recycle_it_api.enums;

public enum GoalDifficult {
    normal,
    extraJob,
    difficult,
    hard;

    public String getValue() {
        return this.name();
    }
}
