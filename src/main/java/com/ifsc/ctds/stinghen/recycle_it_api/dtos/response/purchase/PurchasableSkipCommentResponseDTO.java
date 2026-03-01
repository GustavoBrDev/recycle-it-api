package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableSkipComment;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
public class PurchasableSkipCommentResponseDTO extends PurchasableItemResponseDTO {

    public PurchasableSkipCommentResponseDTO(PurchasableSkipComment skipComment) {
        super(skipComment);
    }
}
