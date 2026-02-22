package com.ifsc.ctds.stinghen.recycle_it_api.services.punctuation;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.pontuation.PunctuationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para os métodos específicos de pontuações
 * @author Gustavo Stinghen
 * @see Punctuation
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class PunctuationService {

    public PunctuationRepository repository;

    /**
     * Atualiza uma pontuação existente
     * @param id o id da pontuação
     * @param punctuation a pontuação com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação não for encontrada
     */
    @Transactional
    public ResponseDTO update(Long id, Punctuation punctuation) {
        Punctuation existingPunctuation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pontuação não encontrada com id: " + id
                ));

        existingPunctuation.setLastUpdated(punctuation.getLastUpdated());

        repository.save(existingPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza a última atualização de uma pontuação
     * @param id o id da pontuação
     * @param lastUpdated a nova data de última atualização
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação não for encontrada
     */
    @Transactional
    public ResponseDTO editLastUpdated(Long id, LocalDateTime lastUpdated) {
        if (repository.existsById(id)) {
            Punctuation punctuation = repository.findById(id).get();
            punctuation.setLastUpdated(lastUpdated);
            repository.save(punctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação não encontrada com id: " + id);
    }

    /**
     * Obtem o objeto de Punctuation pelo id fornecido
     * @param id o id a ser buscado
     * @return a pontuação em forma de {@link Punctuation}
     */
    @Transactional(readOnly = true)
    public Punctuation getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todas as pontuações
     * @return lista de pontuações em forma de {@link Punctuation}
     */
    @Transactional(readOnly = true)
    public List<Punctuation> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as pontuações de forma paginada
     * @param pageable as configurações de paginação
     * @return página de pontuações em forma de {@link Punctuation} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<Punctuation> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém pontuações por período de última atualização
     * @param startDate data inicial
     * @param endDate data final
     * @return lista de pontuações em forma de {@link Punctuation}
     */
    @Transactional(readOnly = true)
    public List<Punctuation> getByLastUpdatedRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findAll().stream()
                .filter(punctuation -> punctuation.getLastUpdated().isAfter(startDate) && 
                                      punctuation.getLastUpdated().isBefore(endDate))
                .toList();
    }

    /**
     * Obtém pontuações por período de última atualização de forma paginada
     * @param startDate data inicial
     * @param endDate data final
     * @param pageable as configurações de paginação
     * @return página de pontuações em forma de {@link Punctuation} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<Punctuation> getByLastUpdatedRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        List<Punctuation> punctuationsInRange = repository.findAll().stream()
                .filter(punctuation -> punctuation.getLastUpdated().isAfter(startDate) && 
                                      punctuation.getLastUpdated().isBefore(endDate))
                .toList();
        
        // Convert to Page manually since we don't have a custom query
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), punctuationsInRange.size());
        List<Punctuation> pageContent = punctuationsInRange.subList(start, end);
        
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, punctuationsInRange.size());
    }

    /**
     * Deleta a pontuação com base em seu ID
     * @param id o id da pontuação a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação deletada")
                    .content("A pontuação com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação não encontrada com o ID " + id);
    }
}
