package com.ifsc.ctds.stinghen.recycle_it_api.repository.project;

import com.ifsc.ctds.stinghen.recycle_it_api.enums.OtherMaterials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface que representa o repositório de outros materiais de projetos.
 */
public interface OtherMaterialRepository extends JpaRepository<OtherMaterial, Long> {

    List<OtherMaterial> findByType(OtherMaterials type);

    Page<OtherMaterial> findByType(OtherMaterials type, Pageable pageable);

    List<OtherMaterial> findByDescriptionContainingIgnoreCase(String description);

    Page<OtherMaterial> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
