package com.ifsc.ctds.stinghen.recycle_it_api.models.goals;

public interface IItem {

    void increment( Long amount);
    void decrement( Long amount);
    float calculateProgress();
}
