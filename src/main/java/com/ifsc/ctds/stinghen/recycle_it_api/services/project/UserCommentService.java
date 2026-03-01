package com.ifsc.ctds.stinghen.recycle_it_api.services.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.UserCommentResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.UserComment;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.project.UserCommentRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para os métodos específicos de comentários de usuários em projetos
 * @author Gustavo Stinghen
 * @see UserComment
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class UserCommentService {

    public UserCommentRepository repository;
    public RegularUserService userService;
    public ProjectService projectService;


    /**
     * Cria/persiste o registro de um comentário de usuário no banco de dados
     * @param comment o comentário a ser criado
     * @param userEmail o email do usuário que está comentando
     * @param projectId o id do projeto ao qual o comentário pertence
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(String comment, String userEmail, Long projectId) {

        RegularUser user = userService.getObjectByEmail(userEmail);

        UserComment userComment = UserComment.builder()
                .user(user)
                .text(comment)
                .date(LocalDateTime.now())
                .project(projectService.getObjectById(projectId))
                .build();

        repository.save(userComment);
        projectService.finalize(user, projectId);


        return FeedbackResponseDTO.builder()
                .mainMessage("Comentário criado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza um comentário de usuário existente
     * @param id o id do comentário
     * @param comment o comentário com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o comentário não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, UserComment comment) {
        UserComment existingComment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Comentário não encontrado com id: " + id
                ));

        existingComment.setUser(comment.getUser());
        existingComment.setText(comment.getText());

        repository.save(existingComment);

        return FeedbackResponseDTO.builder()
                .mainMessage("Comentário atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o usuário de um comentário
     * @param id o id do comentário
     * @param user o novo usuário
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o comentário não for encontrado
     */
    @Transactional
    public ResponseDTO editUser(Long id, User user) {
        if (repository.existsById(id)) {
            UserComment comment = repository.findById(id).get();
            comment.setUser(user);
            repository.save(comment);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Comentário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Comentário não encontrado com id: " + id);
    }

    /**
     * Atualiza o texto de um comentário
     * @param id o id do comentário
     * @param text o novo texto
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o comentário não for encontrado
     */
    @Transactional
    public ResponseDTO editText(Long id, String text) {
        if (repository.existsById(id)) {
            UserComment comment = repository.findById(id).get();
            comment.setText(text);
            repository.save(comment);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Comentário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Comentário não encontrado com id: " + id);
    }

    /**
     * Atualiza a data de um comentário
     * @param id o id do comentário
     * @param date a nova data
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o comentário não for encontrado
     */
    @Transactional
    public ResponseDTO editDate(Long id, LocalDateTime date) {
        if (repository.existsById(id)) {
            UserComment comment = repository.findById(id).get();
            comment.setDate(date);
            repository.save(comment);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Comentário atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Comentário não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de UserComment pelo id fornecido
     * @param id o id a ser buscado
     * @return o comentário em forma de {@link UserComment}
     */
    @Transactional(readOnly = true)
    public UserComment getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de um comentário pelo id fornecido
     * @param id o id a ser buscado
     * @return o comentário em forma da DTO {@link UserCommentResponseDTO}
     * @throws NotFoundException quando o comentário não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new UserCommentResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Comentário não encontrado com o ID " + id);
    }

    /**
     * Obtém todos os comentários
     * @return lista de comentários em forma de {@link UserComment}
     */
    @Transactional(readOnly = true)
    public List<UserComment> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os comentários de forma paginada
     * @param pageable as configurações de paginação
     * @return página de comentários em forma de {@link UserComment} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<UserComment> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém comentários por usuário
     * @param userId o id do usuário
     * @return lista de comentários em forma de {@link UserComment}
     */
    @Transactional(readOnly = true)
    public List<UserComment> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * Obtém comentários por usuário ordenados por data
     * @param userId o id do usuário
     * @return lista de comentários em forma de {@link UserComment}
     */
    @Transactional(readOnly = true)
    public List<UserComment> getByUserIdOrderByDateDesc(Long userId) {
        return repository.findByUserIdOrderByDateDesc(userId);
    }

    /**
     * Obtém comentários por email do usuário
     * @param email o email do usuário
     * @return lista de comentários em forma de {@link UserComment}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<UserComment> getByUserEmail(String email) {
        if (userService.existsByEmail(email)) {
            Long userId = userService.getObjectByEmail(email).getId();
            return repository.findByUserId(userId);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Obtém comentários por email do usuário ordenados por data
     * @param email o email do usuário
     * @return lista de comentários em forma de {@link UserComment}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public List<UserComment> getByUserEmailOrderByDateDesc(String email) {
        if (userService.existsByEmail(email)) {
            Long userId = userService.getObjectByEmail(email).getId();
            return repository.findByUserIdOrderByDateDesc(userId);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Deleta o comentário com base em seu ID
     * @param id o id do comentário a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o comentário não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Comentário deletado")
                    .content("O comentário com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Comentário não encontrado com o ID " + id);
    }
}
