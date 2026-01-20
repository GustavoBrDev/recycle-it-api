package com.ifsc.ctds.stinghen.recycle_it_api.models.goals;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class RecycleGoal extends Goal {
}
