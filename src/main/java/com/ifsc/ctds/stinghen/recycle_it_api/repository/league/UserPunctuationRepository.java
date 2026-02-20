package com.ifsc.ctds.stinghen.recycle_it_api.repository.league;

import com.ifsc.ctds.stinghen.recycle_it_api.models.league.UserPunctuation;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositório para a entidade de Pontuação do Usuário durante a Sessão da Liga
 */
public interface UserPunctuationRepository extends JpaRepository<UserPunctuation, Long> {

    UserPunctuation findByUser_Id ( Long id );
}
