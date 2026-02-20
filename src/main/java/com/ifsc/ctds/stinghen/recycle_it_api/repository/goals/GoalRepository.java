package com.ifsc.ctds.stinghen.recycle_it_api.repository.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.Goal;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositório para a entidade de Metas
 * Com specification executor para filtros complexos
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {
}
