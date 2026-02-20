package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitação de material de projeto.
 */
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "materialKind"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RecycledMaterialRequestDTO.class, name = "RECYCLED"),
        @JsonSubTypes.Type(value = OtherMaterialRequestDTO.class, name = "OTHER")
})
public abstract class ProjectMaterialRequestDTO<T> implements ConvertibleRequestDTO<T> {

    @NotNull
    @Positive
    public Long quantity;

}
