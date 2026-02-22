package com.ifsc.ctds.stinghen.recycle_it_api.services.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase.PurchasableSkipCommentResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableSkipComment;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase.PurchasableSkipCommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de itens de pular comentário compráveis
 * @author Gustavo Stinghen
 * @see PurchasableSkipComment
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class PurchasableSkipCommentService {

    public PurchasableSkipCommentRepository repository;

    /**
     * Cria/persiste o registro de um item de pular comentário comprável no banco de dados
     * @param skipComment o item de pular comentário comprável a ser criado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(PurchasableSkipComment skipComment) {
        repository.save(skipComment);

        return FeedbackResponseDTO.builder()
                .mainMessage("Item de pular comentário comprável criado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza um item de pular comentário comprável existente
     * @param id o id do item de pular comentário comprável
     * @param skipComment o item de pular comentário comprável com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item de pular comentário comprável não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, PurchasableSkipComment skipComment) {
        PurchasableSkipComment existingSkipComment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item de pular comentário comprável não encontrado com id: " + id
                ));

        existingSkipComment.setPrice(skipComment.getPrice());
        existingSkipComment.setIsOnSale(skipComment.getIsOnSale());

        repository.save(existingSkipComment);

        return FeedbackResponseDTO.builder()
                .mainMessage("Item de pular comentário comprável atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Obtem o objeto de PurchasableSkipComment pelo id fornecido
     * @param id o id a ser buscado
     * @return o item de pular comentário comprável em forma de {@link PurchasableSkipComment}
     */
    @Transactional(readOnly = true)
    public PurchasableSkipComment getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de um item de pular comentário comprável pelo id fornecido
     * @param id o id a ser buscado
     * @return o item de pular comentário comprável em forma da DTO {@link PurchasableSkipCommentResponseDTO}
     * @throws NotFoundException quando o item de pular comentário comprável não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new PurchasableSkipCommentResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Item de pular comentário comprável não encontrado com o ID " + id);
    }

    /**
     * Obtém todos os itens de pular comentário compráveis
     * @return lista de itens de pular comentário compráveis em forma de {@link PurchasableSkipComment}
     */
    @Transactional(readOnly = true)
    public List<PurchasableSkipComment> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os itens de pular comentário compráveis de forma paginada
     * @param pageable as configurações de paginação
     * @return página de itens de pular comentário compráveis em forma de {@link PurchasableSkipComment} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PurchasableSkipComment> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém itens de pular comentário compráveis que estão em promoção
     * @return lista de itens de pular comentário compráveis em promoção em forma de {@link PurchasableSkipComment}
     */
    @Transactional(readOnly = true)
    public List<PurchasableSkipComment> getAllOnSale() {
        return repository.findAll().stream()
                .filter(skipComment -> skipComment.getIsOnSale())
                .toList();
    }

    /**
     * Obtém itens de pular comentário compráveis que estão em promoção de forma paginada
     * @param pageable as configurações de paginação
     * @return página de itens de pular comentário compráveis em promoção em forma de {@link PurchasableSkipComment} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PurchasableSkipComment> getAllOnSale(Pageable pageable) {
        List<PurchasableSkipComment> onSaleSkipComments = repository.findAll().stream()
                .filter(skipComment -> skipComment.getIsOnSale())
                .toList();
        
        // Convert to Page manually since we don't have a custom query
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), onSaleSkipComments.size());
        List<PurchasableSkipComment> pageContent = onSaleSkipComments.subList(start, end);
        
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, onSaleSkipComments.size());
    }

    /**
     * Deleta o item de pular comentário comprável com base em seu ID
     * @param id o id do item de pular comentário comprável a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o item de pular comentário comprável não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Item de pular comentário comprável deletado")
                    .content("O item de pular comentário comprável com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Item de pular comentário comprável não encontrado com o ID " + id);
    }
}
