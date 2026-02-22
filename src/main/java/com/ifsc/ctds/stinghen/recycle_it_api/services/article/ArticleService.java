package com.ifsc.ctds.stinghen.recycle_it_api.services.article;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article.ArticleResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.article.Article;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.article.ArticleRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.security.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * Service para os métodos específicos de artigos
 * @author Gustavo Stinghen
 * @see Article
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class ArticleService {

    public ArticleRepository repository;
    public UserCredentialsRepository credentialsRepository;


    /**
     * Atualiza um artigo existente
     * @param id o id do artigo
     * @param article o artigo com os dados atualizados
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, Article article) {
        Article existingArticle = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Artigo não encontrado com id: " + id
                ));

        existingArticle.setTitle(article.getTitle());
        existingArticle.setDescription(article.getDescription());
        existingArticle.setMinimumTime(article.getMinimumTime());
        existingArticle.setAuthor(article.getAuthor());

        repository.save(existingArticle);

        return FeedbackResponseDTO.builder()
                .mainMessage("Artigo atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza o título de um artigo
     * @param id o id do artigo
     * @param title o novo título
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO editTitle(Long id, String title) {
        if (repository.existsById(id)) {
            Article article = repository.findById(id).get();
            article.setTitle(title);
            repository.save(article);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Artigo atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo não encontrado com id: " + id);
    }

    /**
     * Atualiza a descrição de um artigo
     * @param id o id do artigo
     * @param description a nova descrição
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO editDescription(Long id, String description) {
        if (repository.existsById(id)) {
            Article article = repository.findById(id).get();
            article.setDescription(description);
            repository.save(article);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Artigo atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo não encontrado com id: " + id);
    }

    /**
     * Atualiza o tempo mínimo de leitura de um artigo
     * @param id o id do artigo
     * @param minimumTime o novo tempo mínimo
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO editMinimumTime(Long id, Duration minimumTime) {
        if (repository.existsById(id)) {
            Article article = repository.findById(id).get();
            article.setMinimumTime(minimumTime);
            repository.save(article);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Artigo atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo não encontrado com id: " + id);
    }

    /**
     * Atualiza o autor de um artigo
     * @param id o id do artigo
     * @param author o novo autor
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO editAuthor(Long id, User author) {
        if (repository.existsById(id)) {
            Article article = repository.findById(id).get();
            article.setAuthor(author);
            repository.save(article);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Artigo atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo não encontrado com id: " + id);
    }

    /**
     * Obtem o objeto de Article pelo id fornecido
     * @param id o id a ser buscado
     * @return o artigo em forma de {@link Article}
     */
    @Transactional(readOnly = true)
    public Article getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém todos os artigos
     * @return lista de artigos em forma de {@link Article}
     */
    @Transactional(readOnly = true)
    public List<Article> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os artigos de forma paginada
     * @param pageable as configurações de paginação
     * @return página de artigos em forma de {@link Article} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<Article> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém artigos por autor de forma paginada
     * @param authorId o id do autor
     * @param pageable as configurações de paginação
     * @return página de artigos em forma de {@link Article} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<Article> getByAuthorId(Long authorId, Pageable pageable) {
        return repository.findByAuthor_Id(authorId, pageable);
    }

    /**
     * Obtém artigos por email do autor de forma paginada
     * @param email o email do autor
     * @param pageable as configurações de paginação
     * @return página de artigos em forma de {@link Article} utilizando paginação {@link Page}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public Page<Article> getByAuthorEmail(String email, Pageable pageable) {
        if (credentialsRepository.existsByEmail(email)) {
            User user = credentialsRepository.findByEmail(email).get().getUser();
            return repository.findByAuthor_Id(user.getId(), pageable);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Deleta o artigo com base em seu ID
     * @param id o id do artigo a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Artigo deletado")
                    .content("O artigo com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo não encontrado com o ID " + id);
    }
}
