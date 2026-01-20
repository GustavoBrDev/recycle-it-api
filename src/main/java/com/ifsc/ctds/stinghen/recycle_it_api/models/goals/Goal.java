package com.ifsc.ctds.stinghen.recycle_it_api.models.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Goal implements IGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float progress;

    private GoalDifficult difficult;

    private GoalFrequency frequency;

    private LocalDate nextCheck;

    private Float multiplier;
}
