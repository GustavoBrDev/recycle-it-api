package com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.ConvertibleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ReduceItemRequestDTO implements ConvertibleRequestDTO<ReduceItem> {

    @NotBlank
    public Materials type;

    @NotNull
    @PositiveOrZero
    public int quantity;

    @Override
    public ReduceItem convert() {
        return ReduceItem.builder()
                .type(this.type)
                .quantity(this.quantity)
                .build();
    }
}
