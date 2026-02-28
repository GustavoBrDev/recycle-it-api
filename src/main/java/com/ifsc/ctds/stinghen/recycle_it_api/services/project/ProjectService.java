package com.ifsc.ctds.stinghen.recycle_it_api.services.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project.ProjectMaterialRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project.ProjectRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.FullProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.ProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.QuickProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.InvalidRelationshipException;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.ProjectRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.services.goals.GoalService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.league.LeagueSessionService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.punctuation.PointsPunctuationService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.goals.RecycleGoalService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.goals.ReduceGoalService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.goals.ReduceItemService;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.RecycleGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.specifications.ProjectSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    public RegularUserService userService;
    public PointsPunctuationService punctuationService;
    public LeagueSessionService leagueService;
    public RecycleGoalService recycleGoalService;
    public ReduceGoalService reduceGoalService;
    public ReduceItemService reduceItemService;
    public GoalService goalService;

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
        existingProject.setMaterials(
                requestDTO.materials.stream()
                        .map(ProjectMaterialRequestDTO::convert)
                        .collect(java.util.stream.Collectors.toList())
        );
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
     * Atualiza a quantidade de um material específico do projeto
     * @param projectId o id do projeto
     * @param materialId o id do material
     * @param newQuantity a nova quantidade
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto ou material não for encontrado
     */
    @Transactional
    public ResponseDTO updateMaterialQuantity(Long projectId, Long materialId, Long newQuantity) {
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Projeto não encontrado com id: " + projectId
                ));

        if (project.getMaterials() == null || project.getMaterials().isEmpty()) {
            throw new EntityNotFoundException("O projeto não possui materiais para atualizar");
        }

        ProjectMaterial materialToUpdate = project.getMaterials().stream()
                .filter(material -> material.getId().equals(materialId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Material não encontrado com id: " + materialId
                ));

        materialToUpdate.setQuantity(newQuantity);
        repository.save(project);

        return FeedbackResponseDTO.builder()
                .mainMessage("Quantidade do material atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Inicia um projeto de reciclagem
     * @param email o e-mail do usuário que iniciou o projeto
     * @param projectId o ID do projeto iniciado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto não for encontrado
     * @throws InvalidRelationshipException caso a relação entre o usuário e o projeto seja inválida (já está realizando o projeto)
     */
    @Transactional
    public ResponseDTO start ( String email, Long projectId ){

       RegularUser user = userService.getObjectByEmail(email);
       userService.addProject(user.getId() ,projectId);

       return FeedbackResponseDTO.builder()
                .mainMessage("Projeto iniciado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();

    }

    /**
     * Finaliza um projeto, aplicando todas as regras de pontuação e atualização de metas
     * [RN-17] Os pontos de reutilização são concedidos ao utilizar um material da lista de redução em um projeto de reciclagem.
     * Cada utilização de material concede 2 pontos com acumulativo até de 50 pontos.
     * @param user o usuário que finalizou o projeto
     * @param projectId o ID do projeto finalizado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o projeto não for encontrado
     * @throws InvalidRelationshipException caso a relação entre o usuário e o projeto seja inválida (não estava realizando o projeto)
     */
    @Transactional
    public ResponseDTO finalize ( RegularUser user, Long projectId ){

        Long userId = user.getId();

        // 1. Remove o projeto do usuário
        userService.removeProject(userId, projectId);
        
        // 2. Obtém o projeto para analisar seus materiais
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com id: " + projectId));

        // 3. Processa pontuação de reciclagem (5 pontos fixos)
        processRecyclingPoints(userId);
        
        // 4. Processa atualização da meta de reciclagem
        processRecycleGoalProgress(userId);

        // 5. Processa pontuação de reutilização baseada nos materiais do projeto
        int reusePoints = processReusePoints(userId, project);

        // 6. Adiciona pontuação na liga
        processLeaguePoints(userId, 5L + reusePoints);

        String message = reusePoints > 0
                ? "Projeto finalizado com sucesso! Você ganhou 5 pontos de reciclagem + " + reusePoints + " pontos de reutilização."
                : "Projeto finalizado com sucesso! Você ganhou 5 pontos de reciclagem.";

        return FeedbackResponseDTO.builder()
                .mainMessage("Projeto finalizado com sucesso")
                .content(message)
                .isAlert(false)
                .isError(false)
                .build();

    }

    /**
     * Obtém todos os materiais de um projeto
     * @param projectId o id do projeto
     * @return lista de materiais do projeto
     * @throws EntityNotFoundException quando o projeto não for encontrado
     */
    @Transactional(readOnly = true)
    public List<ProjectMaterial> getProjectMaterials(Long projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Projeto não encontrado com id: " + projectId
                ));

        return project.getMaterials() != null ? project.getMaterials() : List.of();
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
     * Obtém projetos filtrados através de uma pesquisa inteligente
     * @param search campo de pesquisa
     * @return página de DTOs de projetos filtrados
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getFiltered(String search) {
        return repository.findAll(ProjectSpecification.getFiltered(search), Pageable.unpaged()).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém projetos filtrados através de uma pesquisa inteligente
     * @param search campo de pesquisa
     * @param pageable configurações de paginação
     * @return página de DTOs de projetos filtrados
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getFiltered(String search, Pageable pageable) {
        return repository.findAll(
                ProjectSpecification.getFiltered(search), pageable).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém projetos filtrados através de uma pesquisa inteligente como QuickProjectResponseDTO
     * @param search campo de pesquisa
     * @return página de DTOs de projetos filtrados
     */
    @Transactional(readOnly = true)
    public Page<QuickProjectResponseDTO> getFilteredAsQuick(String search) {
        return repository.findAll(ProjectSpecification.getFiltered(search), Pageable.unpaged()).map(QuickProjectResponseDTO::new);
    }

    /**
     * Obtém projetos filtrados através de uma pesquisa inteligente como QuickProjectResponseDTO
     * @param search campo de pesquisa
     * @param pageable configurações de paginação
     * @return página de DTOs de projetos filtrados
     */
    @Transactional(readOnly = true)
    public Page<QuickProjectResponseDTO> getFilteredAsQuick(String search, Pageable pageable) {
        return repository.findAll(
                ProjectSpecification.getFiltered(search), pageable).map(QuickProjectResponseDTO::new);
    }

    /**
     * Obtém projetos filtrados através de uma pesquisa inteligente como FullProjectResponseDTO
     * @param search campo de pesquisa
     * @return página de DTOs de projetos filtrados
     */
    @Transactional(readOnly = true)
    public Page<FullProjectResponseDTO> getFilteredAsFull(String search) {
        return repository.findAll(ProjectSpecification.getFiltered(search), Pageable.unpaged()).map(FullProjectResponseDTO::new);
    }

    /**
     * Obtém projetos filtrados através de uma pesquisa inteligente como FullProjectResponseDTO
     * @param search campo de pesquisa
     * @param pageable configurações de paginação
     * @return página de DTOs de projetos filtrados
     */
    @Transactional(readOnly = true)
    public Page<FullProjectResponseDTO> getFilteredAsFull(String search, Pageable pageable) {
        return repository.findAll(
                ProjectSpecification.getFiltered(search), pageable).map(FullProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados com base nos materiais do usuário
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @return página de DTOs de projetos recomendados
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getRecommendedByMaterials(Map<Materials, Long> userMaterials) {
        return repository.findAll(ProjectSpecification.getRecommendedByMaterials(userMaterials), Pageable.unpaged()).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados com base nos materiais do usuário (paginado)
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @param pageable configurações de paginação
     * @return página de DTOs de projetos recomendados
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getRecommendedByMaterials(Map<Materials, Long> userMaterials, Pageable pageable) {
        return repository.findAll(ProjectSpecification.getRecommendedByMaterials(userMaterials), pageable).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados com base nos materiais do usuário com tolerância
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @param tolerancePercent percentual de tolerância (ex: 20 para aceitar até 20% a mais)
     * @return página de DTOs de projetos recomendados
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getRecommendedByMaterialsWithTolerance(Map<Materials, Long> userMaterials, int tolerancePercent) {
        return repository.findAll(ProjectSpecification.getRecommendedByMaterialsWithTolerance(userMaterials, tolerancePercent), Pageable.unpaged()).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados com base nos materiais do usuário com tolerância (paginado)
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @param tolerancePercent percentual de tolerância (ex: 20 para aceitar até 20% a mais)
     * @param pageable configurações de paginação
     * @return página de DTOs de projetos recomendados
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getRecommendedByMaterialsWithTolerance(Map<Materials, Long> userMaterials, int tolerancePercent, Pageable pageable) {
        return repository.findAll(ProjectSpecification.getRecommendedByMaterialsWithTolerance(userMaterials, tolerancePercent), pageable).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados e filtrados combinando busca inteligente e materiais
     * @param search termo de busca inteligente
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @return página de DTOs de projetos recomendados e filtrados
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getFilteredAndRecommended(String search, Map<Materials, Long> userMaterials) {
        return repository.findAll(ProjectSpecification.getFilteredAndRecommended(search, userMaterials), Pageable.unpaged()).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados e filtrados combinando busca inteligente e materiais (paginado)
     * @param search termo de busca inteligente
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @param pageable configurações de paginação
     * @return página de DTOs de projetos recomendados e filtrados
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> getFilteredAndRecommended(String search, Map<Materials, Long> userMaterials, Pageable pageable) {
        return repository.findAll(ProjectSpecification.getFilteredAndRecommended(search, userMaterials), pageable).map(ProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados como QuickProjectResponseDTO
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @return página de DTOs quick de projetos recomendados
     */
    @Transactional(readOnly = true)
    public Page<QuickProjectResponseDTO> getRecommendedByMaterialsAsQuick(Map<Materials, Long> userMaterials) {
        return repository.findAll(ProjectSpecification.getRecommendedByMaterials(userMaterials), Pageable.unpaged()).map(QuickProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados como QuickProjectResponseDTO (paginado)
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @param pageable configurações de paginação
     * @return página de DTOs quick de projetos recomendados
     */
    @Transactional(readOnly = true)
    public Page<QuickProjectResponseDTO> getRecommendedByMaterialsAsQuick(Map<Materials, Long> userMaterials, Pageable pageable) {
        return repository.findAll(ProjectSpecification.getRecommendedByMaterials(userMaterials), pageable).map(QuickProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados como FullProjectResponseDTO
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @return página de DTOs full de projetos recomendados
     */
    @Transactional(readOnly = true)
    public Page<FullProjectResponseDTO> getRecommendedByMaterialsAsFull(Map<Materials, Long> userMaterials) {
        return repository.findAll(ProjectSpecification.getRecommendedByMaterials(userMaterials), Pageable.unpaged()).map(FullProjectResponseDTO::new);
    }

    /**
     * Obtém projetos recomendados como FullProjectResponseDTO (paginado)
     * @param userMaterials mapa de materiais do usuário (tipo -> quantidade)
     * @param pageable configurações de paginação
     * @return página de DTOs full de projetos recomendados
     */
    @Transactional(readOnly = true)
    public Page<FullProjectResponseDTO> getRecommendedByMaterialsAsFull(Map<Materials, Long> userMaterials, Pageable pageable) {
        return repository.findAll(ProjectSpecification.getRecommendedByMaterials(userMaterials), pageable).map(FullProjectResponseDTO::new);
    }

    /**
     * Deleta o projeto com base no seu ID
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

    // ============= MÉTODOS AUXILIARES DE PONTUAÇÃO =============

    /**
     * Processa a concessão de pontos de reciclagem para o usuário
     * @param userId ID do usuário que receberá os pontos
     */
    private void processRecyclingPoints(Long userId) {
        punctuationService.incrementRecyclePoints(userId, 5L);
    }

    /**
     * Processa a atualização do progresso da meta de reciclagem ativa do usuário
     * @param userId ID do usuário cuja meta será atualizada
     */
    private void processRecycleGoalProgress(Long userId) {
        try {
            var activeRecycleGoals = recycleGoalService.getActiveByUserId(userId);
            if (!activeRecycleGoals.isEmpty()) {
                RecycleGoal activeGoal = activeRecycleGoals.get(0);
                goalService.incrementProgress(activeGoal.getId(), 1.0f);
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar meta de reciclagem: " + e.getMessage());
        }
    }

    /**
     * Processa a pontuação de reutilização baseada nos materiais do projeto
     * [RN-17] Cada utilização de material da lista de redução concede 2 pontos (máximo 50 pontos)
     * @param userId ID do usuário que receberá os pontos
     * @param project Projeto cujos materiais serão analisados
     * @return Total de pontos de reutilização concedidos
     */
    private int processReusePoints(Long userId, Project project) {
        int totalReusePoints = 0;

        try {
            var activeReduceGoals = reduceGoalService.getActiveByUserId(userId);

            if (activeReduceGoals.isEmpty()) {
                return 0;
            }

            ReduceGoal activeReduceGoal = activeReduceGoals.getFirst();

            if (project.getMaterials() != null) {
                for (ProjectMaterial projectMaterial : project.getMaterials()) {
                    int materialPoints = calculateMaterialReusePoints(projectMaterial, activeReduceGoal);

                    if (materialPoints > 0) {
                        punctuationService.incrementRecyclePoints(userId, (long) materialPoints);
                        totalReusePoints += materialPoints;

                        if (totalReusePoints >= 50) {
                            totalReusePoints = 50;
                            break;
                        }
                    }
                }
            }

            if (totalReusePoints > 0) {
                updateReduceGoalProgress(userId, totalReusePoints / 2);
            }

        } catch (Exception e) {
            System.err.println("Erro ao processar pontos de reutilização: " + e.getMessage());
        }

        return totalReusePoints;
    }

    /**
     * Calcula os pontos de reutilização para um material específico do projeto
     * @param projectMaterial Material do projeto a ser analisado
     * @param reduceGoal Meta de redução ativa do usuário
     * @return Pontos de reutilização (2 pontos por material compatível)
     */
    private int calculateMaterialReusePoints(ProjectMaterial projectMaterial, ReduceGoal reduceGoal) {
        if (reduceGoal.getItems() == null || reduceGoal.getItems().isEmpty()) {
            return 0;
        }

        for (var reduceItem : reduceGoal.getItems()) {
            if (isMaterialCompatible(projectMaterial, reduceItem)) {
                try {
                    reduceItemService.increment(reduceItem.getId());
                    return 2;
                } catch (Exception e) {
                    System.err.println("Erro ao incrementar item de redução: " + e.getMessage());
                    return 0;
                }
            }
        }

        return 0;
    }

    /**
     * Verifica se um material do projeto é compatível com um item da meta de redução
     * @param projectMaterial Material do projeto
     * @param reduceItem Item da meta de redução
     * @return true se forem compatíveis
     */
    private boolean isMaterialCompatible(ProjectMaterial projectMaterial, ReduceItem reduceItem) {
        if (projectMaterial instanceof RecycledMaterial recycledMaterial) {
            return recycledMaterial.getType() == reduceItem.getType();
        }
        return false;
    }

    /**
     * Atualiza o progresso da meta de redução com base nos materiais reutilizados
     * [RN-REDUCE] Materiais reutilizados incrementam o consumo atual (actualQuantity) dos itens
     * O progresso é recalculado automaticamente com base na porcentagem de redução alcançada
     * @param userId ID do usuário
     * @param materialsReused Quantidade de materiais reutilizados
     */
    private void updateReduceGoalProgress(Long userId, int materialsReused) {
        try {
            var activeReduceGoals = reduceGoalService.getActiveByUserId(userId);
            if (!activeReduceGoals.isEmpty()) {
                ReduceGoal activeGoal = activeReduceGoals.getFirst();

                // O progresso já foi atualizado nos itens através do método calculateMaterialReusePoints
                // que chamou reduceItemService.increment() para cada material compatível
                // Agora precisamos apenas recalcular o progresso total da meta
                float newProgress = goalService.calculateReduceGoalProgress(activeGoal);

                // Atualiza o progresso da meta com o novo valor calculado
                goalService.incrementProgress(activeGoal.getId(), newProgress - (activeGoal.getProgress() != null ? activeGoal.getProgress() : 0.0f));
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar progresso da meta de redução: " + e.getMessage());
        }
    }

    /**
     * Processa a adição de pontos na liga do usuário
     * @param userId ID do usuário
     * @param totalPoints Total de pontos a serem adicionados
     */
    private void processLeaguePoints(Long userId, Long totalPoints) {
        try {
            PointsPunctuation leaguePunctuation = leagueService.getActivePointsPunctuationByUserId(userId);
            punctuationService.incrementRecyclePoints(leaguePunctuation.getId(), totalPoints);
        } catch (Exception e) {
            // Usuário não está em liga ativa - ignora silenciosamente
        }
    }
}
