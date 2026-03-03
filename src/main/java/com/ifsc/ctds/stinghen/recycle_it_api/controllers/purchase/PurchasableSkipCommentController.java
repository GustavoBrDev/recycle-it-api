package com.ifsc.ctds.stinghen.recycle_it_api.controllers.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase.PurchasableSkipCommentResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableSkipComment;
import com.ifsc.ctds.stinghen.recycle_it_api.services.purchase.PurchasableSkipCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Classe de controle para os itens de pular comentário compráveis
 * Implementa todos os métodos públicos disponíveis na PurchasableSkipCommentService
 * @see PurchasableSkipCommentService, PurchasableSkipComment
 * @version 1.0
 * @since 01/03/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/purchasables/skip-comment")
public class PurchasableSkipCommentController {

    /**
     * O serviço de itens de pular comentário compráveis que permite criar, atualizar, buscar, listar e deletar itens de pular comentário compráveis
     * @see PurchasableSkipCommentService
     */
    private final PurchasableSkipCommentService service;

    /**
     * Método POST para criar um novo item de pular comentário comprável
     * @param skipComment O item de pular comentário comprável a ser criado
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see PurchasableSkipCommentService#create(PurchasableSkipComment)
     */
    @Tag(name = "Itens de Pular Comentário Compráveis", description = "Recurso para gerenciamento de itens de pular comentário compráveis")
    @Operation(summary = "[DEV] Cria um novo item de pular comentário comprável", description = "Cria um novo item de pular comentário comprável e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Item de pular comentário comprável criado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item de pular comentário comprável criado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar item de pular comentário comprável")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createPurchasableSkipComment(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = PurchasableSkipComment.class)),
            example = """
                    {
                        "price": 50,
                        "isOnSale": true
                    }
                    """) @Valid PurchasableSkipComment skipComment) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(skipComment), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar um item de pular comentário comprável existente
     * @param id O ID do item a ser atualizado
     * @param skipComment O item de pular comentário comprável atualizado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasableSkipCommentService#update(Long, PurchasableSkipComment)
     */
    @Tag(name = "Itens de Pular Comentário Compráveis", description = "Recurso para gerenciamento de itens de pular comentário compráveis")
    @Operation(summary = "[DEV] Atualiza um item de pular comentário comprável", description = "Atualiza um item de pular comentário comprável existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item de pular comentário comprável atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item de pular comentário comprável atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar item de pular comentário comprável")
    @ApiResponse(responseCode = "404", description = "Item de pular comentário comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasableSkipComment(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = PurchasableSkipComment.class)),
            example = """
                    {
                        "price": 75,
                        "isOnSale": false
                    }
                    """) @Valid PurchasableSkipComment skipComment) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, skipComment), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um item de pular comentário comprável pelo ID
     * @param id O ID do item a ser buscado
     * @return Um ResponseEntity contendo o item e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableSkipCommentService#getObjectById(Long), PurchasableSkipComment
     */
    @Tag(name = "Itens de Pular Comentário Compráveis", description = "Recurso para gerenciamento de itens de pular comentário compráveis")
    @Operation(summary = "Busca item de pular comentário comprável pelo ID", description = "Busca um item de pular comentário comprável pelo ID e retorna o item com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item de pular comentário comprável encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableSkipComment.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "price": 50,
                        "isOnSale": true
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Item de pular comentário comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<PurchasableSkipComment> getPurchasableSkipCommentById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um item de pular comentário comprável como DTO pelo ID
     * @param id O ID do item a ser buscado
     * @return Um ResponseEntity contendo o item e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableSkipCommentService#getById(Long), PurchasableSkipCommentResponseDTO
     */
    @Tag(name = "Itens de Pular Comentário Compráveis", description = "Recurso para gerenciamento de itens de pular comentário compráveis")
    @Operation(summary = "Busca item de pular comentário comprável como DTO pelo ID", description = "Busca um item de pular comentário comprável pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item de pular comentário comprável encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableSkipCommentResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "price": 50,
                        "isOnSale": true,
                        "type": "PURCHASABLE_SKIP_COMMENT"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Item de pular comentário comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<PurchasableSkipCommentResponseDTO> getPurchasableSkipCommentDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((PurchasableSkipCommentResponseDTO) service.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens de pular comentário compráveis
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableSkipCommentService#getAll(), List<PurchasableSkipComment>
     */
    @Tag(name = "Itens de Pular Comentário Compráveis", description = "Recurso para gerenciamento de itens de pular comentário compráveis")
    @Operation(summary = "Lista todos os itens de pular comentário compráveis", description = "Lista todos os itens de pular comentário compráveis e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens de pular comentário compráveis listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableSkipComment.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "price": 50,
                            "isOnSale": true
                        },
                        {
                            "id": 2,
                            "price": 75,
                            "isOnSale": false
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item de pular comentário comprável encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<PurchasableSkipComment>> getAllPurchasableSkipComments() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens de pular comentário compráveis com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableSkipCommentService#getAll(Pageable), Page<PurchasableSkipComment>
     */
    @Tag(name = "Itens de Pular Comentário Compráveis", description = "Recurso para gerenciamento de itens de pular comentário compráveis")
    @Operation(summary = "Lista todos os itens de pular comentário compráveis com paginação", description = "Lista todos os itens de pular comentário compráveis com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens de pular comentário compráveis listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item de pular comentário comprável encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<PurchasableSkipComment>> getAllPurchasableSkipCommentsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens de pular comentário compráveis em promoção
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableSkipCommentService#getAllOnSale(), List<PurchasableSkipComment>
     */
    @Tag(name = "Itens de Pular Comentário Compráveis", description = "Recurso para gerenciamento de itens de pular comentário compráveis")
    @Operation(summary = "Lista todos os itens de pular comentário compráveis em promoção", description = "Lista todos os itens de pular comentário compráveis em promoção e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens de pular comentário compráveis em promoção listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableSkipComment.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "price": 50,
                            "isOnSale": true
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item de pular comentário comprável em promoção encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/on-sale")
    public ResponseEntity<List<PurchasableSkipComment>> getAllPurchasableSkipCommentsOnSale() {
        try {
            return new ResponseEntity<>(service.getAllOnSale(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens de pular comentário compráveis em promoção com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableSkipCommentService#getAllOnSale(Pageable), Page<PurchasableSkipComment>
     */
    @Tag(name = "Itens de Pular Comentário Compráveis", description = "Recurso para gerenciamento de itens de pular comentário compráveis")
    @Operation(summary = "Lista todos os itens de pular comentário compráveis em promoção com paginação", description = "Lista todos os itens de pular comentário compráveis em promoção com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens de pular comentário compráveis em promoção listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item de pular comentário comprável em promoção encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/on-sale/paged")
    public ResponseEntity<Page<PurchasableSkipComment>> getAllPurchasableSkipCommentsOnSalePaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllOnSale(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
