package com.ifsc.ctds.stinghen.recycle_it_api.repository.league;

import com.ifsc.ctds.stinghen.recycle_it_api.models.league.League;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade de Sessões da Liga
 */
public interface LeagueRepository extends JpaRepository<League, Long> {

    League findLeagueByTier(int tier);

}
