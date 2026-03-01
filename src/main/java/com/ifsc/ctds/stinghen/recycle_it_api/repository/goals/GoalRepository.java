package com.ifsc.ctds.stinghen.recycle_it_api.repository.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalStatus;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


/**
 * Repositório para a entidade de Metas
 * Com specification executor para filtros complexos
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {

    // Métodos por status
    List<Goal> findByStatus(GoalStatus status);
    Page<Goal> findByStatus(GoalStatus status, Pageable pageable);

    // Métodos por status e usuário ID
    List<Goal> findByUser_IdAndStatus(Long userId, GoalStatus status);
    Page<Goal> findByUser_IdAndStatus(Long userId, GoalStatus status, Pageable pageable);

    // Métodos por usuário e email (via join)
    List<Goal> findByUser_Credential_EmailAndStatus(String email, GoalStatus status);
    Page<Goal> findByUser_Credential_EmailAndStatus(String email, GoalStatus status, Pageable pageable);

    // Métodos para verificar metas marcadas para hoje (nextCheck)
    List<Goal> findByUser_IdAndNextCheck(Long userId, LocalDate date);
    List<Goal> findByUser_Credential_EmailAndNextCheck(String email, LocalDate date);

    // Queries performáticas para buscar apenas IDs das metas marcadas para hoje
    @Query("SELECT g.id FROM Goal g WHERE g.user.id = :userId AND g.nextCheck = :date")
    List<Long> findGoalIdsByUserAndNextCheck(Long userId, LocalDate date);

    @Query("SELECT g.id FROM Goal g WHERE g.user.credential.email = :email AND g.nextCheck = :date")
    List<Long> findGoalIdsByUserEmailAndNextCheck(String email, LocalDate date);
}
