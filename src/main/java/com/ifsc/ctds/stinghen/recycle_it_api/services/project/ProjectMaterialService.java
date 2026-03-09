package com.ifsc.ctds.stinghen.recycle_it_api.services.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.OtherMaterialRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.ProjectMaterialRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.RecycledMaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * Service para os métodos específicos de materiais de projetos
 * @author Gustavo Stinghen
 * @see ProjectMaterial
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class ProjectMaterialService {

    public ProjectMaterialRepository repository;
    public RecycledMaterialRepository recycledMaterialRepository;
    public OtherMaterialRepository otherMaterialRepository;

    /**
     * Atualiza um material de projeto existente
     * @param id o id do material de projeto
     * @param projectMaterial o material de projeto com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o material de projeto não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, ProjectMaterial projectMaterial) {
        ProjectMaterial existingProjectMaterial = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Material de projeto não encontrado com id: " + id
                ));

        existingProjectMaterial.setQuantity(projectMaterial.getQuantity());

        repository.save(existingProjectMaterial);

        return FeedbackResponseDTO.builder()
                .mainMessage("Material de projeto atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza a quantidade de um material de projeto
     * @param id o id do material de projeto
     * @param quantity a nova quantidade
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o material de projeto não for encontrado
     */
    @Transactional
    public ResponseDTO editQuantity(Long id, Long quantity) {
        if (repository.existsById(id)) {
            ProjectMaterial projectMaterial = repository.findById(id).get();
            projectMaterial.setQuantity(quantity);
            repository.save(projectMaterial);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Material de projeto atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Material de projeto não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de ProjectMaterial pelo id fornecido
     * @param id o id a ser buscado
     * @return o material de projeto em forma de {@link ProjectMaterial}
     */
    @Transactional(readOnly = true)
    public ProjectMaterial getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todos os materiais de projeto
     * @return lista de materiais de projeto em forma de {@link ProjectMaterial}
     */
    @Transactional(readOnly = true)
    public List<ProjectMaterial> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os materiais de projeto de forma paginada
     * @param pageable as configurações de paginação
     * @return página de materiais de projeto em forma de {@link ProjectMaterial} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ProjectMaterial> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém materiais de projeto por quantidade mínima
     * @param minQuantity a quantidade mínima
     * @return lista de materiais de projeto em forma de {@link ProjectMaterial}
     */
    @Transactional(readOnly = true)
    public List<ProjectMaterial> getByMinQuantity(Long minQuantity) {
        return repository.findByQuantityGreaterThanEqual(minQuantity);
    }

    /**
     * Obtém materiais de projeto por quantidade mínima de forma paginada
     * @param minQuantity a quantidade mínima
     * @param pageable as configurações de paginação
     * @return página de materiais de projeto em forma de {@link ProjectMaterial} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ProjectMaterial> getByMinQuantity(Long minQuantity, Pageable pageable) {
        return repository.findByQuantityGreaterThanEqual(minQuantity, pageable);
    }

    /**
     * Obtém todos os materiais de um projeto pelo ID do projeto
     * @param projectId o ID do projeto
     * @return lista de materiais do projeto em forma de {@link ProjectMaterial}
     */
    @Transactional(readOnly = true)
    public List<ProjectMaterial> getByProjectId(Long projectId) {
        List<RecycledMaterial> recycledMaterials = recycledMaterialRepository.findAll().stream()
                .filter(rm -> rm.getProject() != null && rm.getProject().getId().equals(projectId))
                .toList();
        
        List<OtherMaterial> otherMaterials = otherMaterialRepository.findAll().stream()
                .filter(om -> om.getProject() != null && om.getProject().getId().equals(projectId))
                .toList();
        
        return Stream.concat(recycledMaterials.stream(), otherMaterials.stream()).toList();
    }

    /**
     * Obtém todos os materiais de um projeto pelo ID do projeto de forma paginada
     * @param projectId o ID do projeto
     * @param pageable as configurações de paginação
     * @return página de materiais do projeto em forma de {@link ProjectMaterial} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ProjectMaterial> getByProjectId(Long projectId, Pageable pageable) {
        List<RecycledMaterial> recycledMaterials = recycledMaterialRepository.findAll().stream()
                .filter(rm -> rm.getProject() != null && rm.getProject().getId().equals(projectId))
                .toList();
        
        List<OtherMaterial> otherMaterials = otherMaterialRepository.findAll().stream()
                .filter(om -> om.getProject() != null && om.getProject().getId().equals(projectId))
                .toList();
        
        List<ProjectMaterial> allMaterials = Stream.concat(recycledMaterials.stream(), otherMaterials.stream()).toList();
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allMaterials.size());
        List<ProjectMaterial> subList = allMaterials.subList(start, end);
        
        return new PageImpl<>(subList, pageable, allMaterials.size());
    }

    /**
     * Deleta o material de projeto com base em seu ID
     * @param id o id do material de projeto a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o material de projeto não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Material de projeto deletado")
                    .content("O material de projeto com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Material de projeto não encontrado com o ID " + id);
    }
}
