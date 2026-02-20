package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.SimpleUserResponseDTO;
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
}
