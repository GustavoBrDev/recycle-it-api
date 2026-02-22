package com.ifsc.ctds.stinghen.recycle_it_api.services.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project.ProjectRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.FullProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.ProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.QuickProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de projetos
 * @author Gustavo Stinghen
 * @see Project
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class ProjectService {

    public ProjectRepository repository;

    /**
     * Cria/persiste o registro de um projeto no banco de dados
     * @param requestDTO DTO de request de projetos
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(ProjectRequestDTO requestDTO) {
        Project project = requestDTO.convert();
        repository.save(project);

        return FeedbackResponseDTO.builder()
                .mainMessage("Projeto criado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza um projeto existente
     * @param id o id do projeto
     * @param requestDTO DTO com os dados atualizados do projeto
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, ProjectRequestDTO requestDTO) {
        Project existingProject = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Projeto não encontrado com id: " + id
                ));

        existingProject.setText(requestDTO.text);
        existingProject.setDescription(requestDTO.description);
        // TODO: Analisar esse update
        existingProject.setInstructions(requestDTO.instructions);

        repository.save(existingProject);

        return FeedbackResponseDTO.builder()
                .mainMessage("Projeto atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o texto de um projeto
     * @param id o id do projeto
     * @param text o novo texto
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto não for encontrado
     */
    @Transactional
    public ResponseDTO editText(Long id, String text) {
        if (repository.existsById(id)) {
            Project project = repository.findById(id).get();
            project.setText(text);
            repository.save(project);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Projeto atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Projeto não encontrado com id: " + id);
    }

    /**
     * Atualiza a descrição de um projeto
     * @param id o id do projeto
     * @param description a nova descrição
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto não for encontrado
     */
    @Transactional
    public ResponseDTO editDescription(Long id, String description) {
        if (repository.existsById(id)) {
            Project project = repository.findById(id).get();
            project.setDescription(description);
            repository.save(project);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Projeto atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Projeto não encontrado com id: " + id);
    }

    /**
     * Atualiza as instruções de um projeto
     * @param id o id do projeto
     * @param instructions as novas instruções
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto não for encontrado
     */
    @Transactional
    public ResponseDTO editInstructions(Long id, String instructions) {
        if (repository.existsById(id)) {
            Project project = repository.findById(id).get();
            project.setInstructions(instructions);
            repository.save(project);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Projeto atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Projeto não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de Projeto pelo id fornecido
     * @param id o id a ser buscado
     * @return o projeto em forma de {@link Project}
     */
    @Transactional(readOnly = true)
    public Project getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response completa de um projeto pelo id fornecido
     * @param id o id a ser buscado
     * @return o projeto em forma da DTO {@link FullProjectResponseDTO}
     * @throws NotFoundException quando o projeto não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getFullById(Long id) {
        if (repository.existsById(id)) {
            return new FullProjectResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Projeto não encontrado com o ID " + id);
    }

    /**
     * Obtém a response padrão de um projeto pelo id fornecido
     * @param id o id a ser buscado
     * @return o projeto em forma da DTO {@link ProjectResponseDTO}
     * @throws NotFoundException quando o projeto não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getResponseById(Long id) {
        if (repository.existsById(id)) {
            return new ProjectResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Projeto não encontrado com o ID " + id);
    }

    /**
     * Obtém a response rápida de um projeto pelo id fornecido
     * @param id o id a ser buscado
     * @return o projeto em forma da DTO {@link QuickProjectResponseDTO}
     * @throws NotFoundException quando o projeto não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getQuickById(Long id) {
        if (repository.existsById(id)) {
            return new QuickProjectResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Projeto não encontrado com o ID " + id);
    }

    /**
     * Obtém todos os projetos
     * @return lista de projetos em forma de {@link Project}
     */
    @Transactional(readOnly = true)
    public List<Project> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os projetos de forma rápida
     * @return lista de projetos em forma de {@link QuickProjectResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<QuickProjectResponseDTO> getAllQuick() {
        return repository.findAll().stream().map(QuickProjectResponseDTO::new).toList();
    }

    /**
     * Obtém todos os projetos de forma padrão
     * @return lista de projetos em forma de {@link ProjectResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAllResponse() {
        return repository.findAll().stream().map(ProjectResponseDTO::new).toList();
    }

    /**
     * Obtém todos os projetos de forma completa
     * @return lista de projetos em forma de {@link FullProjectResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<FullProjectResponseDTO> getAllFull() {
        return repository.findAll().stream().map(FullProjectResponseDTO::new).toList();
    }

    /**
     * Obtém todos os projetos de forma paginada
     * @param pageable as configurações de paginação
     * @return página de projetos em forma de {@link Project} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<Project> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém todos os projetos de forma rápida e paginada
     * @param pageable as configurações de paginação
     * @return página de projetos em forma de {@link QuickProjectResponseDTO} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<QuickProjectResponseDTO> getAllQuick(Pageable pageable) {
        return repository.findAll(pageable).map(QuickProjectResponseDTO::new);
    }

    /**
     * Obtém todos os projetos de forma padrão e paginada
     * @param pageable as configurações de paginação
     * @return página de projetos em forma de {@link ProjectResponseDTO} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getAllResponse(Pageable pageable) {
        return repository.findAll(pageable).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém todos os projetos de forma completa e paginada
     * @param pageable as configurações de paginação
     * @return página de projetos em forma de {@link FullProjectResponseDTO} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<FullProjectResponseDTO> getAllFull(Pageable pageable) {
        return repository.findAll(pageable).map(FullProjectResponseDTO::new);
    }

    /**
     * Deleta o projeto com base em seu ID
     * @param id o id do projeto a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Projeto deletado")
                    .content("O projeto com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Projeto não encontrado com o ID " + id);
    }
}
