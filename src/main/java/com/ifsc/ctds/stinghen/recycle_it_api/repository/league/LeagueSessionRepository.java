package com.ifsc.ctds.stinghen.recycle_it_api.repository.league;

import com.ifsc.ctds.stinghen.recycle_it_api.models.league.LeagueSession;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade de Sessões da Liga
 */
public interface LeagueSessionRepository extends JpaRepository<LeagueSession, Long> {
}
