package com.ifsc.ctds.stinghen.recycle_it_api.services.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals.RecycleGoalRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.RecycleGoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalStatus;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.RecycleGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.goals.RecycleGoalRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para os métodos específicos de metas de reciclagem
 * @author Gustavo Stinghen
 * @see RecycleGoal
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class RecycleGoalService {

    public RecycleGoalRepository repository;
    public RegularUserService userService;
    public GoalService goalService;


    /**
     * Cria/persiste o registro de uma meta de reciclagem no banco de dados
     * Verifica se o usuário já tem uma meta ativa do mesmo tipo
     * @param requestDTO DTO de request de metas de reciclagem
     * @param email o e-mail do usuário possuidor da meta
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(RecycleGoalRequestDTO requestDTO, String email) {
        RecycleGoal goal = requestDTO.convert();
        RegularUser user = userService.getObjectByEmail(email);
        goal.setUser(user);
        Long userId = user.getId();
        
        // Verifica se já existe uma meta ativa do mesmo tipo para o usuário
        List<RecycleGoal> activeGoals = getActiveByUserId(userId);
        
        if (!activeGoals.isEmpty()) {
            // Se já existe meta ativa, define a nova como "next"
            goal.setStatus(GoalStatus.NEXT);
            
            // Verifica se já existe uma meta "next" para atualizar
            List<RecycleGoal> nextGoals = getNextByUserId(userId);
            if (!nextGoals.isEmpty()) {
                // Atualiza a meta "next" existente
                RecycleGoal existingNextGoal = nextGoals.get(0);
                existingNextGoal.setDifficult(goal.getDifficult());
                existingNextGoal.setFrequency(goal.getFrequency());
                existingNextGoal.setMultiplier(goal.getMultiplier());
                existingNextGoal.setNextCheck(goal.getNextCheck());
                existingNextGoal.setProgress(goal.getProgress());
                repository.save(existingNextGoal);
                
                return FeedbackResponseDTO.builder()
                        .mainMessage("Meta futura atualizada")
                        .content("Sua meta de reciclagem foi atualizada com sucesso")
                        .isAlert(false)
                        .isError(false)
                        .build();
            }
        } else {
            // Se não existe meta ativa, define como "actual"
            goal.setStatus(GoalStatus.ACTUAL);
        }
        
        repository.save(goal);

        String statusMessage = goal.getStatus() == GoalStatus.ACTUAL ? "ativa" : "'futura'";
        return FeedbackResponseDTO.builder()
                .mainMessage("Meta de reciclagem " + statusMessage + " salva com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza uma meta de reciclagem existente
     * @param id o id da meta
     * @param requestDTO DTO com os dados atualizados da meta
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO update(Long id, RecycleGoalRequestDTO requestDTO) {
        RecycleGoal existingGoal = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Meta de reciclagem não encontrada com id: " + id
                ));

        existingGoal.setDifficult(requestDTO.goalDifficult);
        existingGoal.setFrequency(requestDTO.goalFrequency);

        repository.save(existingGoal);

        return FeedbackResponseDTO.builder()
                .mainMessage("Meta de reciclagem atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }


    /**
     * Obtem o objeto de RecycleGoal pelo id fornecido
     * @param id o id a ser buscado
     * @return a meta em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public RecycleGoal getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de uma meta de reciclagem pelo id fornecido
     * @param id o id a ser buscado
     * @return a meta em forma da DTO {@link RecycleGoalResponseDTO}
     * @throws NotFoundException quando a meta não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new RecycleGoalResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Meta de reciclagem não encontrada com o ID " + id);
    }

    /**
     * Obtém todas as metas de reciclagem
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as metas de reciclagem de forma paginada
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém todas as metas de reciclagem ativas (sem paginação)
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getActive() {
        return repository.findByStatus(GoalStatus.ACTUAL);
    }

    /**
     * Obtém todas as metas de reciclagem ativas (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getActive(Pageable pageable) {
        return repository.findByStatus(GoalStatus.ACTUAL, pageable);
    }

    /**
     * Obtém todas as metas de reciclagem inativas (sem paginação)
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getInactive() {
        return repository.findByStatus(GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas de reciclagem inativas (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getInactive(Pageable pageable) {
        return repository.findByStatus(GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas de reciclagem "next" (sem paginação)
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getNext() {
        return repository.findByStatus(GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas de reciclagem "next" (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getNext(Pageable pageable) {
        return repository.findByStatus(GoalStatus.NEXT, pageable);
    }

    // ============= MÉTODOS POR STATUS E USUÁRIO ID =============

    /**
     * Obtém todas as metas de reciclagem ativas de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getActiveByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.ACTUAL);
    }

    /**
     * Obtém todas as metas de reciclagem ativas de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getActiveByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.ACTUAL, pageable);
    }

    /**
     * Obtém todas as metas de reciclagem inativas de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getInactiveByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas de reciclagem inativas de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getInactiveByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas de reciclagem "next" de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getNextByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas de reciclagem "next" de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getNextByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.NEXT, pageable);
    }

    /**
     * Obtém todas as metas de reciclagem ativas de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getActiveByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.ACTUAL);
    }

    /**
     * Obtém todas as metas de reciclagem ativas de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getActiveByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.ACTUAL, pageable);
    }

    /**
     * Obtém todas as metas de reciclagem inativas de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getInactiveByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas de reciclagem inativas de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getInactiveByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas de reciclagem "next" de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public List<RecycleGoal> getNextByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas de reciclagem "next" de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link RecycleGoal}
     */
    @Transactional(readOnly = true)
    public Page<RecycleGoal> getNextByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.NEXT, pageable);
    }

    /**
     * Deleta a meta de reciclagem com base em seu ID
     * @param id o id da meta a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta de reciclagem deletada")
                    .content("A meta de reciclagem com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta de reciclagem não encontrada com o ID " + id);
    }

    /**
     * Processa o impacto na meta de reciclagem quando um projeto é finalizado
     * Adiciona o projeto à lista de projetos finalizados e atualiza o progresso
     * @param userId ID do usuário
     * @param project Projeto finalizado (do tipo {@link Project})
     * @return progresso atualizado da meta (0.0 se não houver meta ativa)
     */
    @Transactional
    public float processProjectCompletion(Long userId, Project project) {
        try {
            var activeGoals = getActiveByUserId(userId);
            if (activeGoals.isEmpty()) {
                return 0.0f;
            }
            
            RecycleGoal activeGoal = activeGoals.getFirst();
            
            // Adiciona o projeto à lista de projetos finalizados
            if (activeGoal.getFinishedProjects() == null) {
                activeGoal.setFinishedProjects(new java.util.ArrayList<>());
            }

            activeGoal.getFinishedProjects().add(project);
            
            // Recalcula e atualiza o progresso
            float progress = calculateProgress(activeGoal);
            
            if (progress != 0) {
                goalService.editProgress(activeGoal.getId(), progress);
            }
            
            return progress;
            
        } catch (Exception e) {
            System.err.println("Erro ao processar meta de reciclagem: " + e.getMessage());
            return 0.0f;
        }
    }

    /**
     * Calcula o progresso de uma meta de reciclagem com base na dificuldade
     * * @param recycleGoal meta de reciclagem
     * @return progresso calculado (0.0 a 100.0)
     */
    @Transactional(readOnly = true)
    public float calculateProgress(RecycleGoal recycleGoal) {
        if (recycleGoal == null) {
            return 0.0f;
        }

        // Obtém o alvo de projetos com base na dificuldade
        int targetProjects = getTargetByDifficulty(recycleGoal.getDifficult());
        
        // Obtém a quantidade de projetos realizados
        int finishedProjects = recycleGoal.getFinishedProjects() != null ? recycleGoal.getFinishedProjects().size() : 0;
        
        // Calcula o progresso como projetos realizados / alvo
        if (targetProjects <= 0) {
            return 0.0f;
        }
        
        float progress = (float) finishedProjects / targetProjects;
        return Math.min(100.0f, Math.max(0.0f, progress * 100));
    }

    /**
     * Obtém o alvo de projetos de reciclagem com base na dificuldade
     * @param difficult a dificuldade da meta
     * @return quantidade alvo de projetos mensais
     */
    private int getTargetByDifficulty(GoalDifficult difficult) {
        if (difficult == null) {
            return 1; // Padrão: Normal
        }
        
        return switch (difficult) {
            case normal -> 1;
            case extraJob -> 2;
            case difficult -> 4;
            case hard -> 6;
            default -> 1;
        };
    }
}
