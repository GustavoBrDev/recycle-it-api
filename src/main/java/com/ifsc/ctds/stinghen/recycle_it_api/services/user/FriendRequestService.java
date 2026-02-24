package com.ifsc.ctds.stinghen.recycle_it_api.services.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.FriendRequestResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.DeniedRequestException;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.InvalidRelationshipException;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.FriendRequest;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
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
import java.time.ZoneId;
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
    public RegularUserService regularUserService;

    /**
     * Cria/persiste o registro de uma solicitação de amizade no banco de dados
     * @param email o e-mail do usuário que realizou a solicitação
     * @param targetId o id do usuário que será adicionado como amigo (alvo da solicitação)
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(String email, Long targetId) {

        FriendRequest friendRequest = FriendRequest.builder()
                .target( regularUserService.getObjectById(targetId) )
                .sender(regularUserService.getObjectByEmail(email) )
                .sendDate( LocalDateTime.now(ZoneId.of("America/Sao_Paulo")) )
                .build();

        repository.save(friendRequest);

        return FeedbackResponseDTO.builder()
                .mainMessage("Solicitação de amizade criada com sucesso")
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
     * Aceita a solicitação de amizade
     * @param id o id da solictação aceita
     * @param email o e-mail do usuário que realizou a aprovação
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws DeniedRequestException quando a solicitação de amizade não lhe pertence
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO accept (Long id, String email) {

        FriendRequest friendRequest = this.getObjectById(id);

        if ( ! friendRequest.getTarget().getCredential().getEmail().equals(email) ) {
            throw new DeniedRequestException("A solicitação em questão não lhe pertence");
        }

        ResponseDTO response = regularUserService.addFriend( friendRequest.getSender().getId(), friendRequest.getTarget().getId() );
        this.deleteById(id);

        return response;
    }

    /**
     * Rejeita a solicitação de amizade
     * @param id o id da solictação aceita
     * @param email o e-mail do usuário que realizou a rejeição
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws DeniedRequestException quando a solicitação de amizade não lhe pertence
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO reject (Long id, String email) {

        FriendRequest friendRequest = this.getObjectById(id);

        if ( ! friendRequest.getTarget().getCredential().getEmail().equals(email) ) {
            throw new DeniedRequestException("A solicitação em questão não lhe pertence");
        }

        deleteById(id);

        return FeedbackResponseDTO.builder()
                .mainMessage("Solicitação de amizade rejeitada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Cancela a solicitação de amizade
     * @param userId o id do usário alvo da solicitação
     * @param email o e-mail do usuário que realizou o cancelamento
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws DeniedRequestException quando a solicitação de amizade não lhe pertence
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     * @throws InvalidRelationshipException caso a relação seja inválida
     */
    @Transactional
    public ResponseDTO cancel (Long userId, String email) {

        FriendRequest friendRequest = this.getByTargetIdAndSenderEmail(userId, email);

        if ( ! friendRequest.getTarget().getCredential().getEmail().equals(email) ) {
            throw new DeniedRequestException("A solicitação em questão não lhe pertence");
        }

        deleteById(friendRequest.getId());

        return FeedbackResponseDTO.builder()
                .mainMessage("Solicitação de amizade cancelada com sucesso")
                .content("O usuário não verá mais sua solicitação")
                .isAlert(false)
                .isError(false)
                .build();
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
     * Obtém solicitações de amizade por ID do destinatário
     * @param targetId o ID do usuário destinatário
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getByTargetId(Long targetId) {
        if (regularUserService.existsById(targetId)) {
            return repository.findByTargetId(targetId);
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
        if (regularUserService.existsById(senderId)) {
            return repository.findBySenderId(senderId);
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
        if (regularUserService.existsByEmail(targetEmail)) {
            return repository.findByTarget_Credential_Email(targetEmail);
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
        if (regularUserService.existsByEmail(senderEmail)) {
            return repository.findBySender_Credential_Email(senderEmail);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + senderEmail);
    }

    /**
     * Obtém solicitação de amizade por ID do destinatário e email do remetente
     * @param targetId o ID do usuário destinatário
     * @param senderEmail o email do usuário remetente
     * @return a solicitação de amizade em forma de {@link FriendRequest}
     * @throws NotFoundException quando o usuário não for encontrado ou a solicitação não existir
     */
    @Transactional(readOnly = true)
    public FriendRequest getByTargetIdAndSenderEmail(Long targetId, String senderEmail) {
        if (regularUserService.existsById(targetId) && regularUserService.existsByEmail(senderEmail)) {
            return repository.findByTargetIdAndSender_Credential_Email(targetId, senderEmail);
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + targetId + " ou e-mail " + senderEmail);
    }

    /**
     * Obtém todas as solicitações de amizade por ID do destinatário
     * @param targetId o ID do usuário destinatário
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getAllByTargetId(Long targetId) {
        if (regularUserService.existsById(targetId)) {
            return repository.findByTargetId(targetId);
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + targetId);
    }

    /**
     * Obtém todas as solicitações de amizade por ID do destinatário com paginação
     * @param targetId o ID do usuário destinatário
     * @param pageable as configurações de paginação
     * @return página de solicitações de amizade em forma de {@link FriendRequest} utilizando paginação {@link Page}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public Page<FriendRequest> getAllByTargetId(Long targetId, Pageable pageable) {
        if (regularUserService.existsById(targetId)) {
            return repository.findByTargetId(targetId, pageable);
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + targetId);
    }

    /**
     * Obtém todas as solicitações de amizade por email do destinatário
     * @param targetEmail o email do usuário destinatário
     * @return lista de solicitações de amizade em forma de {@link FriendRequest}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequest> getAllByTargetEmail(String targetEmail) {
        if (regularUserService.existsByEmail(targetEmail)) {
            return repository.findByTarget_Credential_Email(targetEmail);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + targetEmail);
    }

    /**
     * Obtém todas as solicitações de amizade por email do destinatário com paginação
     * @param targetEmail o email do usuário destinatário
     * @param pageable as configurações de paginação
     * @return página de solicitações de amizade em forma de {@link FriendRequest} utilizando paginação {@link Page}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public Page<FriendRequest> getAllByTargetEmail(String targetEmail, Pageable pageable) {
        if (regularUserService.existsByEmail(targetEmail)) {
            return repository.findByTarget_Credential_Email(targetEmail, pageable);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + targetEmail);
    }

    /**
     * Obtém uma solicitação de amizade por ID como ResponseDTO
     * @param id o ID da solicitação de amizade
     * @return uma {@link ResponseDTO} do tipo {@link FriendRequestResponseDTO}
     * @throws EntityNotFoundException quando a solicitação de amizade não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getObjectByIdAsResponse(Long id) {
        FriendRequest friendRequest = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação de amizade não encontrada com o ID " + id));
        
        return new FriendRequestResponseDTO(friendRequest);
    }

    /**
     * Obtém todas as solicitações de amizade como ResponseDTO
     * @return uma {@link ResponseDTO} contendo lista de {@link FriendRequestResponseDTO}
     */
    @Transactional(readOnly = true)
    public ResponseDTO getAllAsResponse() {
        List<FriendRequestResponseDTO> friendRequests = repository.findAll().stream()
                .map(FriendRequestResponseDTO::new)
                .toList();
        
        return FeedbackResponseDTO.builder()
                .mainMessage("Solicitações de amizade encontradas")
                .content("Total de " + friendRequests.size() + " solicitações")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Obtém todas as solicitações de amizade paginadas como ResponseDTO
     * @param pageable as configurações de paginação
     * @return uma {@link ResponseDTO} contendo página de {@link FriendRequestResponseDTO}
     */
    @Transactional(readOnly = true)
    public ResponseDTO getAllAsResponse(Pageable pageable) {
        Page<FriendRequestResponseDTO> friendRequests = repository.findAll(pageable)
                .map(FriendRequestResponseDTO::new);
        
        return FeedbackResponseDTO.builder()
                .mainMessage("Solicitações de amizade encontradas")
                .content("Página " + (friendRequests.getNumber() + 1) + " de " + friendRequests.getTotalPages())
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Obtém solicitações de amizade por ID do destinatário como ResponseDTO
     * @param targetId o ID do usuário destinatário
     * @return uma lista de {@link FriendRequestResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDTO> getByTargetIdAsResponse(Long targetId) {
        if (regularUserService.existsById(targetId)) {
            return repository.findByTargetId(targetId).stream()
                    .map(FriendRequestResponseDTO::new)
                    .toList();
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + targetId);
    }

    /**
     * Obtém solicitações de amizade por ID do remetente como ResponseDTO
     * @param senderId o ID do usuário remetente
     * @return uma lista de {@link FriendRequestResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDTO> getBySenderIdAsResponse(Long senderId) {
        if (regularUserService.existsById(senderId)) {
            return repository.findBySenderId(senderId).stream()
                    .map(FriendRequestResponseDTO::new)
                    .toList();
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + senderId);
    }

    /**
     * Obtém solicitações de amizade por email do destinatário como ResponseDTO
     * @param targetEmail o email do usuário destinatário
     * @return uma lista de {@link FriendRequestResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDTO> getByTargetEmailAsResponse(String targetEmail) {
        if (regularUserService.existsByEmail(targetEmail)) {
            return repository.findByTarget_Credential_Email(targetEmail).stream()
                    .map(FriendRequestResponseDTO::new)
                    .toList();
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + targetEmail);
    }

    /**
     * Obtém solicitações de amizade por email do remetente como ResponseDTO
     * @param senderEmail o email do usuário remetente
     * @return uma lista de {@link FriendRequestResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDTO> getBySenderEmailAsResponse(String senderEmail) {
        if (regularUserService.existsByEmail(senderEmail)) {
            return repository.findBySender_Credential_Email(senderEmail).stream()
                    .map(FriendRequestResponseDTO::new)
                    .toList();
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + senderEmail);
    }

    /**
     * Obtém todas as solicitações de amizade por ID do destinatário como ResponseDTO
     * @param targetId o ID do usuário destinatário
     * @return uma lista de {@link FriendRequestResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDTO> getAllByTargetIdAsResponse(Long targetId) {
        if (regularUserService.existsById(targetId)) {
            return repository.findByTargetId(targetId).stream()
                    .map(FriendRequestResponseDTO::new)
                    .toList();
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + targetId);
    }

    /**
     * Obtém todas as solicitações de amizade por ID do destinatário com paginação como ResponseDTO
     * @param targetId o ID do usuário destinatário
     * @param pageable as configurações de paginação
     * @return uma página de {@link FriendRequestResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public Page<FriendRequestResponseDTO> getAllByTargetIdAsResponse(Long targetId, Pageable pageable) {
        if (regularUserService.existsById(targetId)) {
            return repository.findByTargetId(targetId, pageable)
                    .map(FriendRequestResponseDTO::new);
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + targetId);
    }

    /**
     * Obtém todas as solicitações de amizade por email do destinatário como ResponseDTO
     * @param targetEmail o email do usuário destinatário
     * @return uma {@link ResponseDTO} contendo lista de {@link FriendRequestResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<FriendRequestResponseDTO> getAllByTargetEmailAsResponse(String targetEmail) {
        if (regularUserService.existsByEmail(targetEmail)) {
            return repository.findByTarget_Credential_Email(targetEmail).stream()
                    .map(FriendRequestResponseDTO::new)
                    .toList();
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + targetEmail);
    }

    /**
     * Obtém todas as solicitações de amizade por email do destinatário com paginação como ResponseDTO
     * @param targetEmail o email do usuário destinatário
     * @param pageable as configurações de paginação
     * @return uma página de {@link FriendRequestResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public Page<FriendRequestResponseDTO> getAllByTargetEmailAsResponse(String targetEmail, Pageable pageable) {
        if (regularUserService.existsByEmail(targetEmail)) {
            return repository.findByTarget_Credential_Email(targetEmail, pageable)
                    .map(FriendRequestResponseDTO::new);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + targetEmail);
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
        if (regularUserService.existsById(senderId) && regularUserService.existsById(targetId)) {
            return repository.existsBySenderIdAndTargetId(senderId, targetId);
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
        if (regularUserService.existsByEmail(senderEmail) && regularUserService.existsByEmail(targetEmail)) {
            return repository.existsBySender_Credential_EmailAndTarget_Credential_Email(senderEmail, targetEmail);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail fornecido");
    }

    /**
     * Verifica se existe alguma solicitação de amizade com base no destinatário por ID
     * @param targetId o ID do usuário destinatário
     * @return true se existir alguma solicitação, false caso contrário
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public boolean existsByTargetId(Long targetId) {
        if (regularUserService.existsById(targetId)) {
            return repository.existsByTargetId(targetId);
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + targetId);
    }

    /**
     * Verifica se existe alguma solicitação de amizade com base no destinatário por email
     * @param targetEmail o email do usuário destinatário
     * @return true se existir alguma solicitação, false caso contrário
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public boolean existsByTargetEmail(String targetEmail) {
        if (regularUserService.existsByEmail(targetEmail)) {
            return repository.existsByTarget_Credential_Email(targetEmail);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + targetEmail);
    }

    /**
     * Verifica se existe alguma solicitação de amizade com base no remetente por ID
     * @param senderId o ID do usuário remetente
     * @return true se existir alguma solicitação, false caso contrário
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public boolean existsBySenderId(Long senderId) {
        if (regularUserService.existsById(senderId)) {
            return repository.existsBySenderId(senderId);
        }

        throw new NotFoundException("Usuário não encontrado com o ID " + senderId);
    }

    /**
     * Verifica se existe alguma solicitação de amizade com base no remetente por email
     * @param senderEmail o email do usuário remetente
     * @return true se existir alguma solicitação, false caso contrário
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public boolean existsBySenderEmail(String senderEmail) {
        if (regularUserService.existsByEmail(senderEmail)) {
            return repository.existsBySender_Credential_Email(senderEmail);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + senderEmail);
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
