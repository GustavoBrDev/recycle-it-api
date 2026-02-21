package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.Goal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * DTO para retorno das metas do usuário
 */

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class GoalResponseDTO implements ResponseDTO {

    public Long id;

    public Float progress;

    public GoalDifficult difficult;

    public GoalFrequency frequency;

    public LocalDate nextCheck;

    public Float multiplier;

    public GoalResponseDTO(Goal goal) {
        this.id = goal.getId();
        this.progress = goal.getProgress();
        this.difficult = goal.getDifficult();
        this.frequency = goal.getFrequency();
        this.nextCheck = goal.getNextCheck();
        this.multiplier = goal.getMultiplier();
    }
}
