package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para solicitação de projeto.
 */
public class ProjectRequestDTO implements ConvertibleRequestDTO<Project> {

    @NotBlank
    public String text;

    @NotBlank
    public String description;

    @NotEmpty
    public List<? extends ProjectMaterialRequestDTO<? extends ProjectMaterial>> materials;

    @NotBlank
    public String instructions;

    @Override
    public Project convert() {
        return Project.builder()
                .text(text)
                .description(description)
                .materials(
                        materials.stream()
                                .map(ProjectMaterialRequestDTO::convert)
                                .collect(Collectors.toList())
                )
                .instructions(instructions)
                .build();
    }
}
