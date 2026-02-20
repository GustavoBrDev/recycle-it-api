package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.League;
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

}
