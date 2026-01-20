package com.ifsc.ctds.stinghen.recycle_it_api.repository.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade de Itens para a Meta de Redução
 */
public interface ReduceItemRepository extends JpaRepository<ReduceItem, Long> {
}
