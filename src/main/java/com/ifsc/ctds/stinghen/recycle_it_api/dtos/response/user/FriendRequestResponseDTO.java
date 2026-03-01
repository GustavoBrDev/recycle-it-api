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

    private Long id;
    private SimpleUserResponseDTO sender;

    public FriendRequestResponseDTO(FriendRequest friendRequest) {
        this.id = friendRequest.getId();
        this.sender = new SimpleUserResponseDTO( (RegularUser) friendRequest.getSender());
    }
}
