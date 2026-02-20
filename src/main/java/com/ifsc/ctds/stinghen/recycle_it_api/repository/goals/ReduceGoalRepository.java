package com.ifsc.ctds.stinghen.recycle_it_api.repository.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade de Metas de Redução
 */
public interface ReduceGoalRepository extends JpaRepository<ReduceGoal, Long> {
}
