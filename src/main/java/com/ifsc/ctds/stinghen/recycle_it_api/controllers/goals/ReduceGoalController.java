package com.ifsc.ctds.stinghen.recycle_it_api.controllers.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals.ReduceGoalRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.ReduceGoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.ReduceGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.services.goals.ReduceGoalService;
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
 * Classe de controle para as metas de redução
 * Implementa todos os métodos públicos disponíveis na ReduceGoalService
 * @see ReduceGoalService, ReduceGoal
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/reduce-goals")
public class ReduceGoalController {

    /**
     * O serviço de metas de redução que permite criar, atualizar, buscar, listar e deletar metas de redução
     * @see ReduceGoalService
     */
    private final ReduceGoalService service;

    /**
     * Método POST para criar uma nova meta de redução
     * @param requestDTO A {@link ReduceGoalRequestDTO} contendo os dados da meta
     * @param email O email do autor da meta
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see ReduceGoalService#create(ReduceGoalRequestDTO, String), ReduceGoalRequestDTO
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Cria uma nova meta de redução", description = "Cria uma nova meta de redução e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Meta de redução criada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta de redução criada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar meta de redução")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createReduceGoal(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = ReduceGoalRequestDTO.class)),
            example = """
                    {
                        "goalDifficult": "NORMAL",
                        "goalFrequency": "WEEKLY",
                        "estimatedItems": [
                            {
                                "targetQuantity": 10,
                                "material": "PLASTIC"
                            }
                        ]
                    }
                    """) @Valid ReduceGoalRequestDTO requestDTO,
            @RequestParam @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(requestDTO, email), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar uma meta de redução existente
     * @param id O ID da meta a ser atualizada
     * @param requestDTO A {@link ReduceGoalRequestDTO} contendo os dados atualizados
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceGoalService#update(Long, ReduceGoalRequestDTO), ReduceGoalRequestDTO
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Atualiza uma meta de redução", description = "Atualiza uma meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Meta de redução atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta de redução atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar meta de redução")
    @ApiResponse(responseCode = "404", description = "Meta de redução não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateReduceGoal(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = ReduceGoalRequestDTO.class)),
            example = """
                    {
                        "goalDifficult": "DIFFICULT",
                        "goalFrequency": "MONTHLY",
                        "estimatedItems": [
                            {
                                "targetQuantity": 20,
                                "material": "PAPER"
                            }
                        ]
                    }
                    """) @Valid ReduceGoalRequestDTO requestDTO) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, requestDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os dias restantes para pular de uma meta de redução
     * @param id O ID da meta a ser atualizada
     * @param skipDaysLeft O novo número de dias restantes para pular
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceGoalService#editSkipDaysLeft(Long, int)
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Atualiza dias restantes para pular da meta de redução", description = "Atualiza os dias restantes para pular de uma meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Dias restantes atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Dias restantes atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar dias restantes")
    @ApiResponse(responseCode = "404", description = "Meta de redução não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/skip-days-left")
    public ResponseEntity<FeedbackResponseDTO> updateSkipDaysLeft(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "5") int skipDaysLeft) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editSkipDaysLeft(id, skipDaysLeft), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para decrementar os dias restantes para pular de uma meta de redução
     * @param id O ID da meta a ser atualizada
     * @param amount A quantidade a ser decrementada
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ReduceGoalService#decrementSkipDaysLeft(Long, int)
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Decrementa dias restantes para pular da meta de redução", description = "Decrementa os dias restantes para pular de uma meta de redução existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Dias restantes decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Dias restantes decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar dias restantes")
    @ApiResponse(responseCode = "404", description = "Meta de redução não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/skip-days-left/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementSkipDaysLeft(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "2") int amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementSkipDaysLeft(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar uma meta de redução pelo ID
     * @param id O ID da meta a ser buscada
     * @return Um ResponseEntity contendo a meta e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getObjectById(Long), ReduceGoal
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Busca meta de redução pelo ID", description = "Busca uma meta de redução pelo ID e retorna a meta com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Meta de redução encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "progress": 60.0,
                        "difficult": "NORMAL",
                        "frequency": "WEEKLY",
                        "nextCheck": "2026-03-01",
                        "status": "ACTUAL",
                        "skipDaysLeft": 5,
                        "items": [
                            {
                                "targetQuantity": 10,
                                "currentQuantity": 6,
                                "material": "PLASTIC"
                            }
                        ]
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Meta de redução não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<ReduceGoal> getReduceGoalById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma meta de redução como DTO pelo ID
     * @param id O ID da meta a ser buscada
     * @return Um ResponseEntity contendo a meta e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getById(Long), ReduceGoalResponseDTO
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Busca meta de redução como DTO pelo ID", description = "Busca uma meta de redução pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Meta de redução encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoalResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "progress": 60.0,
                        "difficult": "NORMAL",
                        "frequency": "WEEKLY",
                        "nextCheck": "2026-03-01",
                        "skipDaysLeft": 5,
                        "items": [
                            {
                                "targetQuantity": 10,
                                "currentQuantity": 6,
                                "material": "PLASTIC"
                            }
                        ]
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Meta de redução não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<ReduceGoalResponseDTO> getReduceGoalDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((ReduceGoalResponseDTO) service.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as metas de redução
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getAll(), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista todas as metas de redução", description = "Lista todas as metas de redução e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "progress": 60.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "ACTUAL",
                            "skipDaysLeft": 5,
                            "items": [
                                {
                                    "targetQuantity": 10,
                                    "currentQuantity": 6,
                                    "material": "PLASTIC"
                                }
                            ]
                        },
                        {
                            "id": 2,
                            "progress": 30.0,
                            "difficult": "DIFFICULT",
                            "frequency": "MONTHLY",
                            "nextCheck": "2026-03-15",
                            "status": "NEXT",
                            "skipDaysLeft": 10,
                            "items": [
                                {
                                    "targetQuantity": 20,
                                    "currentQuantity": 6,
                                    "material": "PAPER"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<ReduceGoal>> getAllReduceGoals() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as metas de redução com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getAll(Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista todas as metas de redução com paginação", description = "Lista todas as metas de redução com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<ReduceGoal>> getAllReduceGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução ativas
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getActive(), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução ativas", description = "Lista metas de redução ativas e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "progress": 60.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "ACTUAL",
                            "skipDaysLeft": 5,
                            "items": [
                                {
                                    "targetQuantity": 10,
                                    "currentQuantity": 6,
                                    "material": "PLASTIC"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução ativa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/active")
    public ResponseEntity<List<ReduceGoal>> getActiveReduceGoals() {
        try {
            return new ResponseEntity<>(service.getActive(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução ativas com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getActive(Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução ativas com paginação", description = "Lista metas de redução ativas com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução ativa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/active/paged")
    public ResponseEntity<Page<ReduceGoal>> getActiveReduceGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getActive(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução inativas
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getInactive(), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução inativas", description = "Lista metas de redução inativas e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 2,
                            "progress": 100.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-02-15",
                            "status": "INACTIVE",
                            "skipDaysLeft": 0,
                            "items": [
                                {
                                    "targetQuantity": 20,
                                    "currentQuantity": 20,
                                    "material": "PAPER"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução inativa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/inactive")
    public ResponseEntity<List<ReduceGoal>> getInactiveReduceGoals() {
        try {
            return new ResponseEntity<>(service.getInactive(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução inativas com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getInactive(Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução inativas com paginação", description = "Lista metas de redução inativas com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução inativa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/inactive/paged")
    public ResponseEntity<Page<ReduceGoal>> getInactiveReduceGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getInactive(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução "next"
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getNext(), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução next", description = "Lista metas de redução next e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 3,
                            "progress": 0.0,
                            "difficult": "DIFFICULT",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "NEXT",
                            "skipDaysLeft": 15,
                            "items": [
                                {
                                    "targetQuantity": 30,
                                    "currentQuantity": 0,
                                    "material": "GLASS"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução next encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/next")
    public ResponseEntity<List<ReduceGoal>> getNextReduceGoals() {
        try {
            return new ResponseEntity<>(service.getNext(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução "next" com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getNext(Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução next com paginação", description = "Lista metas de redução next com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução next encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/next/paged")
    public ResponseEntity<Page<ReduceGoal>> getNextReduceGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getNext(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução ativas de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getActiveByUserId(Long), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução ativas de usuário por ID", description = "Lista metas de redução ativas de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "progress": 60.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "ACTUAL",
                            "skipDaysLeft": 5,
                            "items": [
                                {
                                    "targetQuantity": 10,
                                    "currentQuantity": 6,
                                    "material": "PLASTIC"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<ReduceGoal>> getActiveReduceGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getActiveByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução ativas de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getActiveByUserId(Long, Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução ativas de usuário por ID com paginação", description = "Lista metas de redução ativas de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/active/paged")
    public ResponseEntity<Page<ReduceGoal>> getActiveReduceGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getActiveByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução inativas de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getInactiveByUserId(Long), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução inativas de usuário por ID", description = "Lista metas de redução inativas de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 2,
                            "progress": 100.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-02-15",
                            "status": "INACTIVE",
                            "skipDaysLeft": 0,
                            "items": [
                                {
                                    "targetQuantity": 20,
                                    "currentQuantity": 20,
                                    "material": "PAPER"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/inactive")
    public ResponseEntity<List<ReduceGoal>> getInactiveReduceGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução inativas de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getInactiveByUserId(Long, Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução inativas de usuário por ID com paginação", description = "Lista metas de redução inativas de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/inactive/paged")
    public ResponseEntity<Page<ReduceGoal>> getInactiveReduceGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução "next" de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getNextByUserId(Long), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução next de usuário por ID", description = "Lista metas de redução next de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 3,
                            "progress": 0.0,
                            "difficult": "DIFFICULT",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "NEXT",
                            "skipDaysLeft": 15,
                            "items": [
                                {
                                    "targetQuantity": 30,
                                    "currentQuantity": 0,
                                    "material": "GLASS"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/next")
    public ResponseEntity<List<ReduceGoal>> getNextReduceGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getNextByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução "next" de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getNextByUserId(Long, Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução next de usuário por ID com paginação", description = "Lista metas de redução next de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/next/paged")
    public ResponseEntity<Page<ReduceGoal>> getNextReduceGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getNextByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução ativas de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getActiveByUserEmail(String), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução ativas de usuário por email", description = "Lista metas de redução ativas de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "progress": 60.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "ACTUAL",
                            "skipDaysLeft": 5,
                            "items": [
                                {
                                    "targetQuantity": 10,
                                    "currentQuantity": 6,
                                    "material": "PLASTIC"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/active")
    public ResponseEntity<List<ReduceGoal>> getActiveReduceGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getActiveByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução ativas de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getActiveByUserEmail(String, Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução ativas de usuário por email com paginação", description = "Lista metas de redução ativas de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/active/paged")
    public ResponseEntity<Page<ReduceGoal>> getActiveReduceGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getActiveByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução inativas de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getInactiveByUserEmail(String), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução inativas de usuário por email", description = "Lista metas de redução inativas de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 2,
                            "progress": 100.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-02-15",
                            "status": "INACTIVE",
                            "skipDaysLeft": 0,
                            "items": [
                                {
                                    "targetQuantity": 20,
                                    "currentQuantity": 20,
                                    "material": "PAPER"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/inactive")
    public ResponseEntity<List<ReduceGoal>> getInactiveReduceGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução inativas de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getInactiveByUserEmail(String, Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução inativas de usuário por email com paginação", description = "Lista metas de redução inativas de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/inactive/paged")
    public ResponseEntity<Page<ReduceGoal>> getInactiveReduceGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução "next" de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getNextByUserEmail(String), List<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução next de usuário por email", description = "Lista metas de redução next de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso",
            content = @Content(schema = @Schema(implementation = ReduceGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 3,
                            "progress": 0.0,
                            "difficult": "DIFFICULT",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "NEXT",
                            "skipDaysLeft": 15,
                            "items": [
                                {
                                    "targetQuantity": 30,
                                    "currentQuantity": 0,
                                    "material": "GLASS"
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/next")
    public ResponseEntity<List<ReduceGoal>> getNextReduceGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getNextByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de redução "next" de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ReduceGoalService#getNextByUserEmail(String, Pageable), Page<ReduceGoal>
     */
    @Tag(name = "Metas de Redução", description = "Recurso para gerenciamento de metas de redução")
    @Operation(summary = "Lista metas de redução next de usuário por email com paginação", description = "Lista metas de redução next de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de redução listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de redução encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/next/paged")
    public ResponseEntity<Page<ReduceGoal>> getNextReduceGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getNextByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
