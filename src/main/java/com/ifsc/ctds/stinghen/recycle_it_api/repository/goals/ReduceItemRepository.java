package com.ifsc.ctds.stinghen.recycle_it_api.repository.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositório para a entidade de Itens para a Meta de Redução
 */
public interface ReduceItemRepository extends JpaRepository<ReduceItem, Long> {

    List<ReduceItem> findByType(Materials type);

    Page<ReduceItem> findByType(Materials type, Pageable pageable);

    @Query("SELECT ri FROM ReduceItem ri WHERE ri.actualQuantity >= ri.targetQuantity")
    List<ReduceItem> findCompleted();

    @Query("SELECT ri FROM ReduceItem ri WHERE ri.actualQuantity >= ri.targetQuantity")
    Page<ReduceItem> findCompleted(Pageable pageable);
}
