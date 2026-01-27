package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.OtherMaterials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
public class OtherMaterialRequestDTO extends ProjectMaterialRequestDTO<OtherMaterial> {

    @NotBlank
    @Valid
    public OtherMaterials type;

    @NotBlank
    public String description;

    @Override
    public OtherMaterial convert() {
        return OtherMaterial.builder()
                .type(type)
                .quantity(super.quantity)
                .description(description)
                .build();
    }
}
