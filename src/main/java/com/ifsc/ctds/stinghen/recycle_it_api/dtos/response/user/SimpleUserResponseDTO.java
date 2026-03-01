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
public class SimpleUserResponseDTO implements ResponseDTO {

    public Long id;

    public String name;

    private Avatar avatar;

    public SimpleUserResponseDTO(RegularUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.avatar = user.getCurrentAvatar();
    }

}
