package com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableAvatar;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PurchasableAvatarResponseDTO extends PurchasableItemResponseDTO {

    private Avatar avatar;

    public PurchasableAvatarResponseDTO(PurchasableAvatar avatar) {
        super(avatar);
        this.avatar = avatar.getAvatar();
    }
}
