package com.ifsc.ctds.stinghen.recycle_it_api.controllers.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.ReduceItemResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceItem;
import com.ifsc.ctds.stinghen.recycle_it_api.services.goals.ReduceItemService;
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
import java.util.List;

/**
 * Classe de controle para os itens para metas de redução
 * Implementa todos os métodos públicos disponíveis na ReduceItemService
 * @see ReduceItemService, ReduceItem
 * @version 1.0
 * @since 01/03/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/goals/reduce/item")
public class ReduceItemController {

    /**
     * O serviço de itens para metas de redução que permite criar, atualizar, buscar, listar e deletar itens para metas de redução
     * @see ReduceItemService
     */
    private final ReduceItemService service;

    /**
     * Método POST para criar um novo item para meta de redução
     * @param reduceItem O item para meta de redução a ser criado
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see ReduceItemService#create(ReduceItem)
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Cria um novo item para meta de redução", description = "Cria um novo item para meta de redução e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Item para meta de redução criado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
               
                        "mainMessage": "Item para meta de redução criado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar item para meta de redução")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createReduceItem(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = ReduceItem.class)),
            example = """
                    {
                        "type": "PLASTIC",
                        "targetQuantity": 100,
                        "actualQuantity": 0
                    }
                    """) @Valid ReduceItem reduceItem) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(reduceItem), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar um item para meta de redução existente
     * @param id O ID do item a ser atualizado
     * @param reduceItem O item para meta de redução atualizado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceItemService#update(Long, ReduceItem)
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Atualiza um item para meta de redução", description = "Atualiza um item para meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item para meta de redução atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item para meta de redução atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar item para meta de redução")
    @ApiResponse(responseCode = "404", description = "Item para meta de redução não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateReduceItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = ReduceItem.class)),
            example = """
                    {
                        "type": "GLASS",
                        "targetQuantity": 150,
                        "actualQuantity": 75
                    }
                    """) @Valid ReduceItem reduceItem) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, reduceItem), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o tipo de um item para meta de redução
     * @param id O ID do item a ser atualizado
     * @param type O novo tipo de material
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceItemService#editType(Long, Materials)
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Atualiza o tipo do item para meta de redução", description = "Atualiza o tipo de um item para meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Tipo do item para meta de redução atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Tipo do item para meta de redução atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar tipo do item para meta de redução")
    @ApiResponse(responseCode = "404", description = "Item para meta de redução não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/type")
    public ResponseEntity<FeedbackResponseDTO> updateReduceItemType(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "METAL") Materials type) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editType(id, type), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a quantidade alvo de um item para meta de redução
     * @param id O ID do item a ser atualizado
     * @param targetQuantity A nova quantidade alvo
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceItemService#editTargetQuantity(Long, int)
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Atualiza a quantidade alvo do item para meta de redução", description = "Atualiza a quantidade alvo de um item para meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Quantidade alvo atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Quantidade alvo atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar quantidade alvo")
    @ApiResponse(responseCode = "404", description = "Item para meta de redução não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/target-quantity")
    public ResponseEntity<FeedbackResponseDTO> updateReduceItemTargetQuantity(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "200") int targetQuantity) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editTargetQuantity(id, targetQuantity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a quantidade atual de um item para meta de redução
     * @param id O ID do item a ser atualizado
     * @param actualQuantity A nova quantidade atual
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceItemService#editActualQuantity(Long, int)
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Atualiza a quantidade atual do item para meta de redução", description = "Atualiza a quantidade atual de um item para meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Quantidade atual atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Quantidade atual atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar quantidade atual")
    @ApiResponse(responseCode = "404", description = "Item para meta de redução não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/actual-quantity")
    public ResponseEntity<FeedbackResponseDTO> updateReduceItemActualQuantity(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "150") int actualQuantity) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editActualQuantity(id, actualQuantity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar a quantidade de um item para meta de redução
     * @param id O ID do item a ser atualizado
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceItemService#increment(Long, Long)
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Incrementa a quantidade do item para meta de redução", description = "Incrementa a quantidade de um item para meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Quantidade incrementada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Quantidade incrementada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar quantidade")
    @ApiResponse(responseCode = "404", description = "Item para meta de redução não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementReduceItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.increment(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar a quantidade de um item para meta de redução
     * @param id O ID do item a ser atualizado
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceItemService#decrement(Long, Long)
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Decrementa a quantidade do item para meta de redução", description = "Decrementa a quantidade de um item para meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Quantidade decrementada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Quantidade decrementada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar quantidade")
    @ApiResponse(responseCode = "404", description = "Item para meta de redução não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementReduceItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrement(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um item para meta de redução pelo ID
     * @param id O ID do item a ser buscado
     * @return Um ResponseEntity contendo o item e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceItemService#getObjectById(Long), ReduceItem
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Busca item para meta de redução pelo ID", description = "Busca um item para meta de redução pelo ID e retorna o item com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item para meta de redução encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceItem.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "type": "PLASTIC",
                        "targetQuantity": 100,
                        "actualQuantity": 75
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Item para meta de redução não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<ReduceItem> getReduceItemById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens para metas de redução
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceItemService#getAll(), List<ReduceItem>
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Lista todos os itens para metas de redução", description = "Lista todos os itens para metas de redução e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens para metas de redução listados com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceItem.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "type": "PLASTIC",
                            "targetQuantity": 100,
                            "actualQuantity": 75
                        },
                        {
                            "id": 2,
                            "type": "GLASS",
                            "targetQuantity": 150,
                            "actualQuantity": 100
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item para meta de redução encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<ReduceItem>> getAllReduceItems() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os itens para metas de redução com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceItemService#getAll(Pageable), Page<ReduceItem>
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Lista todos os itens para metas de redução com paginação", description = "Lista todos os itens para metas de redução com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens para metas de redução listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item para meta de redução encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<ReduceItem>> getAllReduceItemsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar itens para metas de redução por tipo
     * @param type O tipo de material
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceItemService#getByType(Materials), List<ReduceItem>
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Lista itens para metas de redução por tipo", description = "Lista itens para metas de redução por tipo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens para metas de redução listados com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceItem.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "type": "PLASTIC",
                            "targetQuantity": 100,
                            "actualQuantity": 75
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item para meta de redução encontrado para o tipo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ReduceItem>> getReduceItemsByType(
            @PathVariable @Parameter(required = true, example = "PLASTIC") Materials type) {
        try {
            return new ResponseEntity<>(service.getByType(type), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar itens para metas de redução por tipo com paginação
     * @param type O tipo de material
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceItemService#getByType(Materials, Pageable), Page<ReduceItem>
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Lista itens para metas de redução por tipo com paginação", description = "Lista itens para metas de redução por tipo com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens para metas de redução listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item para meta de redução encontrado para o tipo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/type/{type}/paged")
    public ResponseEntity<Page<ReduceItem>> getReduceItemsByTypePaged(
            @PathVariable @Parameter(required = true, example = "PLASTIC") Materials type,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByType(type, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar itens para metas de redução concluídos
     * @return Um ResponseEntity contendo a lista de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceItemService#getCompleted(), List<ReduceItem>
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Lista itens para metas de redução concluídos", description = "Lista itens para metas de redução concluídos e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens para metas de redução concluídos listados com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceItem.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "type": "PLASTIC",
                            "targetQuantity": 100,
                            "actualQuantity": 100
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum item para meta de redução concluído encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/completed")
    public ResponseEntity<List<ReduceItem>> getCompletedReduceItems() {
        try {
            return new ResponseEntity<>(service.getCompleted(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar itens para metas de redução concluídos com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de itens e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceItemService#getCompleted(Pageable), Page<ReduceItem>
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Lista itens para metas de redução concluídos com paginação", description = "Lista itens para metas de redução concluídos com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Itens para metas de redução concluídos listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum item para meta de redução concluído encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/completed/paged")
    public ResponseEntity<Page<ReduceItem>> getCompletedReduceItemsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getCompleted(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um item para meta de redução
     * @param id O ID do item a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceItemService#deleteById(Long)
     */
    @Tag(name = "Itens para Metas de Redução", description = "Recurso para gerenciamento de itens para metas de redução")
    @Operation(summary = "Deleta um item para meta de redução", description = "Deleta um item para meta de redução e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Item para meta de redução deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Item para meta de redução deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar item para meta de redução")
    @ApiResponse(responseCode = "404", description = "Item para meta de redução não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteReduceItem(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
