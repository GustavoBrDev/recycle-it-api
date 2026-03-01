package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO resumida de response para projetos de reclicagem (listagem)
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuickProjectResponseDTO implements ResponseDTO {

    public Long id;

    public String description;

    private List<ProjectMaterialResponseDTO> materials;

    public QuickProjectResponseDTO(Project project) {
        this.id = project.getId();
        this.description = project.getDescription();
        
        this.materials = project.getMaterials() != null
            ? project.getMaterials().stream().map(ProjectMaterialResponseDTO::new).toList()
            : List.of();
    }
}
