package com.ifsc.ctds.stinghen.recycle_it_api.repository.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalStatus;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório para a entidade de Metas de Redução
 */
public interface ReduceGoalRepository extends JpaRepository<ReduceGoal, Long> {

    // Métodos por status
    List<ReduceGoal> findByStatus(GoalStatus status);
    Page<ReduceGoal> findByStatus(GoalStatus status, Pageable pageable);

    // Métodos por status e usuário ID
    List<ReduceGoal> findByUser_IdAndStatus(Long userId, GoalStatus status);
    Page<ReduceGoal> findByUser_IdAndStatus(Long userId, GoalStatus status, Pageable pageable);

    // Métodos por usuário e email (via join)
    List<ReduceGoal> findByUser_Credential_EmailAndStatus(String email, GoalStatus status);
    Page<ReduceGoal> findByUser_Credential_EmailAndStatus(String email, GoalStatus status, Pageable pageable);
}
