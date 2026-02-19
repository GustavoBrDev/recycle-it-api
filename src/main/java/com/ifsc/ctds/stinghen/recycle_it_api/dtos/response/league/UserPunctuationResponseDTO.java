package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.SimpleUserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder

/**
 * DTO de response para a pontuação do usuário na liga
 */
public class UserPunctuationResponseDTO implements ResponseDTO {

    public SimpleUserResponseDTO user;

    public  Long punctuation;
}
