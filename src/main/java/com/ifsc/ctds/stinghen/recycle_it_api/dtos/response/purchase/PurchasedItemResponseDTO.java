package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasedItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchasedItemResponseDTO implements ResponseDTO {

    private Long id;

    private PurchasableItemResponseDTO item;

    private LocalDateTime purchaseDate;

    public PurchasedItemResponseDTO(PurchasedItem purchasedItem) {
        this.id = purchasedItem.getId();
        this.purchaseDate = purchasedItem.getPurchaseDate();
        this.item = new PurchasableItemResponseDTO(purchasedItem.getItem());
    }
}
