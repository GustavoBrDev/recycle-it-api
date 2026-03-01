package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.SimpleUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.UserPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * DTO de response para a pontuação do usuário na liga
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPunctuationResponseDTO implements ResponseDTO {

    public SimpleUserResponseDTO user;

    public  Long punctuation;

    public UserPunctuationResponseDTO(UserPunctuation userPunctuation) {
        SimpleUserResponseDTO userDto = null;
        if (userPunctuation.getUser() instanceof RegularUser regularUser) {
            userDto = new SimpleUserResponseDTO(regularUser);
        }
        this.user = userDto;
        this.punctuation = userPunctuation.calculateTotal();
    }
}
