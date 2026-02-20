package com.ifsc.ctds.stinghen.recycle_it_api.enums;

public enum League {
    paper,
    glass,
    metal,
    plastic;

    public String getValue() {
        return this.name();
    }
}
