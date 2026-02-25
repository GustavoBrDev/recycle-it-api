package com.ifsc.ctds.stinghen.recycle_it_api.repository.pontuation;

import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade de Pontuação por pontos
 */
public interface PointsPunctuationRepository extends JpaRepository<PointsPunctuation, Long> {

    List<PointsPunctuation> findByUserId(Long userId);

    List<PointsPunctuation> findByUserCredentialEmail(String email);

    /**
     * Encontra a pontuação mais recente pelo ID do usuário
     * @param userId o ID do usuário
     * @return a pontuação mais recente do usuário
     */
    default Optional<PointsPunctuation> findMostRecentByUserId(Long userId) {
        List<PointsPunctuation> results = findByUserId(userId);
        return results.stream().min((p1, p2) -> p2.getLastUpdated().compareTo(p1.getLastUpdated()));
    }

    /**
     * Encontra a pontuação mais recente pelo email do usuário
     * @param email o email do usuário
     * @return a pontuação mais recente do usuário
     */
    default Optional<PointsPunctuation> findMostRecentByUserEmail(String email) {
        List<PointsPunctuation> results = findByUserCredentialEmail(email);
        return results.stream().min((p1, p2) -> p2.getLastUpdated().compareTo(p1.getLastUpdated()));
    }
}
