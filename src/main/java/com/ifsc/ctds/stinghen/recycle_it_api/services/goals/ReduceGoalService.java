package com.ifsc.ctds.stinghen.recycle_it_api.services.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals.ReduceGoalRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.ReduceGoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalStatus;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.goals.ReduceGoalRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service para os métodos específicos de metas de redução
 * @author Gustavo Stinghen
 * @see ReduceGoal
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class ReduceGoalService {

    public ReduceGoalRepository repository;
    public RegularUserService userService;

    /**
     * Cria/persiste o registro de uma meta de redução no banco de dados
     * Verifica se o usuário já tem uma meta ativa do mesmo tipo
     * @param requestDTO DTO de request de metas de redução
     * @param email o e-mail do usuário possuidor da meta
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(ReduceGoalRequestDTO requestDTO, String email) {
        ReduceGoal goal = requestDTO.convert();
        RegularUser user = userService.getObjectByEmail(email);
        goal.setUser(user);
        Long userId = user.getId();
        
        // Verifica se já existe uma meta ativa do mesmo tipo para o usuário
        List<ReduceGoal> activeGoals = getActiveByUserId(userId);
        
        if (!activeGoals.isEmpty()) {
            // Se já existe meta ativa, define a nova como "next"
            goal.setStatus(GoalStatus.NEXT);
            
            // Verifica se já existe uma meta "next" para atualizar
            List<ReduceGoal> nextGoals = getNextByUserId(userId);
            if (!nextGoals.isEmpty()) {
                // Atualiza a meta "next" existente
                ReduceGoal existingNextGoal = nextGoals.getFirst();
                existingNextGoal.setDifficult(goal.getDifficult());
                existingNextGoal.setFrequency(goal.getFrequency());
                existingNextGoal.setMultiplier(goal.getMultiplier());
                existingNextGoal.setNextCheck(goal.getNextCheck());
                existingNextGoal.setProgress(goal.getProgress());
                repository.save(existingNextGoal);

                return FeedbackResponseDTO.builder()
                        .mainMessage("Meta futura atualizada")
                        .content("Sua meta de redução foi atualizada com sucesso")
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
                .mainMessage("Meta de redução " + statusMessage + " salva com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza uma meta de redução existente
     * @param id o id da meta
     * @param requestDTO DTO com os dados atualizados da meta
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO update(Long id, ReduceGoalRequestDTO requestDTO) {
        ReduceGoal existingGoal = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Meta de redução não encontrada com id: " + id
                ));

        existingGoal.setDifficult(requestDTO.goalDifficult);
        existingGoal.setFrequency(requestDTO.goalFrequency);
        // TODO: Analisar esse update de items

        repository.save(existingGoal);

        return FeedbackResponseDTO.builder()
                .mainMessage("Meta de redução atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }


    /**
     * Atualiza os dias de pulo restantes de uma meta de redução
     * @param id o id da meta
     * @param skipDaysLeft a nova quantidade de dias de pulo restantes
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO editSkipDaysLeft(Long id, int skipDaysLeft) {
        if (repository.existsById(id)) {
            ReduceGoal goal = repository.findById(id).get();
            goal.setSkipDaysLeft(skipDaysLeft);
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta de redução atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta de redução não encontrada com id: " + id);
    }

    /**
     * Decrementa os dias de pulo restantes de uma meta de redução
     * @param id o id da meta
     * @param amount a quantidade a ser decrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO decrementSkipDaysLeft(Long id, int amount) {
        if (repository.existsById(id)) {
            ReduceGoal goal = repository.findById(id).get();
            goal.setSkipDaysLeft(Math.max(0, goal.getSkipDaysLeft() - amount));
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta de redução atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta de redução não encontrada com id: " + id);
    }

    /**
     * Obtem o objeto de ReduceGoal pelo id fornecido
     * @param id o id a ser buscado
     * @return a meta em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public ReduceGoal getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de uma meta de redução pelo id fornecido
     * @param id o id a ser buscado
     * @return a meta em forma da DTO {@link ReduceGoalResponseDTO}
     * @throws NotFoundException quando a meta não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new ReduceGoalResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Meta de redução não encontrada com o ID " + id);
    }

    /**
     * Obtém todas as metas de redução
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as metas de redução de forma paginada
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém todas as metas de redução ativas (sem paginação)
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getActive() {
        return repository.findByStatus(GoalStatus.ACTUAL);
    }

    /**
     * Obtém todas as metas de redução ativas (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getActive(Pageable pageable) {
        return repository.findByStatus(GoalStatus.ACTUAL, pageable);
    }

    /**
     * Obtém todas as metas de redução inativas (sem paginação)
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getInactive() {
        return repository.findByStatus(GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas de redução inativas (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getInactive(Pageable pageable) {
        return repository.findByStatus(GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas de redução "next" (sem paginação)
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getNext() {
        return repository.findByStatus(GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas de redução "next" (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getNext(Pageable pageable) {
        return repository.findByStatus(GoalStatus.NEXT, pageable);
    }

    /**
     * Obtém todas as metas de redução ativas de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getActiveByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.ACTUAL);
    }

    /**
     * Obtém todas as metas de redução ativas de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getActiveByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.ACTUAL, pageable);
    }

    /**
     * Obtém todas as metas de redução inativas de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getInactiveByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas de redução inativas de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getInactiveByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas de redução "next" de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getNextByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas de redução "next" de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getNextByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.NEXT, pageable);
    }

    /**
     * Obtém todas as metas de redução ativas de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getActiveByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.ACTUAL);
    }

    /**
     * Obtém todas as metas de redução ativas de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getActiveByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.ACTUAL, pageable);
    }

    /**
     * Obtém todas as metas de redução inativas de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getInactiveByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas de redução inativas de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getInactiveByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas de redução "next" de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public List<ReduceGoal> getNextByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas de redução "next" de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link ReduceGoal}
     */
    @Transactional(readOnly = true)
    public Page<ReduceGoal> getNextByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.NEXT, pageable);
    }

    /**
     * Deleta a meta de redução com base em seu ID
     * @param id o id da meta a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta de redução deletada")
                    .content("A meta de redução com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta de redução não encontrada com o ID " + id);
    }
}
