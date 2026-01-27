package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.punctuation.PunctuationResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.League;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de retorno das informações do usuário
 * Completa, contendo apenas informações públicas a serem visualizadas no perfil
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullUserResponseDTO implements ResponseDTO {

    public Long id;

    public String name;

    public Avatar currentAvatar;

    public League actualLeague;

    public List<SimpleUserResponseDTO> friends;

    public List<PunctuationResponseDTO> punctuations;
}
