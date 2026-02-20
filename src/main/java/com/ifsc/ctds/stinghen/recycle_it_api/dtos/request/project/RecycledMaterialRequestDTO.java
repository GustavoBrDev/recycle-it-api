package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class RecycledMaterialRequestDTO extends ProjectMaterialRequestDTO<RecycledMaterial> {

    @NotBlank
    @Valid
    public Materials type;

    @Override
    public RecycledMaterial convert() {
        return RecycledMaterial.builder()
                .type(type)
                .quantity(super.quantity)
                .build();
    }
}
