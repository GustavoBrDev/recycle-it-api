package com.ifsc.ctds.stinghen.recycle_it_api.services.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.GoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
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
     * Atualiza uma meta existente
     * @param id o id da meta
     * @param goal a meta com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a meta não for encontrada
     */
    @Transactional
    public ResponseDTO update(Long id, Goal goal) {
        Goal existingGoal = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Meta não encontrada com id: " + id
                ));

        existingGoal.setProgress(goal.getProgress());
        existingGoal.setDifficult(goal.getDifficult());
        existingGoal.setFrequency(goal.getFrequency());
        existingGoal.setNextCheck(goal.getNextCheck());
        existingGoal.setMultiplier(goal.getMultiplier());

        repository.save(existingGoal);

        return FeedbackResponseDTO.builder()
                .mainMessage("Meta atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

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
