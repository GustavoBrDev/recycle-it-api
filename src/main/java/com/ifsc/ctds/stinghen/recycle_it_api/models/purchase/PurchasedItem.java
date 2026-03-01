package com.ifsc.ctds.stinghen.recycle_it_api.models.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class PurchasedItem implements IPurchased {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PurchasableItem item;

    @ManyToOne
    private RegularUser user;

    private LocalDateTime purchaseDate;
}
