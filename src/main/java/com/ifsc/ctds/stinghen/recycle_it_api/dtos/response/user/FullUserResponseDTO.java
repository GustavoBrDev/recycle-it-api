package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.punctuation.PunctuationResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.League;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
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

    public FullUserResponseDTO(RegularUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.currentAvatar = user.getCurrentAvatar();
        this.actualLeague = user.getActualLeague();
        
        this.friends = user.getFriends() != null 
            ? user.getFriends().stream().map(SimpleUserResponseDTO::new).toList()
            : List.of();
        
        this.punctuations = user.getPunctuations() != null
            ? user.getPunctuations().stream().map(this::convertPunctuation).toList()
            : List.of();
    }

    private PunctuationResponseDTO convertPunctuation(Punctuation punctuation) {
        if (punctuation instanceof PointsPunctuation pointsPunctuation) {
            return new com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.punctuation.PointsPunctuationResponseDTO(pointsPunctuation);
        }
        return null;
    }
}
