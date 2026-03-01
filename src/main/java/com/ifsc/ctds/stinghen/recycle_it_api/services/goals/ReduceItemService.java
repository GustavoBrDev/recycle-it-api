package com.ifsc.ctds.stinghen.recycle_it_api.services.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.goals.ReduceItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de itens para metas de redução
 * @author Gustavo Stinghen
 * @see ReduceItem
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class ReduceItemService {

    public ReduceItemRepository repository;

    /**
     * Cria/persiste o registro de um item para meta de redução no banco de dados
     * @param reduceItem o item para meta de redução a ser criado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(ReduceItem reduceItem) {
        repository.save(reduceItem);

        return FeedbackResponseDTO.builder()
                .mainMessage("Item para meta de redução criado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza um item para meta de redução existente
     * @param id o id do item para meta de redução
     * @param reduceItem o item para meta de redução com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item para meta de redução não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, ReduceItem reduceItem) {
        ReduceItem existingReduceItem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item para meta de redução não encontrado com id: " + id
                ));

        existingReduceItem.setType(reduceItem.getType());
        existingReduceItem.setTargetQuantity(reduceItem.getTargetQuantity());
        existingReduceItem.setActualQuantity(reduceItem.getActualQuantity());

        repository.save(existingReduceItem);

        return FeedbackResponseDTO.builder()
                .mainMessage("Item para meta de redução atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o tipo de material de um item para meta de redução
     * @param id o id do item para meta de redução
     * @param type o novo tipo de material
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item para meta de redução não for encontrado
     */
    @Transactional
    public ResponseDTO editType(Long id, Materials type) {
        if (repository.existsById(id)) {
            ReduceItem reduceItem = repository.findById(id).get();
            reduceItem.setType(type);
            repository.save(reduceItem);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item para meta de redução atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item para meta de redução não encontrado com id: " + id);
    }

    /**
     * Atualiza a quantidade alvo de um item para meta de redução
     * @param id o id do item para meta de redução
     * @param targetQuantity a nova quantidade alvo
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item para meta de redução não for encontrado
     */
    @Transactional
    public ResponseDTO editTargetQuantity(Long id, int targetQuantity) {
        if (repository.existsById(id)) {
            ReduceItem reduceItem = repository.findById(id).get();
            reduceItem.setTargetQuantity(targetQuantity);
            repository.save(reduceItem);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item para meta de redução atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item para meta de redução não encontrado com id: " + id);
    }

    /**
     * Atualiza a quantidade atual de um item para meta de redução
     * @param id o id do item para meta de redução
     * @param actualQuantity a nova quantidade atual
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item para meta de redução não for encontrado
     */
    @Transactional
    public ResponseDTO editActualQuantity(Long id, int actualQuantity) {
        if (repository.existsById(id)) {
            ReduceItem reduceItem = repository.findById(id).get();
            reduceItem.setActualQuantity(actualQuantity);
            repository.save(reduceItem);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item para meta de redução atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item para meta de redução não encontrado com id: " + id);
    }

    /**
     * Incrementa a quantidade atual de um item para meta de redução
     * @param id o id do item para meta de redução
     * @param amount a quantidade a ser incrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item para meta de redução não for encontrado
     */
    @Transactional
    public ResponseDTO increment( Long id, Long amount) {
        if (repository.existsById(id)) {
            ReduceItem reduceItem = repository.findById(id).get();
            reduceItem.increment(amount);
            repository.save(reduceItem);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item para meta de redução atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item para meta de redução não encontrado com id: " + id);
    }

    /**
     * Decrementa a quantidade atual de um item para meta de redução
     * @param id o id do item para meta de redução
     * @param amount a quantidade a ser decrementada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item para meta de redução não for encontrado
     */
    @Transactional
    public ResponseDTO decrement(Long id, Long amount) {
        if (repository.existsById(id)) {
            ReduceItem reduceItem = repository.findById(id).get();
            reduceItem.decrement(amount);
            repository.save(reduceItem);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item para meta de redução atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item para meta de redução não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de ReduceItem pelo id fornecido
     * @param id o id a ser buscado
     * @return o item para meta de redução em forma de {@link ReduceItem}
     */
    @Transactional(readOnly = true)
    public ReduceItem getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todos os itens para metas de redução
     * @return lista de itens para metas de redução em forma de {@link ReduceItem}
     */
    @Transactional(readOnly = true)
    public List<ReduceItem> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os itens para metas de redução de forma paginada
     * @param pageable as configurações de paginação
     * @return página de itens para metas de redução em forma de {@link ReduceItem} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ReduceItem> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém itens para metas de redução por tipo de material
     * @param type o tipo de material
     * @return lista de itens para metas de redução em forma de {@link ReduceItem}
     */
    @Transactional(readOnly = true)
    public List<ReduceItem> getByType(Materials type) {
        return repository.findByType(type);
    }

    /**
     * Obtém itens para metas de redução por tipo de material de forma paginada
     * @param type o tipo de material
     * @param pageable as configurações de paginação
     * @return página de itens para metas de redução em forma de {@link ReduceItem} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ReduceItem> getByType(Materials type, Pageable pageable) {
        return repository.findByType(type, pageable);
    }

    /**
     * Obtém itens para metas de redução concluídos (quantidade atual >= quantidade alvo)
     * @return lista de itens para metas de redução concluídos em forma de {@link ReduceItem}
     */
    @Transactional(readOnly = true)
    public List<ReduceItem> getCompleted() {
        return repository.findCompleted();
    }

    /**
     * Obtém itens para metas de redução concluídos de forma paginada
     * @param pageable as configurações de paginação
     * @return página de itens para metas de redução concluídos em forma de {@link ReduceItem} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ReduceItem> getCompleted(Pageable pageable) {
        return repository.findCompleted(pageable);
    }

    /**
     * Deleta o item para meta de redução com base em seu ID
     * @param id o id do item para meta de redução a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item para meta de redução não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item para meta de redução deletado")
                    .content("O item para meta de redução com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item para meta de redução não encontrado com o ID " + id);
    }
}
