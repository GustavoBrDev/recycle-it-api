package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RecycledMaterialResponseDTO extends ProjectMaterialResponseDTO {

    public Materials type;

    public RecycledMaterialResponseDTO(RecycledMaterial material) {
        super(material);
        this.type = material.getType();
    }
}
