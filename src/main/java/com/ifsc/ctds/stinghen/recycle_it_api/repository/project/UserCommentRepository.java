package com.ifsc.ctds.stinghen.recycle_it_api.repository.project;

import com.ifsc.ctds.stinghen.recycle_it_api.models.project.UserComment;;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface que representa o repositório de comentários de usuários nos projetos.
 */

public interface UserCommentRepository extends JpaRepository<UserComment, Long> {
    
    List<UserComment> findByUserId(Long userId);
    
    List<UserComment> findFirst10ByOrderByDateDesc();
}
