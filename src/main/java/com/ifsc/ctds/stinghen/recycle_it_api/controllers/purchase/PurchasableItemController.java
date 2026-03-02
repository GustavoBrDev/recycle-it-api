package com.ifsc.ctds.stinghen.recycle_it_api.controllers.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableItem;
import com.ifsc.ctds.stinghen.recycle_it_api.services.purchase.PurchasableItemService;
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
 * Classe de controle para os itens compráveis
 * Implementa todos os métodos públicos disponíveis na PurchasableItemService
 * @see PurchasableItemService, PurchasableItem
 * @version 1.0
 * @since 01/03/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/purchasables")
public class PurchasableItemController {

    /**
     * O serviço de itens compráveis que permite atualizar, buscar, listar e deletar itens compráveis
     * @see PurchasableItemService
     */
    private final PurchasableItemService service;

    /**
     * Método PUT para atualizar um item comprável existente
     * @param id O ID do item a ser atualizado
     * @param item O item comprável atualizado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasableItemService#update(Long, PurchasableItem)
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "[DEV] Atualiza um item comprável", description = "Atualiza um item comprável existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item comprável atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item comprável atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar item comprável")
    @ApiResponse(responseCode = "404", description = "Item comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasableItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = PurchasableItem.class)),
            example = """
                    {
                        "price": 100,
                        "isOnSale": true
                    }
                    """) @Valid PurchasableItem item) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, item), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o preço de um item comprável
     * @param id O ID do item a ser atualizado
     * @param price O novo preço
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasableItemService#editPrice(Long, Long)
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "[DEV] Atualiza o preço do item comprável", description = "Atualiza o preço de um item comprável existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Preço atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Preço atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar preço")
    @ApiResponse(responseCode = "404", description = "Item comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @PatchMapping("/{id}/price")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasableItemPrice(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "150") @NotNull @Positive Long price) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editPrice(id, price), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o status de promoção de um item comprável
     * @param id O ID do item a ser atualizado
     * @param isOnSale O novo status de promoção
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasableItemService#editIsOnSale(Long, Boolean)
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "[DEV] Atualiza o status de promoção do item comprável", description = "Atualiza o status de promoção de um item comprável existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Status de promoção atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Status de promoção atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar status de promoção")
    @ApiResponse(responseCode = "404", description = "Item comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @PatchMapping("/{id}/is-on-sale")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasableItemIsOnSale(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "true") Boolean isOnSale) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editIsOnSale(id, isOnSale), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um item comprável pelo ID
     * @param id O ID do item a ser buscado
     * @return Um ResponseEntity contendo o item e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableItemService#getObjectById(Long), PurchasableItem
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "Busca item comprável pelo ID", description = "Busca um item comprável pelo ID e retorna o item com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item comprável encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableItem.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "price": 100,
                        "isOnSale": true
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Item comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<PurchasableItem> getPurchasableItemById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens compráveis
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableItemService#getAll(), List<PurchasableItem>
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "Lista todos os itens compráveis", description = "Lista todos os itens compráveis e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens compráveis listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableItem.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "price": 100,
                            "isOnSale": true
                        },
                        {
                            "id": 2,
                            "price": 150,
                            "isOnSale": false
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item comprável encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<PurchasableItem>> getAllPurchasableItems() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens compráveis com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableItemService#getAll(Pageable), Page<PurchasableItem>
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "Lista todos os itens compráveis com paginação", description = "Lista todos os itens compráveis com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens compráveis listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item comprável encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<PurchasableItem>> getAllPurchasableItemsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens compráveis em promoção
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableItemService#getAllOnSale(), List<PurchasableItem>
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "Lista todos os itens compráveis em promoção", description = "Lista todos os itens compráveis em promoção e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens compráveis em promoção listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableItem.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "price": 100,
                            "isOnSale": true
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item comprável em promoção encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/on-sale")
    public ResponseEntity<List<PurchasableItem>> getAllPurchasableItemsOnSale() {
        try {
            return new ResponseEntity<>(service.getAllOnSale(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens compráveis em promoção com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableItemService#getAllOnSale(Pageable), Page<PurchasableItem>
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "Lista todos os itens compráveis em promoção com paginação", description = "Lista todos os itens compráveis em promoção com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens compráveis em promoção listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item comprável em promoção encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/on-sale/paged")
    public ResponseEntity<Page<PurchasableItem>> getAllPurchasableItemsOnSalePaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllOnSale(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um item comprável
     * @param id O ID do item a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasableItemService#deleteById(Long)
     */
    @Tag(name = "Itens Compráveis", description = "Recurso para gerenciamento de itens compráveis")
    @Operation(summary = "[DEV] Deleta um item comprável", description = "Deleta um item comprável e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item comprável deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item comprável deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar item comprável")
    @ApiResponse(responseCode = "404", description = "Item comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deletePurchasableItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
