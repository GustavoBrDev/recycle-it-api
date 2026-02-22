package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.League;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.LeagueSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder

/**
 * DTO completa de response da liga
 */
public class FullLeagueResponseDTO implements ResponseDTO {

    public String name;
    public int tier;
    public int promotedCount;
    public int relegatedCount;
    public boolean promotionEnabled;
    public boolean relegationEnabled;


    public FullLeagueResponseDTO(League league) {
        this.name = league.getName();
        this.tier = league.getTier();
        this.promotedCount = league.getPromotedCount();
        this.relegatedCount = league.getRelegatedCount();
        this.promotionEnabled = league.isPromotionEnabled();
        this.relegationEnabled = league.isRelegationEnabled();
    }

}
