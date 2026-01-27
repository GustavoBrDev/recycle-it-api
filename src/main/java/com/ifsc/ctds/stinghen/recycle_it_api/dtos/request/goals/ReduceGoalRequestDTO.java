package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;

import java.util.List;

/**
 * DTO concreta para a meta de redução
 */
public class ReduceGoalRequestDTO extends GoalRequestDTO<ReduceGoal> {

    public List<ReduceItemRequestDTO> estimatedItems;

    @Override
    public ReduceGoal convert() {
        return ReduceGoal.builder()
                .difficult(super.goalDifficult)
                .items(estimatedItems.stream().map(ReduceItemRequestDTO::convert).toList())
                .frequency(super.goalFrequency)
                .build();
    }
}
