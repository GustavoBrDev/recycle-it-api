package com.ifsc.ctds.stinghen.recycle_it_api.repository.project;

import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface que representa o repositório de materiais reciclados.
 */
public interface RecycledMaterialRepository extends JpaRepository<RecycledMaterial, Long> {
}
