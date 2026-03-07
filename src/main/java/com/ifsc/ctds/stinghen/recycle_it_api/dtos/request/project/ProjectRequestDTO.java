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
    public String title;

    @NotBlank
    public String description;

    @NotEmpty
    public List<? extends ProjectMaterialRequestDTO<? extends ProjectMaterial>> materials;

    @NotBlank
    public String instructions;

    @Override
    public Project convert() {

        System.out.println(materials);

        Project project = Project.builder()
                .title(title)
                .description(description)
                .instructions(instructions)
                .build();

        List<ProjectMaterial> materialList = materials.stream()
                .map(ProjectMaterialRequestDTO::convert)
                .collect(Collectors.toList());

        materialList.forEach(material -> material.setProject(project));

        project.setMaterials(materialList);

        return project;
    }
}
