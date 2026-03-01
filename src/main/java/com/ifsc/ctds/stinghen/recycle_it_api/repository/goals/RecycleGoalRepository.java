package com.ifsc.ctds.stinghen.recycle_it_api.repository.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalStatus;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.RecycleGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Repositório para a entidade de Metas de Reciclagem
 */
public interface RecycleGoalRepository extends JpaRepository<RecycleGoal, Long> {

    // Métodos por status
    List<RecycleGoal> findByStatus(GoalStatus status);
    Page<RecycleGoal> findByStatus(GoalStatus status, Pageable pageable);

    // Métodos por status e usuário ID
    List<RecycleGoal> findByUser_IdAndStatus(Long userId, GoalStatus status);
    Page<RecycleGoal> findByUser_IdAndStatus(Long userId, GoalStatus status, Pageable pageable);

    // Métodos por usuário e email (via join)
    List<RecycleGoal> findByUser_Credential_EmailAndStatus(String email, GoalStatus status);
    Page<RecycleGoal> findByUser_Credential_EmailAndStatus(String email, GoalStatus status, Pageable pageable);
}
