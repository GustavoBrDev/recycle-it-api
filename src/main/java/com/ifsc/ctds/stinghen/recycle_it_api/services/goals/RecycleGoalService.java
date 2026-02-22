package com.ifsc.ctds.stinghen.recycle_it_api.services.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals.RecycleGoalRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.RecycleGoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.RecycleGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.goals.RecycleGoalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

    /**
     * Cria/persiste o registro de uma meta de reciclagem no banco de dados
     * @param requestDTO DTO de request de metas de reciclagem
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(RecycleGoalRequestDTO requestDTO) {
        RecycleGoal goal = requestDTO.convert();
        repository.save(goal);

        return FeedbackResponseDTO.builder()
                .mainMessage("Meta de reciclagem criada com sucesso")
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
}
