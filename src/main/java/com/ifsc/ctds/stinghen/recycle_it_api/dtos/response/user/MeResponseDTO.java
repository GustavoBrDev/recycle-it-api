package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * DTO de retorno das informações do usuário
 * Simplificada, contendo apenas informações a serem exibidas de forma minimizada
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeResponseDTO implements ResponseDTO {

    public String email;

    private Avatar avatar;

    public MeResponseDTO(RegularUser user) {
        this.email = user.getCredential().getEmail();
        this.avatar = user.getCurrentAvatar();
    }

}
