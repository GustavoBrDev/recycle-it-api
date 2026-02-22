package com.ifsc.ctds.stinghen.recycle_it_api.repository.project;

import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface que representa o repositório de outros materiais de projetos.
 */
public interface OtherMaterialRepository extends JpaRepository<OtherMaterial, Long> {
}
