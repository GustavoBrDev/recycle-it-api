package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;
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

    public ReduceItemResponseDTO(ReduceItem item) {
        this.type = item.getType();
        this.targetQuantity = item.getTargetQuantity();
        this.actualQuantity = item.getActualQuantity();
    }
}
