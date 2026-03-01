package com.ifsc.ctds.stinghen.recycle_it_api.services.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.GoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalStatus;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.Goal;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.goals.GoalRepository;
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
 * Service para os métodos específicos de metas
 * @author Gustavo Stinghen
 * @see Goal
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class GoalService {

    public GoalRepository repository;

    /**
     * Atualiza o progresso de uma meta
     * @param id o id da meta
     * @param progress o novo progresso
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO editProgress(Long id, Float progress) {
        if (repository.existsById(id)) {
            Goal goal = repository.findById(id).get();
            goal.setProgress(progress);
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta não encontrada com id: " + id);
    }

    /**
     * Incrementa o progresso de uma meta
     * @param id o id da meta
     * @param amount a quantidade a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO incrementProgress(Long id, Float amount) {
        if (repository.existsById(id)) {
            Goal goal = repository.findById(id).get();
            goal.setProgress(goal.getProgress() + amount);
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta não encontrada com id: " + id);
    }

    // ============= UPDATE/EDIT =============

    /**
     * Atualiza a dificuldade de uma meta
     * @param id o id da meta
     * @param difficult a nova dificuldade
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO editDifficult(Long id, GoalDifficult difficult) {
        if (repository.existsById(id)) {
            Goal goal = repository.findById(id).get();
            goal.setDifficult(difficult);
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta não encontrada com id: " + id);
    }

    /**
     * Atualiza a frequência de uma meta
     * @param id o id da meta
     * @param frequency a nova frequência
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO editFrequency(Long id, GoalFrequency frequency) {
        if (repository.existsById(id)) {
            Goal goal = repository.findById(id).get();
            goal.setFrequency(frequency);
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta não encontrada com id: " + id);
    }

    /**
     * Atualiza a próxima verificação de uma meta
     * @param id o id da meta
     * @param nextCheck a nova data de próxima verificação
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO editNextCheck(Long id, LocalDate nextCheck) {
        if (repository.existsById(id)) {
            Goal goal = repository.findById(id).get();
            goal.setNextCheck(nextCheck);
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta não encontrada com id: " + id);
    }

    /**
     * Atualiza o multiplicador de uma meta
     * @param id o id da meta
     * @param multiplier o novo multiplicador
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO editMultiplier(Long id, Float multiplier) {
        if (repository.existsById(id)) {
            Goal goal = repository.findById(id).get();
            goal.setMultiplier(multiplier);
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta não encontrada com id: " + id);
    }

    /**
     * Obtem o objeto de Goal pelo id fornecido
     * @param id o id a ser buscado
     * @return a meta em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Goal getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todas as metas
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as metas de forma paginada
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Atualiza o status de uma meta
     * @param id o id da meta
     * @param status o novo status
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO editStatus(Long id, GoalStatus status) {
        if (repository.existsById(id)) {
            Goal goal = repository.getReferenceById(id);
            goal.setStatus(status);
            repository.save(goal);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Status da meta atualizado com sucesso")
                    .content("Meta agora está com status: " + status.getValue())
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta não encontrada com id: " + id);
    }

    /**
     * Verifica se há alguma meta marcada para o dia de hoje com base no ID do usuário
     * @param userId o ID do usuário em questão
     * @return um {@link Boolean} indicando se há (true) ou não metas
     */
    @Transactional(readOnly = true)
    public Boolean checkGoalsForTodayByUserId ( Long userId ){
        return getGoalsForTodayByUserId(userId).isEmpty();
    }

    /**
     * Verifica se há alguma meta marcada para o dia de hoje com base no e-mail do usuário
     * @param email o e-mail do usuário em questão
     * @return um {@link Boolean} indicando se há (true) ou não metas
     */
    @Transactional(readOnly = true)
    public Boolean checkGoalsForTodayByUserEmail ( String email ){
        return getGoalsForTodayByUserEmail(email).isEmpty();
    }

    /**
     * Retorna IDs das metas marcadas para hoje com base no nextCheck
     * @param userId o ID do usuário
     * @return lista de IDs das metas marcadas para hoje
     */
    @Transactional(readOnly = true)
    public List<Long> getGoalsForTodayByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        return repository.findGoalIdsByUserAndNextCheck(userId, today);
    }

    /**
     * Retorna IDs das metas marcadas para hoje com base no nextCheck
     * @param email o email do usuário
     * @return lista de IDs das metas marcadas para hoje
     */
    @Transactional(readOnly = true)
    public List<Long> getGoalsForTodayByUserEmail(String email) {
        LocalDate today = LocalDate.now();
        return repository.findGoalIdsByUserEmailAndNextCheck(email, today);
    }

    /**
     * Obtém todas as metas ativas (sem paginação)
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getActive() {
        return repository.findByStatus(GoalStatus.ACTUAL);
    }

    /**
     * Obtém todas as metas ativas (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getActive(Pageable pageable) {
        return repository.findByStatus(GoalStatus.ACTUAL, pageable);
    }


    /**
     * Obtém todas as metas inativas (sem paginação)
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getInactive() {
        return repository.findByStatus(GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas inativas (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getInactive(Pageable pageable) {
        return repository.findByStatus(GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas "next" (sem paginação)
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getNext() {
        return repository.findByStatus(GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas "next" (com paginação)
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getNext(Pageable pageable) {
        return repository.findByStatus(GoalStatus.NEXT, pageable);
    }

    /**
     * Obtém todas as metas ativas de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getActiveByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.ACTUAL);
    }


    /**
     * Obtém todas as metas ativas de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getActiveByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.ACTUAL, pageable);
    }

    /**
     * Obtém todas as metas inativas de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getInactiveByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas inativas de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getInactiveByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas "next" de um usuário por ID (sem paginação)
     * @param userId o ID do usuário
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getNextByUserId(Long userId) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas "next" de um usuário por ID (com paginação)
     * @param userId o ID do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getNextByUserId(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndStatus(userId, GoalStatus.NEXT, pageable);
    }

    /**
     * Obtém todas as metas ativas de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getActiveByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.ACTUAL);
    }

    /**
     * Obtém todas as metas ativas de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getActiveByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.ACTUAL, pageable);
    }

    /**
     * Obtém todas as metas inativas de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getInactiveByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.INACTIVE);
    }

    /**
     * Obtém todas as metas inativas de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getInactiveByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.INACTIVE, pageable);
    }

    /**
     * Obtém todas as metas "next" de um usuário por email (sem paginação)
     * @param email o email do usuário
     * @return lista de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public List<Goal> getNextByUserEmail(String email) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.NEXT);
    }

    /**
     * Obtém todas as metas "next" de um usuário por email (com paginação)
     * @param email o email do usuário
     * @param pageable as configurações de paginação
     * @return página de metas em forma de {@link Goal}
     */
    @Transactional(readOnly = true)
    public Page<Goal> getNextByUserEmail(String email, Pageable pageable) {
        return repository.findByUser_Credential_EmailAndStatus(email, GoalStatus.NEXT, pageable);
    }

    /**
     * Deleta a meta com base em seu ID
     * @param id o id da meta a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Meta deletada")
                    .content("A meta com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Meta não encontrada com o ID " + id);
    }
}
