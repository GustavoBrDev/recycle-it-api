package com.ifsc.ctds.stinghen.recycle_it_api.repository.league;

import com.ifsc.ctds.stinghen.recycle_it_api.models.league.UserPunctuation;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositório para a entidade de Pontuação do Usuário durante a Sessão da Liga
 */
public interface UserPunctuationRepository extends JpaRepository<UserPunctuation, Long> {

    /**
     * Encontra a pontuação do usuário em uma sessão específica
     * @param userId o ID do usuário
     * @param sessionId o ID da sessão
     * @return Optional contendo a pontuação, se encontrada
     */
    UserPunctuation findByUser_IdAndSession_Id(Long userId, Long sessionId);
}
