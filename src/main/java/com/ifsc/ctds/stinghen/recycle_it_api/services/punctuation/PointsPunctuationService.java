package com.ifsc.ctds.stinghen.recycle_it_api.services.punctuation;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.pontuation.PointsPunctuationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de pontuações por pontos
 * @author Gustavo Stinghen
 * @see PointsPunctuation
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class PointsPunctuationService {

    public PointsPunctuationRepository repository;

    /**
     * Cria/persiste o registro de uma pontuação por pontos no banco de dados
     * @param pointsPunctuation a pontuação por pontos a ser criada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(PointsPunctuation pointsPunctuation) {
        repository.save(pointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos criada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza uma pontuação por pontos existente
     * @param id o id da pontuação por pontos
     * @param pointsPunctuation a pontuação por pontos com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO update(Long id, PointsPunctuation pointsPunctuation) {
        PointsPunctuation existingPointsPunctuation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pontuação por pontos não encontrada com id: " + id
                ));

        existingPointsPunctuation.setLastUpdated(pointsPunctuation.getLastUpdated());
        existingPointsPunctuation.setReducePoints(pointsPunctuation.getReducePoints());
        existingPointsPunctuation.setRecyclePoints(pointsPunctuation.getRecyclePoints());
        existingPointsPunctuation.setReusePoints(pointsPunctuation.getReusePoints());
        existingPointsPunctuation.setKnowledgePoints(pointsPunctuation.getKnowledgePoints());

        repository.save(existingPointsPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação por pontos atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza os pontos de redução de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param reducePoints os novos pontos de redução
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO editReducePoints(Long id, Long reducePoints) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            pointsPunctuation.setReducePoints(reducePoints);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com id: " + id);
    }

    /**
     * Atualiza os pontos de reciclagem de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param recyclePoints os novos pontos de reciclagem
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO editRecyclePoints(Long id, Long recyclePoints) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            pointsPunctuation.setRecyclePoints(recyclePoints);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com id: " + id);
    }

    /**
     * Atualiza os pontos de reuso de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param reusePoints os novos pontos de reuso
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO editReusePoints(Long id, Long reusePoints) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            pointsPunctuation.setReusePoints(reusePoints);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com id: " + id);
    }

    /**
     * Atualiza os pontos de conhecimento de uma pontuação por pontos
     * @param id o id da pontuação por pontos
     * @param knowledgePoints os novos pontos de conhecimento
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO editKnowledgePoints(Long id, Long knowledgePoints) {
        if (repository.existsById(id)) {
            PointsPunctuation pointsPunctuation = repository.findById(id).get();
            pointsPunctuation.setKnowledgePoints(knowledgePoints);
            repository.save(pointsPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com id: " + id);
    }

    /**
     * Obtem o objeto de PointsPunctuation pelo id fornecido
     * @param id o id a ser buscado
     * @return a pontuação por pontos em forma de {@link PointsPunctuation}
     */
    @Transactional(readOnly = true)
    public PointsPunctuation getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todas as pontuações por pontos
     * @return lista de pontuações por pontos em forma de {@link PointsPunctuation}
     */
    @Transactional(readOnly = true)
    public List<PointsPunctuation> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as pontuações por pontos de forma paginada
     * @param pageable as configurações de paginação
     * @return página de pontuações por pontos em forma de {@link PointsPunctuation} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PointsPunctuation> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Deleta a pontuação por pontos com base em seu ID
     * @param id o id da pontuação por pontos a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação por pontos não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação por pontos deletada")
                    .content("A pontuação por pontos com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação por pontos não encontrada com o ID " + id);
    }
}
