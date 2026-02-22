package com.ifsc.ctds.stinghen.recycle_it_api.services.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.UserPunctuationResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.UserPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.league.UserPunctuationRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.security.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para os métodos específicos de pontuação de usuário na liga
 * @author Gustavo Stinghen
 * @see UserPunctuation
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class UserPunctuationService {

    public UserPunctuationRepository repository;
    public UserCredentialsRepository credentialsRepository;

    /**
     * Cria/persiste o registro de uma pontuação de usuário na liga no banco de dados
     * @param userPunctuation a pontuação de usuário na liga a ser criada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(UserPunctuation userPunctuation) {
        repository.save(userPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação de usuário na liga criada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza uma pontuação de usuário na liga existente
     * @param id o id da pontuação de usuário na liga
     * @param userPunctuation a pontuação de usuário na liga com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação de usuário na liga não for encontrada
     */
    @Transactional
    public ResponseDTO update(Long id, UserPunctuation userPunctuation) {
        UserPunctuation existingUserPunctuation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pontuação de usuário na liga não encontrada com id: " + id
                ));

        existingUserPunctuation.setUser(userPunctuation.getUser());
        existingUserPunctuation.setPunctuations(userPunctuation.getPunctuations());

        repository.save(existingUserPunctuation);

        return FeedbackResponseDTO.builder()
                .mainMessage("Pontuação de usuário na liga atualizada com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o usuário de uma pontuação de usuário na liga
     * @param id o id da pontuação de usuário na liga
     * @param user o novo usuário
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação de usuário na liga não for encontrada
     */
    @Transactional
    public ResponseDTO editUser(Long id, User user) {
        if (repository.existsById(id)) {
            UserPunctuation userPunctuation = repository.findById(id).get();
            userPunctuation.setUser(user);
            repository.save(userPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação de usuário na liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação de usuário na liga não encontrada com id: " + id);
    }

    /**
     * Atualiza as pontuações de uma pontuação de usuário na liga
     * @param id o id da pontuação de usuário na liga
     * @param punctuations a nova lista de pontuações
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação de usuário na liga não for encontrada
     */
    @Transactional
    public ResponseDTO editPunctuations(Long id, List<Punctuation> punctuations) {
        if (repository.existsById(id)) {
            UserPunctuation userPunctuation = repository.findById(id).get();
            userPunctuation.setPunctuations(punctuations);
            repository.save(userPunctuation);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação de usuário na liga atualizada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação de usuário na liga não encontrada com id: " + id);
    }

    /**
     * Obtem o objeto de UserPunctuation pelo id fornecido
     * @param id o id a ser buscado
     * @return a pontuação de usuário na liga em forma de {@link UserPunctuation}
     */
    @Transactional(readOnly = true)
    public UserPunctuation getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de uma pontuação de usuário na liga pelo id fornecido
     * @param id o id a ser buscado
     * @return a pontuação de usuário na liga em forma da DTO {@link UserPunctuationResponseDTO}
     * @throws NotFoundException quando a pontuação de usuário na liga não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new UserPunctuationResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Pontuação de usuário na liga não encontrada com o ID " + id);
    }

    /**
     * Obtém a pontuação de usuário na liga pelo id do usuário
     * @param userId o id do usuário
     * @return a pontuação de usuário na liga em forma de {@link UserPunctuation}
     */
    @Transactional(readOnly = true)
    public UserPunctuation getObjectByUserId(Long userId) {
        return repository.findByUser_Id(userId);
    }

    /**
     * Obtém a response de uma pontuação de usuário na liga pelo id do usuário
     * @param userId o id do usuário
     * @return a pontuação de usuário na liga em forma da DTO {@link UserPunctuationResponseDTO}
     * @throws NotFoundException quando a pontuação de usuário na liga não for encontrada
     */
    @Transactional(readOnly = true)
    public ResponseDTO getByUserId(Long userId) {
        UserPunctuation userPunctuation = repository.findByUser_Id(userId);
        if (userPunctuation != null) {
            return new UserPunctuationResponseDTO(userPunctuation);
        }

        throw new NotFoundException("Pontuação de usuário na liga não encontrada com o ID do usuário " + userId);
    }

    /**
     * Obtém a pontuação de usuário na liga pelo email do usuário
     * @param email o email do usuário
     * @return a pontuação de usuário na liga em forma de {@link UserPunctuation}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public UserPunctuation getObjectByUserEmail(String email) {
        if (credentialsRepository.existsByEmail(email)) {
            Long userId = credentialsRepository.findByEmail(email).get().getUser().getId();
            return repository.findByUser_Id(userId);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Obtém a response de uma pontuação de usuário na liga pelo email do usuário
     * @param email o email do usuário
     * @return a pontuação de usuário na liga em forma da DTO {@link UserPunctuationResponseDTO}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getByUserEmail(String email) {
        if (credentialsRepository.existsByEmail(email)) {
            Long userId = credentialsRepository.findByEmail(email).get().getUser().getId();
            UserPunctuation userPunctuation = repository.findByUser_Id(userId);
            if (userPunctuation != null) {
                return new UserPunctuationResponseDTO(userPunctuation);
            }
        }

        throw new NotFoundException("Pontuação de usuário na liga não encontrada com o e-mail " + email);
    }

    /**
     * Obtém todas as pontuações de usuário na liga
     * @return lista de pontuações de usuário na liga em forma de {@link UserPunctuation}
     */
    @Transactional(readOnly = true)
    public List<UserPunctuation> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todas as pontuações de usuário na liga de forma paginada
     * @param pageable as configurações de paginação
     * @return página de pontuações de usuário na liga em forma de {@link UserPunctuation} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<UserPunctuation> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Deleta a pontuação de usuário na liga com base em seu ID
     * @param id o id da pontuação de usuário na liga a ser deletada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando a pontuação de usuário na liga não for encontrada
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Pontuação de usuário na liga deletada")
                    .content("A pontuação de usuário na liga com o ID " + id + " foi removida do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Pontuação de usuário na liga não encontrada com o ID " + id);
    }
}
