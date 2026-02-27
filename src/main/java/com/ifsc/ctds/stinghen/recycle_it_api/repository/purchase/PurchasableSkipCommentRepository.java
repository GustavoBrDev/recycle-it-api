package com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableSkipComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface que representa o repositório de itens adquiridos, em especial os comentarios que podem ser pulados .
 */
public interface PurchasableSkipCommentRepository extends JpaRepository<PurchasableSkipComment, Long> {

    List<PurchasableSkipComment> findByIsOnSaleTrue();

    Page<PurchasableSkipComment> findByIsOnSaleTrue(Pageable pageable);
}
