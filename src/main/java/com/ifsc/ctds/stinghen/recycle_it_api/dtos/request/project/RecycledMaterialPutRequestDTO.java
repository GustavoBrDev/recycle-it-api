package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para solicitação de atualização de material reciclado.
 */
public class RecycledMaterialPutRequestDTO implements ConvertibleRequestDTO<RecycledMaterial> {

    @NotBlank
    public Materials type;

    @NotNull
    @Positive
    public Long quantity;

    @Override
    public RecycledMaterial convert() {
        return RecycledMaterial.builder()
                .type(type)
                .quantity(quantity)
                .build();
    }
}
