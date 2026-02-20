package com.ifsc.ctds.stinghen.recycle_it_api.models.goals;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ReduceGoal extends Goal {

    @OneToMany
    private List<ReduceItem> items;

    private int skipDaysLeft;
}
