package com.ifsc.ctds.stinghen.recycle_it_api.models.purchase;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
public class PurchasableSkipComment extends PurchasableItem {

    @Override
    public long buy() {
        return 0;
    }
}
