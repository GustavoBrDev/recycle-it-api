package com.ifsc.ctds.stinghen.recycle_it_api.services.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableItem;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase.PurchasableItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de itens compráveis
 * @author Gustavo Stinghen
 * @see PurchasableItem
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class PurchasableItemService {

    public PurchasableItemRepository repository;

    /**
     * Atualiza um item comprável existente
     * @param id o id do item comprável
     * @param item o item comprável com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item comprável não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, PurchasableItem item) {
        PurchasableItem existingItem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item comprável não encontrado com id: " + id
                ));

        existingItem.setPrice(item.getPrice());
        existingItem.setIsOnSale(item.getIsOnSale());

        repository.save(existingItem);

        return FeedbackResponseDTO.builder()
                .mainMessage("Item comprável atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o preço de um item comprável
     * @param id o id do item comprável
     * @param price o novo preço
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item comprável não for encontrado
     */
    @Transactional
    public ResponseDTO editPrice(Long id, Long price) {
        if (repository.existsById(id)) {
            PurchasableItem item = repository.findById(id).get();
            item.setPrice(price);
            repository.save(item);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item comprável atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item comprável não encontrado com id: " + id);
    }

    /**
     * Atualiza o status de venda de um item comprável
     * @param id o id do item comprável
     * @param isOnSale o novo status de venda
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item comprável não for encontrado
     */
    @Transactional
    public ResponseDTO editIsOnSale(Long id, Boolean isOnSale) {
        if (repository.existsById(id)) {
            PurchasableItem item = repository.findById(id).get();
            item.setIsOnSale(isOnSale);
            repository.save(item);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item comprável atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item comprável não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de PurchasableItem pelo id fornecido
     * @param id o id a ser buscado
     * @return o item comprável em forma de {@link PurchasableItem}
     */
    @Transactional(readOnly = true)
    public PurchasableItem getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todos os itens compráveis
     * @return lista de itens compráveis em forma de {@link PurchasableItem}
     */
    @Transactional(readOnly = true)
    public List<PurchasableItem> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os itens compráveis de forma paginada
     * @param pageable as configurações de paginação
     * @return página de itens compráveis em forma de {@link PurchasableItem} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PurchasableItem> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém itens compráveis que estão em promoção
     * @return lista de itens compráveis em promoção em forma de {@link PurchasableItem}
     */
    @Transactional(readOnly = true)
    public List<PurchasableItem> getAllOnSale() {
        return repository.findByIsOnSaleTrue();
    }

    /**
     * Obtém itens compráveis que estão em promoção de forma paginada
     * @param pageable as configurações de paginação
     * @return página de itens compráveis em promoção em forma de {@link PurchasableItem} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PurchasableItem> getAllOnSale(Pageable pageable) {
        return repository.findByIsOnSaleTrue(pageable);
    }

    /**
     * Deleta o item comprável com base em seu ID
     * @param id o id do item comprável a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item comprável não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item comprável deletado")
                    .content("O item comprável com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item comprável não encontrado com o ID " + id);
    }
}
