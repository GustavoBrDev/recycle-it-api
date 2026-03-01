package com.ifsc.ctds.stinghen.recycle_it_api.repository.user;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Interface que representa o repositório de usuários.
 * Extende JpaRepository para fornecer operações CRUD e JpaSpecificationExecutor para suporte a consultas complexas.
 */
public interface RegularUserRepository extends JpaRepository<RegularUser, Long>, JpaSpecificationExecutor<RegularUser> {

    List<RegularUser> findByActualLeague_Id(Long leagueId);

    Page<RegularUser> findByActualLeague_Id(Long leagueId, Pageable pageable);
}
