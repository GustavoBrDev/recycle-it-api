package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.RecycleGoal;

/**
 * DTO concreta para a meta de reciclagem
 */
public class RecycleGoalRequestDTO extends GoalRequestDTO<RecycleGoal> {

    @Override
    public RecycleGoal convert() {
        return RecycleGoal.builder()
                .difficult(super.goalDifficult)
                .frequency(super.goalFrequency)
                .build();
    }
}
