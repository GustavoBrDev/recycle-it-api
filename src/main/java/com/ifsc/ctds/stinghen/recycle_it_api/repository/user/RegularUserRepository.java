package com.ifsc.ctds.stinghen.recycle_it_api.repository.user;

import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.QuickProjectResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interface que representa o repositório de usuários.
 * Extende JpaRepository para fornecer operações CRUD e JpaSpecificationExecutor para suporte a consultas complexas.
 */
public interface RegularUserRepository extends JpaRepository<RegularUser, Long>, JpaSpecificationExecutor<RegularUser> {

    List<RegularUser> findByActualLeague_Id(Long leagueId);

    Page<RegularUser> findByActualLeague_Id(Long leagueId, Pageable pageable);

    /**
     * Busca os projetos de um usuário pelo ID do usuário como QuickProjectResponseDTO
     * @param userId o ID do usuário
     * @return lista de QuickProjectResponseDTO
     */
    @Query("SELECT new com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.QuickProjectResponseDTO(p) " +
           "FROM RegularUser u JOIN u.projects p WHERE u.id = :userId")
    List<QuickProjectResponseDTO> findProjectsByUserIdAsQuick(@Param("userId") Long userId);

    /**
     * Busca os projetos de um usuário pelo email como QuickProjectResponseDTO
     * @param email o email do usuário
     * @return lista de QuickProjectResponseDTO
     */
    @Query("SELECT new com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.QuickProjectResponseDTO(p) " +
           "FROM RegularUser u JOIN u.projects p WHERE u.credential.email = :email")
    List<QuickProjectResponseDTO> findProjectsByUserEmailAsQuick(@Param("email") String email);

    /**
     * Verifica se um usuário está realizando um projeto específico
     * @param userId o ID do usuário
     * @param projectId o ID do projeto
     * @return true se o usuário está realizando o projeto, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM RegularUser u JOIN u.projects p WHERE u.id = :userId AND p.id = :projectId")
    boolean existsProjectByUserId(@Param("userId") Long userId, @Param("projectId") Long projectId);

    /**
     * Verifica se um usuário está realizando um projeto específico pelo email
     * @param email o email do usuário
     * @param projectId o ID do projeto
     * @return true se o usuário está realizando o projeto, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM RegularUser u JOIN u.projects p WHERE u.credential.email = :email AND p.id = :projectId")
    boolean existsProjectByUserEmail(@Param("email") String email, @Param("projectId") Long projectId);
}
