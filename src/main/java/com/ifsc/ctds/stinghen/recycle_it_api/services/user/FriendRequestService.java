package com.ifsc.ctds.stinghen.recycle_it_api.services.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.FriendRequest;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.user.FriendRequestRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.security.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para os métodos específicos de solicitações de amizade
 * @author Gustavo Stinghen
 * @see FriendRequest
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class FriendRequestService {

    public FriendRequestRepository repository;
    public UserCredentialsRepository credentialsRepository;

    /**
     * Cria/persiste o registro de uma solicitação de amizade no banco de dados
     * @param friendRequest a solicitação de amizade a ser criada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(FriendRequest friendRequest) {
        repository.save(friendRequest);

        return FeedbackResponseDTO.builder()
                .mainMessage("Solicitação de amizade criada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza uma solicitação de amizade existente
     * @param id o id da solicitação de amizade
     * @param friendRequest a solicitação de amizade com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     */
    @Transactional
    public ResponseDTO update(Long id, FriendRequest friendRequest) {
        FriendRequest existingFriendRequest = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Solicitação de amizade não encontrada com id: " + id
                ));

        existingFriendRequest.setSender(friendRequest.getSender());
        existingFriendRequest.setTarget(friendRequest.getTarget());
        existingFriendRequest.setSendDate(friendRequest.getSendDate());

        repository.save(existingFriendRequest);

        return FeedbackResponseDTO.builder()
                .mainMessage("Solicitação de amizade atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o remetente de uma solicitação de amizade
     * @param id o id da solicitação de amizade
     * @param sender o novo remetente
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     */
    @Transactional
    public ResponseDTO editSender(Long id, User sender) {
        if (repository.existsById(id)) {
            FriendRequest friendRequest = repository.findById(id).get();
            friendRequest.setSender(sender);
            repository.save(friendRequest);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Solicitação de amizade atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Solicitação de amizade não encontrada com id: " + id);
    }

    /**
     * Atualiza o destinatário de uma solicitação de amizade
     * @param id o id da solicitação de amizade
     * @param target o novo destinatário
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     */
    @Transactional
    public ResponseDTO editTarget(Long id, User target) {
        if (repository.existsById(id)) {
            FriendRequest friendRequest = repository.findById(id).get();
            friendRequest.setTarget(target);
            repository.save(friendRequest);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Solicitação de amizade atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Solicitação de amizade não encontrada com id: " + id);
    }

    /**
     * Atualiza a data de envio de uma solicitação de amizade
     * @param id o id da solicitação de amizade
     * @param sendDate a nova data de envio
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     */
    @Transactional
    public ResponseDTO editSendDate(Long id, LocalDateTime sendDate) {
        if (repository.existsById(id)) {
            FriendRequest friendRequest = repository.findById(id).get();
            friendRequest.setSendDate(sendDate);
            repository.save(friendRequest);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Solicitação de amizade atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Solicitação de amizade não encontrada com id: " + id);
    }

    /**
     * Obtem o objeto de FriendRequest pelo id fornecido
     * @param id o id a ser buscado
     * @return a solicitação de amizade em forma de {@link FriendRequest}
     */
    @Transactional(readOnly = true)
    public FriendRequest getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todas as solicitações de amizade
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as solicitações de amizade de forma paginada
     * @param pageable as configurações de paginação
     * @return página de solicitações de amizade em forma de {@link FriendRequest} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<FriendRequest> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém solicitações de amizade por destinatário
     * @param target o usuário destinatário
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getByTarget(User target) {
        return repository.findByTarget(target);
    }

    /**
     * Obtém solicitações de amizade por remetente
     * @param sender o usuário remetente
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getBySender(User sender) {
        return repository.findBySender(sender);
    }

    /**
     * Obtém solicitações de amizade por ID do destinatário
     * @param targetId o ID do usuário destinatário
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getByTargetId(Long targetId) {
        if (credentialsRepository.existsById(targetId)) {
            User target = credentialsRepository.findById(targetId).get().getUser();
            return repository.findByTarget(target);
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + targetId);
    }

    /**
     * Obtém solicitações de amizade por ID do remetente
     * @param senderId o ID do usuário remetente
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getBySenderId(Long senderId) {
        if (credentialsRepository.existsById(senderId)) {
            User sender = credentialsRepository.findById(senderId).get().getUser();
            return repository.findBySender(sender);
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + senderId);
    }

    /**
     * Obtém solicitações de amizade por email do destinatário
     * @param targetEmail o email do usuário destinatário
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getByTargetEmail(String targetEmail) {
        if (credentialsRepository.existsByEmail(targetEmail)) {
            User target = credentialsRepository.findByEmail(targetEmail).get().getUser();
            return repository.findByTarget(target);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + targetEmail);
    }

    /**
     * Obtém solicitações de amizade por email do remetente
     * @param senderEmail o email do usuário remetente
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getBySenderEmail(String senderEmail) {
        if (credentialsRepository.existsByEmail(senderEmail)) {
            User sender = credentialsRepository.findByEmail(senderEmail).get().getUser();
            return repository.findBySender(sender);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + senderEmail);
    }

    /**
     * Verifica se existe uma solicitação de amizade entre remetente e destinatário
     * @param senderId o ID do usuário remetente
     * @param targetId o ID do usuário destinatário
     * @return true se existir, false caso contrário
     * @throws NotFoundException quando algum usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public boolean existsBySenderAndTarget(Long senderId, Long targetId) {
        if (credentialsRepository.existsById(senderId) && credentialsRepository.existsById(targetId)) {
            User sender = credentialsRepository.findById(senderId).get().getUser();
            User target = credentialsRepository.findById(targetId).get().getUser();
            return repository.existsBySenderAndTarget(sender, target);
        }

        throw new NotFoundException("Usuário não encontrado com o ID fornecido");
    }

    /**
     * Verifica se existe uma solicitação de amizade entre emails de remetente e destinatário
     * @param senderEmail o email do usuário remetente
     * @param targetEmail o email do usuário destinatário
     * @return true se existir, false caso contrário
     * @throws NotFoundException quando algum usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public boolean existsBySenderAndTargetEmail(String senderEmail, String targetEmail) {
        if (credentialsRepository.existsByEmail(senderEmail) && credentialsRepository.existsByEmail(targetEmail)) {
            User sender = credentialsRepository.findByEmail(senderEmail).get().getUser();
            User target = credentialsRepository.findByEmail(targetEmail).get().getUser();
            return repository.existsBySenderAndTarget(sender, target);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail fornecido");
    }

    /**
     * Deleta a solicitação de amizade com base em seu ID
     * @param id o id da solicitação de amizade a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Solicitação de amizade deletada")
                    .content("A solicitação de amizade com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Solicitação de amizade não encontrada com o ID " + id);
    }
}
