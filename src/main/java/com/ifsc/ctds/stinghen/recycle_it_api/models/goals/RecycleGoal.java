package com.ifsc.ctds.stinghen.recycle_it_api.models.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@Entity
public class RecycleGoal extends Goal {

    @ManyToMany
    public List<Project> finishedProjects;
}
