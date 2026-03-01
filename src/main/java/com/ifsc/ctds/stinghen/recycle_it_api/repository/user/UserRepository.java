package com.ifsc.ctds.stinghen.recycle_it_api.repository.user;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface que representa o repositório de usuários.
 * Extende JpaRepository para fornecer operações CRUD e JpaSpecificationExecutor para suporte a consultas complexas.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


}
