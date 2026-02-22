package com.ifsc.ctds.stinghen.recycle_it_api.services.article;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.article.KnowledgeArticleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article.KnowledgeArticleResponse;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.article.KnowledgeArticle;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.article.KnowledgeArticleRepository;
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
 * Service para os métodos específicos de artigos de conhecimento
 * @author Gustavo Stinghen
 * @see KnowledgeArticle
 * @since 22/02/2026
 */
@AllArgsConstructor
@Service
public class KnowledgeArticleService {

    public KnowledgeArticleRepository repository;
    public UserCredentialsRepository credentialsRepository;

    /**
     * Cria/persiste o registro de um artigo de conhecimento no banco de dados
     * @param requestDTO DTO de request de artigos de conhecimento
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(KnowledgeArticleRequestDTO requestDTO) {
        KnowledgeArticle article = requestDTO.convert();
        repository.save(article);

        return FeedbackResponseDTO.builder()
                .mainMessage("Artigo de conhecimento criado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }

    /**
     * Atualiza um artigo de conhecimento existente
     * @param id o id do artigo
     * @param requestDTO DTO com os dados atualizados do artigo
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO update(Long id, KnowledgeArticleRequestDTO requestDTO) {
        KnowledgeArticle existingArticle = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Artigo de conhecimento não encontrado com id: " + id
                ));

        existingArticle.setTitle(requestDTO.title);
        existingArticle.setDescription(requestDTO.description);
        existingArticle.setMinimumTime(requestDTO.minimumTime);
        existingArticle.setText(requestDTO.text);
        existingArticle.setArticle_references(requestDTO.references);

        repository.save(existingArticle);

        return FeedbackResponseDTO.builder()
                .mainMessage("Artigo de conhecimento atualizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
    }




    /**
     * Atualiza o texto de um artigo de conhecimento
     * @param id o id do artigo
     * @param text o novo texto
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO editText(Long id, String text) {
        if (repository.existsById(id)) {
            KnowledgeArticle article = repository.findById(id).get();
            article.setText(text);
            repository.save(article);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Artigo de conhecimento atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo de conhecimento não encontrado com id: " + id);
    }

    /**
     * Atualiza as referências de um artigo de conhecimento
     * @param id o id do artigo
     * @param references a nova lista de referências
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO editReferences(Long id, List<String> references) {
        if (repository.existsById(id)) {
            KnowledgeArticle article = repository.findById(id).get();
            article.setArticle_references(references);
            repository.save(article);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Artigo de conhecimento atualizado com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo de conhecimento não encontrado com id: " + id);
    }


    /**
     * Obtem o objeto de KnowledgeArticle pelo id fornecido
     * @param id o id a ser buscado
     * @return o artigo em forma de {@link KnowledgeArticle}
     */
    @Transactional(readOnly = true)
    public KnowledgeArticle getObjectById(Long id) {
        return repository.findById(id).get();
    }

    /**
     * Obtém a response de um artigo de conhecimento pelo id fornecido
     * @param id o id a ser buscado
     * @return o artigo em forma da DTO {@link KnowledgeArticleResponse}
     * @throws NotFoundException quando o artigo não for encontrado
     */
    @Transactional(readOnly = true)
    public ResponseDTO getById(Long id) {
        if (repository.existsById(id)) {
            return new KnowledgeArticleResponse(repository.findById(id).get());
        }

        throw new NotFoundException("Artigo de conhecimento não encontrado com o ID " + id);
    }

    /**
     * Obtém todos os artigos de conhecimento
     * @return lista de artigos em forma de {@link KnowledgeArticle}
     */
    @Transactional(readOnly = true)
    public List<KnowledgeArticle> getAll() {
        return repository.findAll();
    }

    /**
     * Obtém todos os artigos de conhecimento de forma paginada
     * @param pageable as configurações de paginação
     * @return página de artigos em forma de {@link KnowledgeArticle} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<KnowledgeArticle> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Obtém artigos de conhecimento por autor de forma paginada
     * @param authorId o id do autor
     * @param pageable as configurações de paginação
     * @return página de artigos em forma de {@link KnowledgeArticle} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<KnowledgeArticle> getByAuthorId(Long authorId, Pageable pageable) {
        return repository.findAll((root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("author").get("id"), authorId), pageable);
    }

    /**
     * Obtém artigos de conhecimento por email do autor de forma paginada
     * @param email o email do autor
     * @param pageable as configurações de paginação
     * @return página de artigos em forma de {@link KnowledgeArticle} utilizando paginação {@link Page}
     * @throws NotFoundException quando o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public Page<KnowledgeArticle> getByAuthorEmail(String email, Pageable pageable) {
        if (credentialsRepository.existsByEmail(email)) {
            User user = credentialsRepository.findByEmail(email).get().getUser();
            return getByAuthorId(user.getId(), pageable);
        }

        throw new NotFoundException("Usuário não encontrado com o e-mail " + email);
    }

    /**
     * Deleta o artigo de conhecimento com base em seu ID
     * @param id o id do artigo a ser deletado
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);

            return FeedbackResponseDTO.builder()
                    .mainMessage("Artigo de conhecimento deletado")
                    .content("O artigo de conhecimento com o ID " + id + " foi removido do banco de dados")
                    .isAlert(true)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo de conhecimento não encontrado com o ID " + id);
    }
}
