package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.OtherMaterials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para solicitação de atualização de outro material.
 */
public class OtherMaterialPutRequestDTO implements ConvertibleRequestDTO<OtherMaterial> {

    @NotBlank
    public OtherMaterials type;

    @NotNull
    @Positive
    public Long quantity;

    @NotBlank
    public String description;

    @Override
    public OtherMaterial convert() {
        return OtherMaterial.builder()
                .type(type)
                .quantity(quantity)
                .description(description)
                .build();
    }
}
