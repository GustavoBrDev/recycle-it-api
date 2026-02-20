package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de response para projetos de reclicagem
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponseDTO implements ResponseDTO {

    public Long id;

    public String text;

    public String description;

    private List<ProjectMaterialResponseDTO> materials;

    public String instructions;

    public List<UserCommentResponseDTO> userComments;
}
