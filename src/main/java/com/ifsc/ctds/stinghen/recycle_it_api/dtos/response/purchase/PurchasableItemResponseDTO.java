package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.IPurchasable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class PurchasableItemResponseDTO implements ResponseDTO {

    private Long id;

    private Long price;

    private Boolean isOnSale;

    @JsonProperty("type")
    public String getType() {
        String name = this.getClass().getSimpleName();

        if (name.startsWith("Purchasable")) {
            name = name.substring("Purchasable".length());
        }

        if (name.endsWith("ResponseDTO")) {
            name = name.substring(0, name.length() - "ResponseDTO".length());
        }

        return name;
    }
}
