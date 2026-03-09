package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.FriendRequest;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestResponseDTO implements ResponseDTO {

    public Long id;
    public SimpleUserResponseDTO user;

    // Construtor que recebe a FriendRequest e o usuário desejado para retornar
    public FriendRequestResponseDTO(FriendRequest friendRequest, RegularUser userToReturn) {
        this.id = friendRequest.getId();
        this.user = new SimpleUserResponseDTO(userToReturn);
    }

    // Construtor original para compatibilidade (retorna o sender)
    public FriendRequestResponseDTO(FriendRequest friendRequest) {
        this.id = friendRequest.getId();
        this.user = new SimpleUserResponseDTO((RegularUser) friendRequest.getSender());
    }
}
