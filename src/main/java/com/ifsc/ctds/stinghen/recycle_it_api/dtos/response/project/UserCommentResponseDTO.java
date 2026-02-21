package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.SimpleUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.UserComment;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de response para os comentários dos usuários
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCommentResponseDTO implements ResponseDTO {

    public SimpleUserResponseDTO user;

    public String text;

    public LocalDateTime date;

    public UserCommentResponseDTO(UserComment comment) {
        this.text = comment.getText();
        this.date = comment.getDate();
        
        SimpleUserResponseDTO userDto = null;
        if (comment.getUser() instanceof RegularUser regularUser) {
            userDto = new SimpleUserResponseDTO(regularUser);
        }
        this.user = userDto;
    }
}
