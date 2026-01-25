package com.ifsc.ctds.stinghen.recycle_it_api.repository.project;

import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface que representa o repositório de materiais de projetos.
 */
public interface ProjectMaterialRepository extends JpaRepository<ProjectMaterial, Long> {
}
