package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.RecycleGoal;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO para retorno da meta de reciclagem do usuário
 */
@AllArgsConstructor
@SuperBuilder
public class RecycleGoalResponseDTO extends GoalResponseDTO{

    public RecycleGoalResponseDTO (RecycleGoal goal ){
        super(goal);
    }

}
