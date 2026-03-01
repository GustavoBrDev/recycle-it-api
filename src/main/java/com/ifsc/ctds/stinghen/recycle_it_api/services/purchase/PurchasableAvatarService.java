package com.ifsc.ctds.stinghen.recycle_it_api.services.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase.PurchasableAvatarResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableAvatar;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.purchase.PurchasableAvatarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de avatares compráveis
 * @author Gustavo Stinghen
 * @see PurchasableAvatar
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class PurchasableAvatarService {

    public PurchasableAvatarRepository repository;

    /**
     * Cria/persiste o registro de um avatar comprável no banco de dados
     * @param avatar o avatar comprável a ser criado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(PurchasableAvatar avatar) {
        repository.save(avatar);

        return FeedbackResponseDTO.builder()
                .mainMessage("Avatar comprável criado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza um avatar comprável existente
     * @param id o id do avatar comprável
     * @param avatar o avatar comprável com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o avatar comprável não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, PurchasableAvatar avatar) {
        PurchasableAvatar existingAvatar = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Avatar comprável não encontrado com id: " + id
                ));

        existingAvatar.setPrice(avatar.getPrice());
        existingAvatar.setIsOnSale(avatar.getIsOnSale());
        existingAvatar.setAvatar(avatar.getAvatar());

        repository.save(existingAvatar);

        return FeedbackResponseDTO.builder()
                .mainMessage("Avatar comprável atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o avatar de um avatar comprável
     * @param id o id do avatar comprável
     * @param avatarType o novo tipo de avatar
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o avatar comprável não for encontrado
     */
    @Transactional
    public ResponseDTO editAvatar(Long id, Avatar avatarType) {
        if (repository.existsById(id)) {
            PurchasableAvatar avatar = repository.findById(id).get();
            avatar.setAvatar(avatarType);
            repository.save(avatar);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Avatar comprável atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Avatar comprável não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de PurchasableAvatar pelo id fornecido
     * @param id o id a ser buscado
     * @return o avatar comprável em forma de {@link PurchasableAvatar}
     */
    @Transactional(readOnly = true)
    public PurchasableAvatar getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de um avatar comprável pelo id fornecido
     * @param id o id a ser buscado
     * @return o avatar comprável em forma da DTO {@link PurchasableAvatarResponseDTO}
     * @throws NotFoundException quando o avatar comprável não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new PurchasableAvatarResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Avatar comprável não encontrado com o ID " + id);
    }

    /**
     * Obtém todos os avatares compráveis
     * @return lista de avatares compráveis em forma de {@link PurchasableAvatar}
     */
    @Transactional(readOnly = true)
    public List<PurchasableAvatar> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os avatares compráveis de forma paginada
     * @param pageable as configurações de paginação
     * @return página de avatares compráveis em forma de {@link PurchasableAvatar} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PurchasableAvatar> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém avatares compráveis por tipo de avatar
     * @param avatarType o tipo de avatar
     * @return lista de avatares compráveis em forma de {@link PurchasableAvatar}
     */
    @Transactional(readOnly = true)
    public List<PurchasableAvatar> getByAvatarType(Avatar avatarType) {
        return repository.findAll().stream()
                .filter(avatar -> avatar.getAvatar().equals(avatarType))
                .toList();
    }

    /**
     * Obtém avatares compráveis que estão em promoção
     * @return lista de avatares compráveis em promoção em forma de {@link PurchasableAvatar}
     */
    @Transactional(readOnly = true)
    public List<PurchasableAvatar> getAllOnSale() {
        return repository.findByIsOnSaleTrue();
    }

    /**
     * Obtém avatares compráveis que estão em promoção de forma paginada
     * @param pageable as configurações de paginação
     * @return página de avatares compráveis em promoção em forma de {@link PurchasableAvatar} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<PurchasableAvatar> getAllOnSale(Pageable pageable) {
        return repository.findByIsOnSaleTrue(pageable);
    }

    /**
     * Deleta o avatar comprável com base em seu ID
     * @param id o id do avatar comprável a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o avatar comprável não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Avatar comprável deletado")
                    .content("O avatar comprável com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Avatar comprável não encontrado com o ID " + id);
    }
}
