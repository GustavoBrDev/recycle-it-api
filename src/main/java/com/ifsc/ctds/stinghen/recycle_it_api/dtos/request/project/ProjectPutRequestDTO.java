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
public class ProjectPutRequestDTO implements ConvertibleRequestDTO<Project> {

    @NotBlank
    public String title;

    @NotBlank
    public String description;

    @NotBlank
    public String instructions;

    @Override
    public Project convert() {
        return Project.builder()
                .title(title)
                .description(description)
                .instructions(instructions)
                .build();
    }
}
