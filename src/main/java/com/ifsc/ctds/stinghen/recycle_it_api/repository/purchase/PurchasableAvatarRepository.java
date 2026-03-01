package com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableAvatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface que representa o repositório de itens adquiridos, em especial os avatares.
 */
public interface PurchasableAvatarRepository extends JpaRepository<PurchasableAvatar, Long> {

    List<PurchasableAvatar> findByIsOnSaleTrue();

    Page<PurchasableAvatar> findByIsOnSaleTrue(Pageable pageable);
}
