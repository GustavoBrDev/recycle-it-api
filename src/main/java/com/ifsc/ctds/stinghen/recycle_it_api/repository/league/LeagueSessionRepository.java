package com.ifsc.ctds.stinghen.recycle_it_api.repository.league;

import com.ifsc.ctds.stinghen.recycle_it_api.models.league.LeagueSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositório para a entidade de Sessões da Liga
 */
@Repository
public interface LeagueSessionRepository extends JpaRepository<LeagueSession, Long> {

    Page<LeagueSession> findAllByOrderByEndDate(Pageable pageable);

    Page<LeagueSession> findByUsers_IdOrderByEndDate(Long userId, Pageable pageable);
    
    /**
     * Encontra a sessão de liga ativa de um usuário por ID
     * @param userId o ID do usuário
     * @return Optional contendo a sessão ativa, se encontrada
     */
    @Query("SELECT ls FROM LeagueSession ls " +
           "JOIN ls.users up " +
           "WHERE up.user.id = :userId " +
           "AND ls.startDate <= :currentDate " +
           "AND (ls.endDate >= :currentDate OR ls.endDate IS NULL) " +
           "AND ls.isFinished = false " +
           "ORDER BY ls.endDate DESC")
    Optional<LeagueSession> findActiveSessionByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);
    
    /**
     * Encontra a sessão de liga ativa de um usuário por email
     * @param userEmail o email do usuário
     * @return Optional contendo a sessão ativa, se encontrada
     */
    @Query("SELECT ls FROM LeagueSession ls " +
           "JOIN ls.users up " +
           "JOIN up.user.credential uc " +
           "WHERE uc.email = :userEmail " +
           "AND ls.startDate <= :currentDate " +
           "AND (ls.endDate >= :currentDate OR ls.endDate IS NULL) " +
           "AND ls.isFinished = false " +
           "ORDER BY ls.endDate DESC")
    Optional<LeagueSession> findActiveSessionByUserEmail(@Param("userEmail") String userEmail, @Param("currentDate") LocalDate currentDate);
}
