package com.ifsc.ctds.stinghen.recycle_it_api.repository.user;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Interface que representa o repositório de usuários.
 * Extende JpaRepository para fornecer operações CRUD e JpaSpecificationExecutor para suporte a consultas complexas.
 */
public interface RegularUserRepository extends JpaRepository<RegularUser, Long>, JpaSpecificationExecutor<RegularUser> {
}
