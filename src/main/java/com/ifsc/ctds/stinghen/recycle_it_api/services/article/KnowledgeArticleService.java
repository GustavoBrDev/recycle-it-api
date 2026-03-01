package com.ifsc.ctds.stinghen.recycle_it_api.services.article;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.article.KnowledgeArticleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article.ArticleResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article.KnowledgeArticleResponse;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import com.ifsc.ctds.stinghen.recycle_it_api.models.article.Article;
import com.ifsc.ctds.stinghen.recycle_it_api.models.article.KnowledgeArticle;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.repository.article.KnowledgeArticleRepository;
import com.ifsc.ctds.stinghen.recycle_it_api.services.punctuation.PointsPunctuationService;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
import com.ifsc.ctds.stinghen.recycle_it_api.specifications.KnowledgeArticleSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
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
    public RegularUserService userService;
    public PointsPunctuationService punctuationService;

    /**
     * Cria/persiste o registro de um artigo de conhecimento no banco de dados
     * @param requestDTO DTO de request de artigos de conhecimento
     * @param email o email do usuário que está criando o artigo
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     */
    @Transactional
    public ResponseDTO create(KnowledgeArticleRequestDTO requestDTO, String email) {
        KnowledgeArticle article = requestDTO.convert();
        article.setAuthor(userService.getObjectByEmail(email));
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
     * Adiciona uma referência a um artigo de conhecimento
     * @param id o id do artigo
     * @param reference a referência a ser adicionada
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO addReference(Long id, String reference) {
        if (repository.existsById(id)) {
            KnowledgeArticle article = repository.findById(id).get();
            
            if (article.getArticle_references() == null) {
                article.setArticle_references(new ArrayList<>());
            }
            
            if (!article.getArticle_references().contains(reference)) {
                article.getArticle_references().add(reference);
                repository.save(article);
            }

            return FeedbackResponseDTO.builder()
                    .mainMessage("Referência adicionada com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo de conhecimento não encontrado com id: " + id);
    }

    /**
     * Remove uma referência de um artigo de conhecimento
     * @param id o id do artigo
     * @param reference a referência a ser removida
     * @return uma {@link ResponseDTO} do tipo {@link FeedbackResponseDTO} informando o status da operação
     * @throws EntityNotFoundException quando o artigo não for encontrado
     */
    @Transactional
    public ResponseDTO removeReference(Long id, String reference) {
        if (repository.existsById(id)) {
            KnowledgeArticle article = repository.findById(id).get();
            
            if (article.getArticle_references() != null && article.getArticle_references().contains(reference)) {
                article.getArticle_references().remove(reference);
                repository.save(article);
            }

            return FeedbackResponseDTO.builder()
                    .mainMessage("Referência removida com sucesso")
                    .isAlert(false)
                    .isError(false)
                    .build();
        }

        throw new EntityNotFoundException("Artigo de conhecimento não encontrado com id: " + id);
    }

    @Transactional
    public ResponseDTO finish ( Long id, String email ){

        RegularUser user = userService.getObjectByEmail(email);
        userService.addArticle(user.getId(), id );
        punctuationService.incrementKnowledgePointsByUserEmail(email, 75L);

        return FeedbackResponseDTO.builder()
                .mainMessage("Artigo finalizado com sucesso")
                .isAlert(false)
                .isError(false)
                .build();
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
     * Obtém artigos filtrados através de uma pesquisa inteligente
     * @param search campo de pesquisa
     * @return página de DTOs de artigos filtrados
     */
    @Transactional(readOnly = true)
    public Page<KnowledgeArticleResponse> getFiltered(String search) {
        return repository.findAll(KnowledgeArticleSpecification.getFiltered(search), Pageable.unpaged()).map(KnowledgeArticleResponse::new);
    }

    /**
     * Obtém artigos filtrados através de uma pesquisa inteligente
     * @param search campo de pesquisa
     * @param pageable configurações de paginação
     * @return página de DTOs de artigos filtrados
     */
    @Transactional(readOnly = true)
    public Page<KnowledgeArticleResponse> getFiltered(String search, Pageable pageable) {
        return repository.findAll(
                KnowledgeArticleSpecification.getFiltered(search), pageable).map(KnowledgeArticleResponse::new);
    }

    /**
     * Obtém todos os IDs dos artigos lidos por um usuário
     * @param userId o ID do usuário
     * @return lista de IDs dos artigos lidos pelo usuário
     */
    @Transactional(readOnly = true)
    public List<Long> getReadArticleIdsByUserId(Long userId) {
        return userService.getObjectById(userId).getArticles().stream()
                .map(Article::getId)
                .toList();
    }

    /**
     * Obtém um artigo de conhecimento como ArticleResponseDTO pelo ID
     * @param id o ID do artigo
     * @return o artigo em forma de {@link ArticleResponseDTO}
     * @throws NotFoundException quando o artigo não for encontrado
     */
    @Transactional(readOnly = true)
    public ArticleResponseDTO getByIdAsArticleResponse(Long id) {
        if (repository.existsById(id)) {
            return new ArticleResponseDTO(repository.findById(id).get());
        }

        throw new NotFoundException("Artigo de conhecimento não encontrado com o ID " + id);
    }

    /**
     * Obtém todos os artigos de conhecimento como lista de ArticleResponseDTO
     * @return lista de artigos em forma de {@link ArticleResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getAllAsArticleResponse() {
        return repository.findAll().stream()
                .map(ArticleResponseDTO::new)
                .toList();
    }

    /**
     * Obtém todos os artigos de conhecimento como página de ArticleResponseDTO
     * @param pageable as configurações de paginação
     * @return página de artigos em forma de {@link ArticleResponseDTO} utilizando paginação {@link Page}
     */
    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getAllAsArticleResponse(Pageable pageable) {
        return repository.findAll(pageable).map(ArticleResponseDTO::new);
    }

    /**
     * Obtém artigos filtrados como página de ArticleResponseDTO
     * @param search campo de pesquisa
     * @return página de ArticleResponseDTO de artigos filtrados
     */
    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getFilteredAsArticleResponse(String search) {
        return repository.findAll(KnowledgeArticleSpecification.getFiltered(search), Pageable.unpaged()).map(ArticleResponseDTO::new);
    }

    /**
     * Obtém artigos filtrados como página de ArticleResponseDTO com paginação
     * @param search campo de pesquisa
     * @param pageable configurações de paginação
     * @return página de ArticleResponseDTO de artigos filtrados
     */
    @Transactional(readOnly = true)
    public Page<ArticleResponseDTO> getFilteredAsArticleResponse(String search, Pageable pageable) {
        return repository.findAll(
                KnowledgeArticleSpecification.getFiltered(search), pageable).map(ArticleResponseDTO::new);
    }

    /**
     * Obtém os artigos lidos por um usuário como lista de ArticleResponseDTO
     * @param userId o ID do usuário
     * @return lista de ArticleResponseDTO dos artigos lidos pelo usuário
     */
    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getReadArticlesAsArticleResponse(Long userId) {
        return userService.getObjectById(userId).getArticles().stream()
                .map(ArticleResponseDTO::new)
                .toList();
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
