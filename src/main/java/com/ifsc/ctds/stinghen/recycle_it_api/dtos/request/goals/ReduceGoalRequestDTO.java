package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;

import java.util.List;

/**
 * DTO concreta para a meta de redução
 */
public class ReduceGoalRequestDTO extends GoalRequestDTO<ReduceGoal> {

    public List<ReduceItemRequestDTO> estimatedItems;

    @Override
    public ReduceGoal convert() {

        List<ReduceItem> items =  estimatedItems.stream().map(ReduceItemRequestDTO::convert).toList();

        return ReduceGoal.builder()
                .difficult(super.goalDifficult)
                .estimatedItems(items)
                .actualItems(items)
                .frequency(super.goalFrequency)
                .build();
    }
}
