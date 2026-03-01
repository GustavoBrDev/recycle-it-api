package com.ifsc.ctds.stinghen.recycle_it_api.controllers.article;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.article.KnowledgeArticleRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article.ArticleResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.article.KnowledgeArticleResponse;
import com.ifsc.ctds.stinghen.recycle_it_api.models.article.KnowledgeArticle;
import com.ifsc.ctds.stinghen.recycle_it_api.services.article.KnowledgeArticleService;
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

import java.util.List;

/**
 * Classe de controle para os artigos de conhecimento
 * @see KnowledgeArticleService, KnowledgeArticle
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/knowledge-articles")
public class KnowledgeArticleController {

    /**
     * O serviço de artigos de conhecimento que permite criar, atualizar, buscar, listar e deletar artigos de conhecimento
     * @see KnowledgeArticleService
     */
    private KnowledgeArticleService service;

    /**
     * Método POST para criar um novo artigo de conhecimento
     * @param requestDTO A {@link KnowledgeArticleRequestDTO} contendo os dados do artigo
     * @param email O email do autor do artigo
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see KnowledgeArticleService#create(KnowledgeArticleRequestDTO, String), KnowledgeArticleRequestDTO
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Cria um novo artigo de conhecimento", description = "Cria um novo artigo de conhecimento e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Artigo de conhecimento criado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Artigo de conhecimento criado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar artigo de conhecimento")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createKnowledgeArticle(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = KnowledgeArticleRequestDTO.class)),
            example = """
                    {
                        "title": "Título do Artigo de Conhecimento",
                        "description": "Descrição detalhada do artigo de conhecimento",
                        "minimumTime": "PT30M",
                        "text": "Conteúdo completo do artigo de conhecimento...",
                        "references": ["ref1", "ref2", "ref3"]
                    }
                    """) @Valid KnowledgeArticleRequestDTO requestDTO,
            @RequestParam @Parameter(required = true, example = "autor@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(requestDTO, email), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar um artigo de conhecimento existente
     * @param id O ID do artigo a ser atualizado
     * @param requestDTO O artigo de conhecimento contendo os dados atualizados
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see KnowledgeArticleService#update(Long, KnowledgeArticleRequestDTO), KnowledgeArticleRequestDTO
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Atualiza um artigo de conhecimento", description = "Atualiza um artigo de conhecimento existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo de conhecimento atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Artigo de conhecimento atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar artigo de conhecimento")
    @ApiResponse(responseCode = "404", description = "Artigo de conhecimento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateKnowledgeArticle(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = KnowledgeArticleRequestDTO.class)),
            example = """
                    {
                        "title": "Novo Título do Artigo",
                        "description": "Nova descrição detalhada",
                        "minimumTime": "PT45M",
                        "text": "Novo conteúdo completo...",
                        "references": ["ref1", "ref2"]
                    }
                    """) @Valid KnowledgeArticleRequestDTO requestDTO) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, requestDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o texto de um artigo de conhecimento
     * @param id O ID do artigo a ser atualizado
     * @param text O novo texto
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see KnowledgeArticleService#editText(Long, String)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Atualiza o texto do artigo de conhecimento", description = "Atualiza o texto de um artigo de conhecimento existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Texto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Texto atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar texto")
    @ApiResponse(responseCode = "404", description = "Artigo de conhecimento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/text")
    public ResponseEntity<FeedbackResponseDTO> updateText(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Novo conteúdo completo do artigo de conhecimento...") String text) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editText(id, text), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para adicionar uma referência a um artigo de conhecimento
     * @param id O ID do artigo a ser atualizado
     * @param reference A referência a ser adicionada
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see KnowledgeArticleService#addReference(Long, String)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Adiciona referência ao artigo de conhecimento", description = "Adiciona uma referência a um artigo de conhecimento existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Referência adicionada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Referência adicionada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao adicionar referência")
    @ApiResponse(responseCode = "404", description = "Artigo de conhecimento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/references")
    public ResponseEntity<FeedbackResponseDTO> addReference(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "nova-referencia-bibliografica") String reference) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.addReference(id, reference), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método DELETE para remover uma referência de um artigo de conhecimento
     * @param id O ID do artigo a ser atualizado
     * @param reference A referência a ser removida
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see KnowledgeArticleService#removeReference(Long, String)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Remove referência do artigo de conhecimento", description = "Remove uma referência de um artigo de conhecimento existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Referência removida com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Referência removida",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao remover referência")
    @ApiResponse(responseCode = "404", description = "Artigo de conhecimento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}/references")
    public ResponseEntity<FeedbackResponseDTO> removeReference(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "referencia-a-ser-removida") String reference) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.removeReference(id, reference), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para finalizar a leitura de um artigo de conhecimento
     * @param id O ID do artigo a ser finalizado
     * @param email O email do usuário que está finalizando a leitura
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see KnowledgeArticleService#finish(Long, String)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Finaliza leitura do artigo de conhecimento", description = "Finaliza a leitura de um artigo de conhecimento e associa ao usuário com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Leitura finalizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Leitura finalizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao finalizar leitura")
    @ApiResponse(responseCode = "404", description = "Artigo de conhecimento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/finish")
    public ResponseEntity<FeedbackResponseDTO> finishReading(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.finish(id, email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um artigo de conhecimento pelo ID
     * @param id O ID do artigo a ser buscado
     * @return Um ResponseEntity contendo o artigo de conhecimento e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getById(Long), KnowledgeArticleResponse
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Busca artigo de conhecimento pelo ID", description = "Busca um artigo de conhecimento pelo ID e retorna o artigo com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo de conhecimento encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = KnowledgeArticleResponse.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "title": "Título do Artigo de Conhecimento",
                        "description": "Descrição detalhada do artigo",
                        "minimumTime": "PT30M",
                        "text": "Conteúdo completo do artigo...",
                        "references": ["ref1", "ref2", "ref3"]
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Artigo de conhecimento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeArticleResponse> getKnowledgeArticleById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((KnowledgeArticleResponse) service.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um artigo de conhecimento como ArticleResponseDTO pelo ID
     * @param id O ID do artigo a ser buscado
     * @return Um ResponseEntity contendo o artigo e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getByIdAsArticleResponse(Long), ArticleResponseDTO
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Busca artigo de conhecimento como ArticleResponseDTO pelo ID", description = "Busca um artigo de conhecimento pelo ID e retorna como ArticleResponseDTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo de conhecimento encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = ArticleResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "title": "Título do Artigo de Conhecimento",
                        "description": "Descrição detalhada do artigo",
                        "minimumTime": "PT30M"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Artigo de conhecimento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/as-article")
    public ResponseEntity<ArticleResponseDTO> getKnowledgeArticleAsArticleResponse(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((ArticleResponseDTO) service.getByIdAsArticleResponse(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os artigos de conhecimento
     * @return Um ResponseEntity contendo a lista de artigos de conhecimento e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getAll(), KnowledgeArticle
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Lista todos os artigos de conhecimento", description = "Lista todos os artigos de conhecimento e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos de conhecimento listados com sucesso",
            content = @Content(schema = @Schema(implementation = KnowledgeArticleResponse.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "title": "Título do Artigo 1",
                            "description": "Descrição detalhada do artigo 1",
                            "minimumTime": "PT30M",
                            "text": "Conteúdo completo do artigo 1...",
                            "references": ["ref1", "ref2"]
                        },
                        {
                            "id": 2,
                            "title": "Título do Artigo 2",
                            "description": "Descrição detalhada do artigo 2",
                            "minimumTime": "PT45M",
                            "text": "Conteúdo completo do artigo 2...",
                            "references": ["ref3", "ref4"]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum artigo de conhecimento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<KnowledgeArticle>> getAllKnowledgeArticles() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os artigos de conhecimento com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de artigos de conhecimento e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getAll(Pageable)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Lista todos os artigos de conhecimento com paginação", description = "Lista todos os artigos de conhecimento com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos de conhecimento listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum artigo de conhecimento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<KnowledgeArticle>> getAllKnowledgeArticlesPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os artigos de conhecimento como ArticleResponseDTO
     * @return Um ResponseEntity contendo a lista de artigos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getAllAsArticleResponse(), ArticleResponseDTO
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Lista todos os artigos de conhecimento como ArticleResponseDTO", description = "Lista todos os artigos de conhecimento como ArticleResponseDTO e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos de conhecimento listados com sucesso",
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
    @ApiResponse(responseCode = "404", description = "Nenhum artigo de conhecimento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/as-article")
    public ResponseEntity<List<ArticleResponseDTO>> getAllKnowledgeArticlesAsArticleResponse() {
        try {
            return new ResponseEntity<>(service.getAllAsArticleResponse(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os artigos de conhecimento como ArticleResponseDTO com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de artigos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getAllAsArticleResponse(Pageable)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Lista todos os artigos de conhecimento como ArticleResponseDTO com paginação", description = "Lista todos os artigos de conhecimento como ArticleResponseDTO com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos de conhecimento listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum artigo de conhecimento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/as-article/paged")
    public ResponseEntity<Page<ArticleResponseDTO>> getAllKnowledgeArticlesAsArticleResponsePaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllAsArticleResponse(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar artigos de conhecimento filtrados sem paginação
     * @param search Termo de busca
     * @return Um ResponseEntity contendo a página de artigos filtrados e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getFiltered(String)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Busca artigos de conhecimento filtrados sem paginação", description = "Busca artigos de conhecimento filtrados sem paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos de conhecimento filtrados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum artigo de conhecimento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/search")
    public ResponseEntity<Page<KnowledgeArticleResponse>> getFilteredKnowledgeArticles(
            @RequestParam @Parameter(required = true, example = "sustentabilidade") String search) {
        try {
            return new ResponseEntity<>(service.getFiltered(search), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar artigos de conhecimento filtrados com paginação
     * @param search Termo de busca
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de artigos filtrados e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getFiltered(String, Pageable)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Busca artigos de conhecimento filtrados com paginação", description = "Busca artigos de conhecimento filtrados com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos de conhecimento filtrados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum artigo de conhecimento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/search/paged")
    public ResponseEntity<Page<KnowledgeArticleResponse>> getFilteredKnowledgeArticlesPaged(
            @RequestParam @Parameter(required = true, example = "meio ambiente") String search,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getFiltered(search, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar artigos de conhecimento filtrados como ArticleResponseDTO sem paginação
     * @param search Termo de busca
     * @return Um ResponseEntity contendo a página de artigos filtrados e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getFilteredAsArticleResponse(String)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Busca artigos de conhecimento filtrados como ArticleResponseDTO sem paginação", description = "Busca artigos de conhecimento filtrados como ArticleResponseDTO sem paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos de conhecimento filtrados com sucesso",
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
    @ApiResponse(responseCode = "404", description = "Nenhum artigo de conhecimento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/search/as-article")
    public ResponseEntity<Page<ArticleResponseDTO>> getFilteredKnowledgeArticlesAsArticleResponse(
            @RequestParam @Parameter(required = true, example = "reciclagem") String search) {
        try {
            return new ResponseEntity<>(service.getFilteredAsArticleResponse(search), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar artigos de conhecimento filtrados como ArticleResponseDTO com paginação
     * @param search Termo de busca
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de artigos filtrados e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getFilteredAsArticleResponse(String, Pageable)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Busca artigos de conhecimento filtrados como ArticleResponseDTO com paginação", description = "Busca artigos de conhecimento filtrados como ArticleResponseDTO com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos de conhecimento filtrados com sucesso",
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
    @ApiResponse(responseCode = "404", description = "Nenhum artigo de conhecimento encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/search/as-article/paged")
    public ResponseEntity<Page<ArticleResponseDTO>> getFilteredKnowledgeArticlesAsArticleResponsePaged(
            @RequestParam @Parameter(required = true, example = "ecologia") String search,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getFilteredAsArticleResponse(search, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar os IDs dos artigos lidos por um usuário
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de IDs e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getReadArticleIdsByUserId(Long)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Busca IDs dos artigos lidos por usuário", description = "Busca os IDs dos artigos de conhecimento lidos por um usuário e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "IDs de artigos encontrados com sucesso",
            content = @Content(schema = @Schema(implementation = Long.class),
            examples = @ExampleObject(value = """
                    [1, 2, 3, 4, 5]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum artigo encontrado para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/read-ids")
    public ResponseEntity<List<Long>> getReadArticleIdsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getReadArticleIdsByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar os artigos lidos por um usuário como ArticleResponseDTO
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de artigos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see KnowledgeArticleService#getReadArticlesAsArticleResponse(Long)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Busca artigos lidos por usuário como ArticleResponseDTO", description = "Busca os artigos de conhecimento lidos por um usuário como ArticleResponseDTO e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigos encontrados com sucesso",
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
    @ApiResponse(responseCode = "404", description = "Nenhum artigo encontrado para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/read")
    public ResponseEntity<List<ArticleResponseDTO>> getReadArticlesAsArticleResponse(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getReadArticlesAsArticleResponse(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um artigo de conhecimento
     * @param id O ID do artigo a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see KnowledgeArticleService#deleteById(Long)
     */
    @Tag(name = "Artigos de Conhecimento", description = "Recurso para gerenciamento de artigos de conhecimento")
    @Operation(summary = "Deleta um artigo de conhecimento", description = "Deleta um artigo de conhecimento e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo de conhecimento deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Artigo de conhecimento deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar artigo de conhecimento")
    @ApiResponse(responseCode = "404", description = "Artigo de conhecimento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteKnowledgeArticle(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
