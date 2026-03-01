package com.ifsc.ctds.stinghen.recycle_it_api.controllers.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.goals.RecycleGoalRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.RecycleGoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.RecycleGoal;
import com.ifsc.ctds.stinghen.recycle_it_api.services.goals.RecycleGoalService;
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
 * Classe de controle para as metas de reciclagem
 * Implementa todos os métodos públicos disponíveis na RecycleGoalService
 * @see RecycleGoalService, RecycleGoal
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/recycle-goals")
public class RecycleGoalController {

    /**
     * O serviço de metas de reciclagem que permite criar, atualizar, buscar, listar e deletar metas de reciclagem
     * @see RecycleGoalService
     */
    private final RecycleGoalService service;

    /**
     * Método POST para criar uma nova meta de reciclagem
     * @param requestDTO A {@link RecycleGoalRequestDTO} contendo os dados da meta
     * @param email O email do autor da meta
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see RecycleGoalService#create(RecycleGoalRequestDTO, String), RecycleGoalRequestDTO
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Cria uma nova meta de reciclagem", description = "Cria uma nova meta de reciclagem e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Meta de reciclagem criada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta de reciclagem criada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar meta de reciclagem")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createRecycleGoal(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = RecycleGoalRequestDTO.class)),
            example = """
                    {
                        "goalDifficult": "NORMAL",
                        "goalFrequency": "WEEKLY"
                    }
                    """) @Valid RecycleGoalRequestDTO requestDTO,
            @RequestParam @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(requestDTO, email), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar uma meta de reciclagem existente
     * @param id O ID da meta a ser atualizada
     * @param requestDTO A {@link RecycleGoalRequestDTO} contendo os dados atualizados
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RecycleGoalService#update(Long, RecycleGoalRequestDTO), RecycleGoalRequestDTO
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Atualiza uma meta de reciclagem", description = "Atualiza uma meta de reciclagem existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Meta de reciclagem atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta de reciclagem atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar meta de reciclagem")
    @ApiResponse(responseCode = "404", description = "Meta de reciclagem não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateRecycleGoal(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = RecycleGoalRequestDTO.class)),
            example = """
                    {
                        "goalDifficult": "DIFFICULT",
                        "goalFrequency": "MONTHLY"
                    }
                    """) @Valid RecycleGoalRequestDTO requestDTO) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, requestDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar uma meta de reciclagem pelo ID
     * @param id O ID da meta a ser buscada
     * @return Um ResponseEntity contendo a meta e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getObjectById(Long), RecycleGoal
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Busca meta de reciclagem pelo ID", description = "Busca uma meta de reciclagem pelo ID e retorna a meta com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Meta de reciclagem encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "progress": 75.5,
                        "difficult": "NORMAL",
                        "frequency": "WEEKLY",
                        "nextCheck": "2026-03-01",
                        "status": "ACTUAL"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Meta de reciclagem não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<RecycleGoal> getRecycleGoalById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma meta de reciclagem como DTO pelo ID
     * @param id O ID da meta a ser buscada
     * @return Um ResponseEntity contendo a meta e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getById(Long), RecycleGoalResponseDTO
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Busca meta de reciclagem como DTO pelo ID", description = "Busca uma meta de reciclagem pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Meta de reciclagem encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoalResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "progress": 75.5,
                        "difficult": "NORMAL",
                        "frequency": "WEEKLY",
                        "nextCheck": "2026-03-01"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Meta de reciclagem não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<RecycleGoalResponseDTO> getRecycleGoalDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((RecycleGoalResponseDTO) service.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as metas de reciclagem
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getAll(), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista todas as metas de reciclagem", description = "Lista todas as metas de reciclagem e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "progress": 75.5,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "ACTUAL"
                        },
                        {
                            "id": 2,
                            "progress": 50.0,
                            "difficult": "DIFFICULT",
                            "frequency": "MONTHLY",
                            "nextCheck": "2026-03-15",
                            "status": "NEXT"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<RecycleGoal>> getAllRecycleGoals() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as metas de reciclagem com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getAll(Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista todas as metas de reciclagem com paginação", description = "Lista todas as metas de reciclagem com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<RecycleGoal>> getAllRecycleGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem ativas
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getActive(), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem ativas", description = "Lista metas de reciclagem ativas e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "progress": 75.5,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "ACTUAL"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem ativa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/active")
    public ResponseEntity<List<RecycleGoal>> getActiveRecycleGoals() {
        try {
            return new ResponseEntity<>(service.getActive(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem ativas com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getActive(Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem ativas com paginação", description = "Lista metas de reciclagem ativas com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem ativa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/active/paged")
    public ResponseEntity<Page<RecycleGoal>> getActiveRecycleGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getActive(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem inativas
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getInactive(), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem inativas", description = "Lista metas de reciclagem inativas e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 2,
                            "progress": 100.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-02-15",
                            "status": "INACTIVE"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem inativa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/inactive")
    public ResponseEntity<List<RecycleGoal>> getInactiveRecycleGoals() {
        try {
            return new ResponseEntity<>(service.getInactive(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem inativas com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getInactive(Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem inativas com paginação", description = "Lista metas de reciclagem inativas com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem inativa encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/inactive/paged")
    public ResponseEntity<Page<RecycleGoal>> getInactiveRecycleGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getInactive(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem "next"
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getNext(), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem next", description = "Lista metas de reciclagem next e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 3,
                            "progress": 0.0,
                            "difficult": "DIFFICULT",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "NEXT"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem next encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/next")
    public ResponseEntity<List<RecycleGoal>> getNextRecycleGoals() {
        try {
            return new ResponseEntity<>(service.getNext(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem "next" com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getNext(Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem next com paginação", description = "Lista metas de reciclagem next com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem next encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/next/paged")
    public ResponseEntity<Page<RecycleGoal>> getNextRecycleGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getNext(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem ativas de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getActiveByUserId(Long), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem ativas de usuário por ID", description = "Lista metas de reciclagem ativas de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "progress": 75.5,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "ACTUAL"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<RecycleGoal>> getActiveRecycleGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getActiveByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem ativas de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getActiveByUserId(Long, Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem ativas de usuário por ID com paginação", description = "Lista metas de reciclagem ativas de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/active/paged")
    public ResponseEntity<Page<RecycleGoal>> getActiveRecycleGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getActiveByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem inativas de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getInactiveByUserId(Long), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem inativas de usuário por ID", description = "Lista metas de reciclagem inativas de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 2,
                            "progress": 100.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-02-15",
                            "status": "INACTIVE"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/inactive")
    public ResponseEntity<List<RecycleGoal>> getInactiveRecycleGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem inativas de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getInactiveByUserId(Long, Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem inativas de usuário por ID com paginação", description = "Lista metas de reciclagem inativas de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/inactive/paged")
    public ResponseEntity<Page<RecycleGoal>> getInactiveRecycleGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem "next" de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getNextByUserId(Long), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem next de usuário por ID", description = "Lista metas de reciclagem next de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 3,
                            "progress": 0.0,
                            "difficult": "DIFFICULT",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "NEXT"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/next")
    public ResponseEntity<List<RecycleGoal>> getNextRecycleGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getNextByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem "next" de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getNextByUserId(Long, Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem next de usuário por ID com paginação", description = "Lista metas de reciclagem next de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/next/paged")
    public ResponseEntity<Page<RecycleGoal>> getNextRecycleGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getNextByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem ativas de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getActiveByUserEmail(String), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem ativas de usuário por email", description = "Lista metas de reciclagem ativas de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "progress": 75.5,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "ACTUAL"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/active")
    public ResponseEntity<List<RecycleGoal>> getActiveRecycleGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getActiveByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem ativas de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getActiveByUserEmail(String, Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem ativas de usuário por email com paginação", description = "Lista metas de reciclagem ativas de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/active/paged")
    public ResponseEntity<Page<RecycleGoal>> getActiveRecycleGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getActiveByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem inativas de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getInactiveByUserEmail(String), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem inativas de usuário por email", description = "Lista metas de reciclagem inativas de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 2,
                            "progress": 100.0,
                            "difficult": "NORMAL",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-02-15",
                            "status": "INACTIVE"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/inactive")
    public ResponseEntity<List<RecycleGoal>> getInactiveRecycleGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem inativas de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getInactiveByUserEmail(String, Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem inativas de usuário por email com paginação", description = "Lista metas de reciclagem inativas de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/inactive/paged")
    public ResponseEntity<Page<RecycleGoal>> getInactiveRecycleGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem "next" de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getNextByUserEmail(String), List<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem next de usuário por email", description = "Lista metas de reciclagem next de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso",
            content = @Content(schema = @Schema(implementation = RecycleGoal.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 3,
                            "progress": 0.0,
                            "difficult": "DIFFICULT",
                            "frequency": "WEEKLY",
                            "nextCheck": "2026-03-01",
                            "status": "NEXT"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/next")
    public ResponseEntity<List<RecycleGoal>> getNextRecycleGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getNextByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas de reciclagem "next" de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycleGoalService#getNextByUserEmail(String, Pageable), Page<RecycleGoal>
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Lista metas de reciclagem next de usuário por email com paginação", description = "Lista metas de reciclagem next de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas de reciclagem listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta de reciclagem encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/next/paged")
    public ResponseEntity<Page<RecycleGoal>> getNextRecycleGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getNextByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar uma meta de reciclagem
     * @param id O ID da meta a ser deletada
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RecycleGoalService#deleteById(Long)
     */
    @Tag(name = "Metas de Reciclagem", description = "Recurso para gerenciamento de metas de reciclagem")
    @Operation(summary = "Deleta uma meta de reciclagem", description = "Deleta uma meta de reciclagem e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Meta de reciclagem deletada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta de reciclagem deletada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar meta de reciclagem")
    @ApiResponse(responseCode = "404", description = "Meta de reciclagem não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteRecycleGoal(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
