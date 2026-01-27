package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * DTO de resposta para os "componentes" da meta de redução
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReduceItemResponseDTO {

    public Materials type;

    public int targetQuantity;

    public int actualQuantity;
}
