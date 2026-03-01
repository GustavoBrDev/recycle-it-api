package com.ifsc.ctds.stinghen.recycle_it_api.controllers.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase.PurchasedItemResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasedItem;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableItem;
import com.ifsc.ctds.stinghen.recycle_it_api.services.purchase.PurchasedItemService;
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
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de controle para os itens comprados
 * Implementa todos os métodos públicos disponíveis na PurchasedItemService
 * @see PurchasedItemService, PurchasedItem
 * @version 1.0
 * @since 01/03/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/purchased-items")
public class PurchasedItemController {

    /**
     * O serviço de itens comprados que permite criar, atualizar, buscar, listar e deletar itens comprados
     * @see PurchasedItemService
     */
    private final PurchasedItemService service;

    /**
     * Método POST para criar um novo item comprado
     * @param purchasedItem O item comprado a ser criado
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see PurchasedItemService#create(PurchasedItem)
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Cria um novo item comprado", description = "Cria um novo item comprado e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Item comprado criado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item comprado criado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar item comprado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createPurchasedItem(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = PurchasedItem.class)),
            example = """
                    {
                        "item": {
                            "id": 1,
                            "price": 100,
                            "isOnSale": true
                        },
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "purchaseDate": "2026-03-01T10:00:00"
                    }
                    """) @Valid PurchasedItem purchasedItem) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(purchasedItem), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar um item comprado existente
     * @param id O ID do item a ser atualizado
     * @param purchasedItem O item comprado atualizado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasedItemService#update(Long, PurchasedItem)
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Atualiza um item comprado", description = "Atualiza um item comprado existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item comprado atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item comprado atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar item comprado")
    @ApiResponse(responseCode = "404", description = "Item comprado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasedItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = PurchasedItem.class)),
            example = """
                    {
                        "item": {
                            "id": 2,
                            "price": 150,
                            "isOnSale": false
                        },
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "purchaseDate": "2026-03-01T15:30:00"
                    }
                    """) @Valid PurchasedItem purchasedItem) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, purchasedItem), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o item de um item comprado
     * @param id O ID do item comprado a ser atualizado
     * @param item O novo item
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasedItemService#editItem(Long, PurchasableItem)
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Atualiza o item do item comprado", description = "Atualiza o item de um item comprado existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item do item comprado atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item do item comprado atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar item do item comprado")
    @ApiResponse(responseCode = "404", description = "Item comprado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/item")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasedItemItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = PurchasableItem.class)),
            example = """
                    {
                        "id": 2,
                        "price": 150,
                        "isOnSale": false
                    }
                    """) @Valid PurchasableItem item) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editItem(id, item), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a data de compra de um item comprado
     * @param id O ID do item comprado a ser atualizado
     * @param purchaseDate A nova data de compra
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasedItemService#editPurchaseDate(Long, LocalDateTime)
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Atualiza a data de compra do item comprado", description = "Atualiza a data de compra de um item comprado existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Data de compra atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Data de compra atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar data de compra")
    @ApiResponse(responseCode = "404", description = "Item comprado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/purchase-date")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasedItemPurchaseDate(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "2026-03-01T15:30:00") LocalDateTime purchaseDate) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editPurchaseDate(id, purchaseDate), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um item comprado pelo ID
     * @param id O ID do item a ser buscado
     * @return Um ResponseEntity contendo o item e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasedItemService#getObjectById(Long), PurchasedItem
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Busca item comprado pelo ID", description = "Busca um item comprado pelo ID e retorna o item com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item comprado encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasedItem.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "item": {
                            "id": 1,
                            "price": 100,
                            "isOnSale": true
                        },
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "purchaseDate": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Item comprado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<PurchasedItem> getPurchasedItemById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um item comprado como DTO pelo ID
     * @param id O ID do item a ser buscado
     * @return Um ResponseEntity contendo o item e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasedItemService#getById(Long), PurchasedItemResponseDTO
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Busca item comprado como DTO pelo ID", description = "Busca um item comprado pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item comprado encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasedItemResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "item": {
                            "id": 1,
                            "price": 100,
                            "isOnSale": true,
                            "type": "RECYCLED_MATERIAL"
                        },
                        "purchaseDate": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Item comprado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<PurchasedItemResponseDTO> getPurchasedItemDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((PurchasedItemResponseDTO) service.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens comprados
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasedItemService#getAll(), List<PurchasedItem>
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Lista todos os itens comprados", description = "Lista todos os itens comprados e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens comprados listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasedItem.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "item": {
                                "id": 1,
                                "price": 100,
                                "isOnSale": true
                            },
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "purchaseDate": "2026-03-01T10:00:00"
                        },
                        {
                            "id": 2,
                            "item": {
                                "id": 2,
                                "price": 150,
                                "isOnSale": false
                            },
                            "user": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "purchaseDate": "2026-03-01T15:30:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item comprado encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<PurchasedItem>> getAllPurchasedItems() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens comprados com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasedItemService#getAll(Pageable), Page<PurchasedItem>
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Lista todos os itens comprados com paginação", description = "Lista todos os itens comprados com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens comprados listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item comprado encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<PurchasedItem>> getAllPurchasedItemsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar itens comprados por intervalo de data
     * @param startDate A data inicial do intervalo
     * @param endDate A data final do intervalo
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasedItemService#getByDateRange(LocalDateTime, LocalDateTime), List<PurchasedItem>
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Lista itens comprados por intervalo de data", description = "Lista itens comprados por intervalo de data e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens comprados listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasedItem.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "item": {
                                "id": 1,
                                "price": 100,
                                "isOnSale": true
                            },
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "purchaseDate": "2026-03-01T10:00:00"
                        },
                        {
                            "id": 2,
                            "item": {
                                "id": 2,
                                "price": 150,
                                "isOnSale": false
                            },
                            "user": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "purchaseDate": "2026-03-01T15:30:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item comprado encontrado para o intervalo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/date-range")
    public ResponseEntity<List<PurchasedItem>> getPurchasedItemsByDateRange(
            @RequestParam @Parameter(required = true, example = "2026-03-01T00:00:00") LocalDateTime startDate,
            @RequestParam @Parameter(required = true, example = "2026-03-01T23:59:59") LocalDateTime endDate) {
        try {
            return new ResponseEntity<>(service.getByDateRange(startDate, endDate), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar itens comprados por intervalo de data com paginação
     * @param startDate A data inicial do intervalo
     * @param endDate A data final do intervalo
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasedItemService#getByDateRange(LocalDateTime, LocalDateTime, Pageable), Page<PurchasedItem>
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Lista itens comprados por intervalo de data com paginação", description = "Lista itens comprados por intervalo de data com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens comprados listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item comprado encontrado para o intervalo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/date-range/paged")
    public ResponseEntity<Page<PurchasedItem>> getPurchasedItemsByDateRangePaged(
            @RequestParam @Parameter(required = true, example = "2026-03-01T00:00:00") LocalDateTime startDate,
            @RequestParam @Parameter(required = true, example = "2026-03-01T23:59:59") LocalDateTime endDate,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByDateRange(startDate, endDate, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um item comprado
     * @param id O ID do item a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasedItemService#deleteById(Long)
     */
    @Tag(name = "Itens Comprados", description = "Recurso para gerenciamento de itens comprados")
    @Operation(summary = "Deleta um item comprado", description = "Deleta um item comprado e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item comprado deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item comprado deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar item comprado")
    @ApiResponse(responseCode = "404", description = "Item comprado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deletePurchasedItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
