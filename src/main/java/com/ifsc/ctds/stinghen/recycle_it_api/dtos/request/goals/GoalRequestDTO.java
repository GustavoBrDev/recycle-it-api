package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project.RecycledMaterialRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO abstrata para metas do usuário
 * @param <T> DTO concreta (generics)
 */
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RecycledMaterialRequestDTO.class, name = "RECYCLED"),
})
public abstract class GoalRequestDTO<T> implements ConvertibleRequestDTO<T> {

    @NotBlank
    public GoalDifficult goalDifficult;

    @NotBlank
    public GoalFrequency goalFrequency;

}
