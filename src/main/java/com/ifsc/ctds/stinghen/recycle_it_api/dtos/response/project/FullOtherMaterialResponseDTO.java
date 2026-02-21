package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.OtherMaterials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FullOtherMaterialResponseDTO extends FullProjectMaterialResponseDTO {

    public OtherMaterials type;
    public String description;

    public FullOtherMaterialResponseDTO(OtherMaterial material) {
        super(material);
        this.type = material.getType();
        this.description = material.getDescription();
    }
}
