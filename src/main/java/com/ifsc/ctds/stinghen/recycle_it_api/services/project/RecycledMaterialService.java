package com.ifsc.ctds.stinghen.recycle_it_api.services.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project.RecycledMaterialRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project.RecycledMaterialPutRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.RecycledMaterialRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de materiais reciclados
 * @author Gustavo Stinghen
 * @see RecycledMaterial
 * @since 22/02/2026
 */
@RequiredArgsConstructor
@Service
public class RecycledMaterialService {

    @Autowired
    public RecycledMaterialRepository repository;

    @Autowired
    @Lazy
    public ProjectService projectService;

    /**
     * Cria um novo material reciclado
     * @param dto o material reciclado a ser criado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto não for encontrado
     */
    @Transactional
    public ResponseDTO create(RecycledMaterialRequestDTO dto) {
        RecycledMaterial recycledMaterial = dto.convert();
        recycledMaterial.setProject(projectService.getObjectById(dto.projectId));
        repository.save(recycledMaterial);

        return FeedbackResponseDTO.builder()
                .mainMessage("Material reciclado criado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o tipo de material de um material reciclado
     * @param id o id do material reciclado
     * @param type o novo tipo de material
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o material reciclado não for encontrado
     */
    @Transactional
    public ResponseDTO editType(Long id, Materials type) {
        if (repository.existsById(id)) {
            RecycledMaterial recycledMaterial = repository.findById(id).get();
            recycledMaterial.setType(type);
            repository.save(recycledMaterial);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Material reciclado atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Material reciclado não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de RecycledMaterial pelo id fornecido
     * @param id o id a ser buscado
     * @return o material reciclado em forma de {@link RecycledMaterial}
     */
    @Transactional(readOnly = true)
    public RecycledMaterial getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todos os materiais reciclados
     * @return lista de materiais reciclados em forma de {@link RecycledMaterial}
     */
    @Transactional(readOnly = true)
    public List<RecycledMaterial> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os materiais reciclados de forma paginada
     * @param pageable as configurações de paginação
     * @return página de materiais reciclados em forma de {@link RecycledMaterial} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<RecycledMaterial> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém materiais reciclados por tipo de material
     * @param type o tipo de material
     * @return lista de materiais reciclados em forma de {@link RecycledMaterial}
     */
    @Transactional(readOnly = true)
    public List<RecycledMaterial> getByType(Materials type) {
        return repository.findByType(type);
    }

    /**
     * Obtém materiais reciclados por tipo de material de forma paginada
     * @param type o tipo de material
     * @param pageable as configurações de paginação
     * @return página de materiais reciclados em forma de {@link RecycledMaterial} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<RecycledMaterial> getByType(Materials type, Pageable pageable) {
        return repository.findByType(type, pageable);
    }

    /**
     * Deleta o material reciclado com base em seu ID
     * @param id o id do material reciclado a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o material reciclado não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Material reciclado deletado")
                    .content("O material reciclado com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Material reciclado não encontrado com o ID " + id);
    }

    /**
     * Atualiza um material reciclado existente
     * @param id o id do material reciclado a ser atualizado
     * @param dto o DTO com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o material reciclado não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, RecycledMaterialPutRequestDTO dto) {
        RecycledMaterial existingMaterial = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material reciclado não encontrado com id: " + id));
        
        existingMaterial.setType(dto.type);
        existingMaterial.setQuantity(dto.quantity);
        repository.save(existingMaterial);

        return FeedbackResponseDTO.builder()
                .mainMessage("Material reciclado atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }


}
