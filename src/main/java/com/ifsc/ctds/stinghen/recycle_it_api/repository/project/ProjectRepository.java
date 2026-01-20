package com.ifsc.ctds.stinghen.recycle_it_api.repository.project;

import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
/**
 * Interface que representa o repositório de projetos.
 * Utiliza o JpaSpecificationExecutor para suporte a consultas complexas.
 */
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
}
