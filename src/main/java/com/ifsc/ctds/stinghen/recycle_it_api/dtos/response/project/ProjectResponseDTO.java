package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
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

    public ProjectResponseDTO(Project project) {
        this.id = project.getId();
        this.text = project.getText();
        this.description = project.getDescription();
        this.instructions = project.getInstructions();
        
        this.materials = project.getMaterials() != null
            ? project.getMaterials().stream().map(ProjectMaterialResponseDTO::new).toList()
            : List.of();
        
        this.userComments = project.getUserComments() != null
            ? project.getUserComments().stream().map(UserCommentResponseDTO::new).toList()
            : List.of();
    }
}
