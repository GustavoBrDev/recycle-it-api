package com.ifsc.ctds.stinghen.recycle_it_api.models.purchase;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PurchasableItem implements IPurchasable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private Boolean isOnSale;
}
