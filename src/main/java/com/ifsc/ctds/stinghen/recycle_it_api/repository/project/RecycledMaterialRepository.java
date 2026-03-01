package com.ifsc.ctds.stinghen.recycle_it_api.repository.project;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface que representa o repositório de materiais reciclados.
 */
public interface RecycledMaterialRepository extends JpaRepository<RecycledMaterial, Long> {

    List<RecycledMaterial> findByType(Materials type);

    Page<RecycledMaterial> findByType(Materials type, Pageable pageable);
}
