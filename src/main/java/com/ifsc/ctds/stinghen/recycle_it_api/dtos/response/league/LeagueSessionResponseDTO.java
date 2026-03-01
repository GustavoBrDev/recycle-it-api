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
 * DTO de response da sessão de liga
 */
public class LeagueSessionResponseDTO implements ResponseDTO {

    public League league;

    public LocalDate startDate;

    public LocalDate endDate;

    public List<UserPunctuationResponseDTO> users;

    public LeagueSessionResponseDTO(LeagueSession session) {
        this.league = session.getLeague();
        this.startDate = session.getStartDate();
        this.endDate = session.getEndDate();
        
        this.users = session.getUsers() != null
            ? session.getUsers().stream().map(UserPunctuationResponseDTO::new).toList()
            : List.of();
    }

}
