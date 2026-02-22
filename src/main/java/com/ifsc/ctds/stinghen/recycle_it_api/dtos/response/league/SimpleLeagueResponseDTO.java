package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.League;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder

/**
 * DTO simplificada de response da liga
 */
public class SimpleLeagueResponseDTO implements ResponseDTO {

    public String name;
    public int tier;


    public SimpleLeagueResponseDTO(League league) {
        this.name = league.getName();
        this.tier = league.getTier();
    }

}
