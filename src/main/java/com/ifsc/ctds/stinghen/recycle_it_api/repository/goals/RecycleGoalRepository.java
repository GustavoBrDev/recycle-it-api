package com.ifsc.ctds.stinghen.recycle_it_api.repository.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.RecycleGoal;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositório para a entidade de Metas de Reciclagem
 */
public interface RecycleGoalRepository extends JpaRepository<RecycleGoal, Long> {
}
