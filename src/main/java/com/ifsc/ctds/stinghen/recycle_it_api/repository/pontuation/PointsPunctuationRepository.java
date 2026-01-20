package com.ifsc.ctds.stinghen.recycle_it_api.repository.pontuation;

import com.ifsc.ctds.stinghen.recycle_it_api.models.pontutation.PointsPunctuation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade de Pontuação por pontos
 */
public interface PointsPunctuationRepository extends JpaRepository<PointsPunctuation, Long> {
}
