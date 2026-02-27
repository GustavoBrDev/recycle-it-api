package com.ifsc.ctds.stinghen.recycle_it_api.services.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase.PurchasedItemResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasedItem;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableItem;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase.PurchasedItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para os métodos específicos de itens comprados
 * @author Gustavo Stinghen
 * @see PurchasedItem
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class PurchasedItemService {

    public PurchasedItemRepository repository;

    /**
     * Cria/persiste o registro de um item comprado no banco de dados
     * @param purchasedItem o item comprado a ser criado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(PurchasedItem purchasedItem) {
        repository.save(purchasedItem);

        return FeedbackResponseDTO.builder()
                .mainMessage("Item comprado criado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza um item comprado existente
     * @param id o id do item comprado
     * @param purchasedItem o item comprado com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item comprado não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, PurchasedItem purchasedItem) {
        PurchasedItem existingPurchasedItem = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item comprado não encontrado com id: " + id
                ));

        existingPurchasedItem.setItem(purchasedItem.getItem());
        existingPurchasedItem.setPurchaseDate(purchasedItem.getPurchaseDate());

        repository.save(existingPurchasedItem);

        return FeedbackResponseDTO.builder()
                .mainMessage("Item comprado atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o item de um item comprado
     * @param id o id do item comprado
     * @param item o novo item
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item comprado não for encontrado
     */
    @Transactional
    public ResponseDTO editItem(Long id, PurchasableItem item) {
        if (repository.existsById(id)) {
            PurchasedItem purchasedItem = repository.findById(id).get();
            purchasedItem.setItem(item);
            repository.save(purchasedItem);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item comprado atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item comprado não encontrado com id: " + id);
    }

    /**
     * Atualiza a data de compra de um item comprado
     * @param id o id do item comprado
     * @param purchaseDate a nova data de compra
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item comprado não for encontrado
     */
    @Transactional
    public ResponseDTO editPurchaseDate(Long id, LocalDateTime purchaseDate) {
        if (repository.existsById(id)) {
            PurchasedItem purchasedItem = repository.findById(id).get();
            purchasedItem.setPurchaseDate(purchaseDate);
            repository.save(purchasedItem);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item comprado atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item comprado não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de PurchasedItem pelo id fornecido
     * @param id o id a ser buscado
     * @return o item comprado em forma de {@link PurchasedItem}
     */
    @Transactional(readOnly = true)
    public PurchasedItem getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de um item comprado pelo id fornecido
     * @param id o id a ser buscado
     * @return o item comprado em forma da DTO {@link PurchasedItemResponseDTO}
     * @throws NotFoundException quando o item comprado não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new PurchasedItemResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Item comprado não encontrado com o ID " + id);
    }

    /**
     * Obtém todos os itens comprados
     * @return lista de itens comprados em forma de {@link PurchasedItem}
     */
    @Transactional(readOnly = true)
    public List<PurchasedItem> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os itens comprados de forma paginada
     * @param pageable as configurações de paginação
     * @return página de itens comprados em forma de {@link PurchasedItem} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PurchasedItem> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém itens comprados por período de data
     * @param startDate data inicial
     * @param endDate data final
     * @return lista de itens comprados em forma de {@link PurchasedItem}
     */
    @Transactional(readOnly = true)
    public List<PurchasedItem> getByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByPurchaseDateBetween(startDate, endDate);
    }

    /**
     * Obtém itens comprados por período de data de forma paginada
     * @param startDate data inicial
     * @param endDate data final
     * @param pageable as configurações de paginação
     * @return página de itens comprados em forma de {@link PurchasedItem} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PurchasedItem> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return repository.findByPurchaseDateBetween(startDate, endDate, pageable);
    }

    /**
     * Deleta o item comprado com base em seu ID
     * @param id o id do item comprado a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item comprado não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item comprado deletado")
                    .content("O item comprado com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item comprado não encontrado com o ID " + id);
    }
}
