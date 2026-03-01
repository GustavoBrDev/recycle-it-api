package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * DTO de resposta concreta para as metas de redução
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReduceGoalResponseDTO extends GoalResponseDTO {

    public List<ReduceItemResponseDTO> items;

    public int skipDaysLeft;

    public ReduceGoalResponseDTO(ReduceGoal goal) {
        super(goal);
        this.skipDaysLeft = goal.getSkipDaysLeft();
        
        this.items = goal.getItems() != null
            ? goal.getItems().stream().map(ReduceItemResponseDTO::new).toList()
            : List.of();
    }
}
