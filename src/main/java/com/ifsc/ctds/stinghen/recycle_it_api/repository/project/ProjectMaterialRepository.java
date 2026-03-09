package com.ifsc.ctds.stinghen.recycle_it_api.repository.project;

import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interface que representa o repositório de materiais de projetos.
 */
public interface ProjectMaterialRepository extends JpaRepository<ProjectMaterial, Long> {

    List<ProjectMaterial> findByQuantityGreaterThanEqual(Long minQuantity);

    Page<ProjectMaterial> findByQuantityGreaterThanEqual(Long minQuantity, Pageable pageable);

    @Query("SELECT pm FROM ProjectMaterial pm WHERE pm.project.id = :projectId")
    List<ProjectMaterial> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT pm FROM ProjectMaterial pm WHERE pm.project.id = :projectId")
    Page<ProjectMaterial> findByProjectId(@Param("projectId") Long projectId, Pageable pageable);
}
