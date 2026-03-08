package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
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

    public String title;

    public String description;

    public List<ProjectMaterialResponseDTO> materials;

    public QuickProjectResponseDTO(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        
        this.materials = project.getMaterials() != null
            ? project.getMaterials().stream()
                .map(material -> {
                    if (material instanceof RecycledMaterial recycled) {
                        return new RecycledMaterialResponseDTO(recycled);
                    } else if (material instanceof OtherMaterial other) {
                        return new OtherMaterialResponseDTO(other);
                    }
                    throw new IllegalStateException("Tipo de material desconhecido: " + material.getClass());
                })
                .toList()
            : List.of();
    }

    public QuickProjectResponseDTO(Project project, List<ProjectMaterial> materials) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        
        this.materials = materials != null
            ? materials.stream()
                .map(material -> {
                    if (material instanceof RecycledMaterial recycled) {
                        return new RecycledMaterialResponseDTO(recycled);
                    } else if (material instanceof OtherMaterial other) {
                        return new OtherMaterialResponseDTO(other);
                    }
                    throw new IllegalStateException("Tipo de material desconhecido: " + material.getClass());
                })
                .toList()
            : List.of();
    }
}
