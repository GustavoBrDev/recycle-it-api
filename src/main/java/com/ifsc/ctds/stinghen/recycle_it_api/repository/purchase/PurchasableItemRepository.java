package com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface que representa o repositório de itens adquiridos.
 */
public interface PurchasableItemRepository extends JpaRepository<PurchasableItem, Long> {

    List<PurchasableItem> findByIsOnSaleTrue();

    Page<PurchasableItem> findByIsOnSaleTrue(Pageable pageable);
}
