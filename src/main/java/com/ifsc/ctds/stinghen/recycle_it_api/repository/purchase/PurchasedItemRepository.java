package com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface que representa o repositório de itens adquiridos.
 */
public interface PurchasedItemRepository extends JpaRepository<PurchasedItem, Long> {
}
