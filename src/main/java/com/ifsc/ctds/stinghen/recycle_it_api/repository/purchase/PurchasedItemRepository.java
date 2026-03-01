package com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface que representa o repositório de itens adquiridos.
 */
public interface PurchasedItemRepository extends JpaRepository<PurchasedItem, Long> {

    @Query("SELECT pi FROM PurchasedItem pi WHERE pi.purchaseDate BETWEEN :startDate AND :endDate")
    List<PurchasedItem> findByPurchaseDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT pi FROM PurchasedItem pi WHERE pi.purchaseDate BETWEEN :startDate AND :endDate")
    Page<PurchasedItem> findByPurchaseDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
