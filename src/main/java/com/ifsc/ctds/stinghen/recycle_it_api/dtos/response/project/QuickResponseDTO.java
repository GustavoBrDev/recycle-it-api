package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
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
public class QuickResponseDTO implements ResponseDTO {

    public Long id;

    public String description;

    private List<ProjectMaterialResponseDTO> materials;
}
