package com.ifsc.ctds.stinghen.recycle_it_api.models.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Data
@EqualsAndHashCode(callSuper=true)
public class PurchasableAvatar extends PurchasableItem {

    private Avatar avatar;

    @Override
    public long buy() {
        return 0;
    }
}
