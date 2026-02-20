package com.ifsc.ctds.stinghen.recycle_it_api.enums;

public enum Materials {
    plastic,
    glass,
    paper,
    metal,
    textile;

    public String getValue() {
        return this.name();
    }
}
