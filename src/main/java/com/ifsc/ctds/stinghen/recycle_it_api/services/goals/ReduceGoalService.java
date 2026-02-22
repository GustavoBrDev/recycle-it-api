package com.ifsc.ctds.stinghen.recycle_it_api.services.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals.ReduceGoalRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.ReduceGoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.goals.ReduceGoalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    /**
     * Cria/persiste o registro de uma meta de redução no banco de dados
     * @param requestDTO DTO de request de metas de redução
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(ReduceGoalRequestDTO requestDTO) {
        ReduceGoal goal = requestDTO.convert();
        repository.save(goal);

        return FeedbackResponseDTO.builder()
                .mainMessage("Meta de redução criada com sucesso")
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
