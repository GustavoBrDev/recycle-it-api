package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO de response para os materiais de um projeto de reciclagem
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "materialKind"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RecycledMaterialResponseDTO.class, name = "RECYCLED"),
        @JsonSubTypes.Type(value = OtherMaterialResponseDTO.class, name = "OTHER")
})
public class ProjectMaterialResponseDTO implements ResponseDTO  {

    public Long quantity;

    public ProjectMaterialResponseDTO(ProjectMaterial material) {
        this.quantity = material.getQuantity();
    }

}
