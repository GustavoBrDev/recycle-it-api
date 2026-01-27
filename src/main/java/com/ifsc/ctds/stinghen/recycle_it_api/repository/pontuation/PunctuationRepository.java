package com.ifsc.ctds.stinghen.recycle_it_api.repository.pontuation;

import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade de Pontuação
 */
public interface PunctuationRepository extends JpaRepository<Punctuation, Long> {
}
