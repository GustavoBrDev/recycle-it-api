package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.OtherMaterials;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OtherMaterialResponseDTO extends ProjectMaterialResponseDTO {

    public OtherMaterials type;
    public String description;
}
