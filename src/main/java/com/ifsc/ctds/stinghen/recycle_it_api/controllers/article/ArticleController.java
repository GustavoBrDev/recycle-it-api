package com.ifsc.ctds.stinghen.recycle_it_api.controllers.article;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article.ArticleResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.article.Article;
import com.ifsc.ctds.stinghen.recycle_it_api.services.article.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

/**
 * Classe de controle para os artigos
 * @see ArticleService, Article
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    /**
     * O serviço de artigos que permite criar, atualizar, buscar, listar e deletar artigos
     * @see ArticleService
     */
    private ArticleService service;

    /**
     * Método PUT para atualizar um artigo existente
     * @param id O ID do artigo a ser atualizado
     * @param article O artigo contendo os dados atualizados
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ArticleService#update(Long, Article), Article
     */
    @Tag(name = "Artigos", description = "Recurso para gerenciamento de artigos")
    @Operation(summary = "Atualiza um artigo", description = "Atualiza um artigo existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Artigo atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar artigo")
    @ApiResponse(responseCode = "404", description = "Artigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateArticle(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = Article.class)),
            example = """
                    {
                        "title": "Título do Artigo",
                        "description": "Descrição detalhada do artigo",
                        "minimumTime": "PT30M"
                    }
                    """) @Valid Article article) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, article), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o título de um artigo
     * @param id O ID do artigo a ser atualizado
     * @param title O novo título
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ArticleService#editTitle(Long, String)
     */
    @Tag(name = "Artigos", description = "Recurso para gerenciamento de artigos")
    @Operation(summary = "Atualiza o título do artigo", description = "Atualiza o título de um artigo existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Título atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Título atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar título")
    @ApiResponse(responseCode = "404", description = "Artigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/title")
    public ResponseEntity<FeedbackResponseDTO> updateTitle(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Novo Título do Artigo") String title) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editTitle(id, title), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a descrição de um artigo
     * @param id O ID do artigo a ser atualizado
     * @param description A nova descrição
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ArticleService#editDescription(Long, String)
     */
    @Tag(name = "Artigos", description = "Recurso para gerenciamento de artigos")
    @Operation(summary = "Atualiza a descrição do artigo", description = "Atualiza a descrição de um artigo existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Descrição atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Descrição atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar descrição")
    @ApiResponse(responseCode = "404", description = "Artigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/description")
    public ResponseEntity<FeedbackResponseDTO> updateDescription(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Nova descrição detalhada do artigo") String description) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editDescription(id, description), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o tempo mínimo de leitura de um artigo
     * @param id O ID do artigo a ser atualizado
     * @param minimumTime O novo tempo mínimo
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ArticleService#editMinimumTime(Long, Duration)
     */
    @Tag(name = "Artigos", description = "Recurso para gerenciamento de artigos")
    @Operation(summary = "Atualiza o tempo mínimo de leitura do artigo", description = "Atualiza o tempo mínimo de leitura de um artigo existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Tempo mínimo atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Tempo mínimo atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar tempo mínimo")
    @ApiResponse(responseCode = "404", description = "Artigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/minimum-time")
    public ResponseEntity<FeedbackResponseDTO> updateMinimumTime(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "PT30M") Duration minimumTime) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editMinimumTime(id, minimumTime), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um artigo pelo ID
     * @param id O ID do artigo a ser buscado
     * @return Um ResponseEntity contendo o artigo e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ArticleService#getObjectById(Long), ArticleResponseDTO
     */
    @Tag(name = "Artigos", description = "Recurso para gerenciamento de artigos")
    @Operation(summary = "Busca artigo pelo ID", description = "Busca um artigo pelo ID e retorna o artigo com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = ArticleResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "title": "Título do Artigo",
                        "description": "Descrição detalhada do artigo",
                        "minimumTime": "PT30M"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Artigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> getArticleById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            Article article = service.getObjectById(id);
            return new ResponseEntity<>(new ArticleResponseDTO(article), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os artigos
     * @return Um ResponseEntity contendo a lista de artigos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ArticleService#getAll(), ArticleResponseDTO
     */
    @Tag(name = "Artigos", description = "Recurso para gerenciamento de artigos")
    @Operation(summary = "Lista todos os artigos", description = "Lista todos os artigos e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos listados com sucesso",
            content = @Content(schema = @Schema(implementation = ArticleResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "title": "Título do Artigo 1",
                            "description": "Descrição detalhada do artigo 1",
                            "minimumTime": "PT30M"
                        },
                        {
                            "id": 2,
                            "title": "Título do Artigo 2",
                            "description": "Descrição detalhada do artigo 2",
                            "minimumTime": "PT45M"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum artigo encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
        try {
            List<Article> articles = service.getAll();
            List<ArticleResponseDTO> responseDTOs = articles.stream()
                    .map(ArticleResponseDTO::new)
                    .toList();
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os artigos com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de artigos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ArticleService#getAll(Pageable)
     */
    @Tag(name = "Artigos", description = "Recurso para gerenciamento de artigos")
    @Operation(summary = "Lista todos os artigos com paginação", description = "Lista todos os artigos com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum artigo encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<ArticleResponseDTO>> getAllArticlesPaged(Pageable pageable) {
        try {
            Page<Article> articles = service.getAll(pageable);
            Page<ArticleResponseDTO> responseDTOs = articles.map(ArticleResponseDTO::new);
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um artigo
     * @param id O ID do artigo a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ArticleService#deleteById(Long)
     */
    @Tag(name = "Artigos", description = "Recurso para gerenciamento de artigos")
    @Operation(summary = "Deleta um artigo", description = "Deleta um artigo e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Artigo deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar artigo")
    @ApiResponse(responseCode = "404", description = "Artigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteArticle(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
