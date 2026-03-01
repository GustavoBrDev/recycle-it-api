package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO completa de response para projetos de reclicagem
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullProjectResponseDTO implements ResponseDTO {

    public Long id;

    public String text;

    public String description;

    private List<FullProjectMaterialResponseDTO> materials;

    public String instructions;

    public List<UserCommentResponseDTO> userComments;

    public FullProjectResponseDTO(Project project) {
        this.id = project.getId();
        this.text = project.getText();
        this.description = project.getDescription();
        this.instructions = project.getInstructions();
        
        this.materials = project.getMaterials() != null
            ? project.getMaterials().stream().map(FullProjectMaterialResponseDTO::new).toList()
            : List.of();
        
        this.userComments = project.getUserComments() != null
            ? project.getUserComments().stream().map(UserCommentResponseDTO::new).toList()
            : List.of();
    }
}
