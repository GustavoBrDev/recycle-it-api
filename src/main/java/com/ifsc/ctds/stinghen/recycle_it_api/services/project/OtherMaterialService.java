package com.ifsc.ctds.stinghen.recycle_it_api.services.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.OtherMaterials;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.OtherMaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de outros materiais de projetos
 * @author Gustavo Stinghen
 * @see OtherMaterial
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class OtherMaterialService {

    public OtherMaterialRepository repository;

    /**
     * Atualiza o tipo de material de um outro material
     * @param id o id do outro material
     * @param type o novo tipo de material
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o outro material não for encontrado
     */
    @Transactional
    public ResponseDTO editType(Long id, OtherMaterials type) {
        if (repository.existsById(id)) {
            OtherMaterial otherMaterial = repository.findById(id).get();
            otherMaterial.setType(type);
            repository.save(otherMaterial);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Outro material atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Outro material não encontrado com id: " + id);
    }

    /**
     * Atualiza a descrição de um outro material
     * @param id o id do outro material
     * @param description a nova descrição
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o outro material não for encontrado
     */
    @Transactional
    public ResponseDTO editDescription(Long id, String description) {
        if (repository.existsById(id)) {
            OtherMaterial otherMaterial = repository.findById(id).get();
            otherMaterial.setDescription(description);
            repository.save(otherMaterial);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Outro material atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Outro material não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de OtherMaterial pelo id fornecido
     * @param id o id a ser buscado
     * @return o outro material em forma de {@link OtherMaterial}
     */
    @Transactional(readOnly = true)
    public OtherMaterial getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todos os outros materiais
     * @return lista de outros materiais em forma de {@link OtherMaterial}
     */
    @Transactional(readOnly = true)
    public List<OtherMaterial> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os outros materiais de forma paginada
     * @param pageable as configurações de paginação
     * @return página de outros materiais em forma de {@link OtherMaterial} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<OtherMaterial> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém outros materiais por tipo de material
     * @param type o tipo de material
     * @return lista de outros materiais em forma de {@link OtherMaterial}
     */
    @Transactional(readOnly = true)
    public List<OtherMaterial> getByType(OtherMaterials type) {
        return repository.findByType(type);
    }

    /**
     * Obtém outros materiais por tipo de material de forma paginada
     * @param type o tipo de material
     * @param pageable as configurações de paginação
     * @return página de outros materiais em forma de {@link OtherMaterial} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<OtherMaterial> getByType(OtherMaterials type, Pageable pageable) {
        return repository.findByType(type, pageable);
    }

    /**
     * Obtém outros materiais por descrição (busca parcial)
     * @param description parte da descrição para buscar
     * @return lista de outros materiais em forma de {@link OtherMaterial}
     */
    @Transactional(readOnly = true)
    public List<OtherMaterial> getByDescriptionContaining(String description) {
        return repository.findByDescriptionContainingIgnoreCase(description);
    }

    /**
     * Obtém outros materiais por descrição (busca parcial) de forma paginada
     * @param description parte da descrição para buscar
     * @param pageable as configurações de paginação
     * @return página de outros materiais em forma de {@link OtherMaterial} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<OtherMaterial> getByDescriptionContaining(String description, Pageable pageable) {
        return repository.findByDescriptionContainingIgnoreCase(description, pageable);
    }

    /**
     * Deleta o outro material com base em seu ID
     * @param id o id do outro material a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o outro material não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Outro material deletado")
                    .content("O outro material com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Outro material não encontrado com o ID " + id);
    }
}
