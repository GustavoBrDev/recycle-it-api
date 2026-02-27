package com.ifsc.ctds.stinghen.recycle_it_api.repository.pontuation;

import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para a entidade de Pontuação
 */
public interface PunctuationRepository extends JpaRepository<Punctuation, Long> {

    @Query("SELECT p FROM Punctuation p WHERE p.lastUpdated BETWEEN :startDate AND :endDate")
    List<Punctuation> findByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM Punctuation p WHERE p.lastUpdated BETWEEN :startDate AND :endDate")
    Page<Punctuation> findByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
