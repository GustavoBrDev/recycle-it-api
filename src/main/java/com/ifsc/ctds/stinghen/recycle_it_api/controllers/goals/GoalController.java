package com.ifsc.ctds.stinghen.recycle_it_api.controllers.goals;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.goals.GoalResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalDifficult;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalFrequency;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.GoalStatus;
import com.ifsc.ctds.stinghen.recycle_it_api.models.goals.Goal;
import com.ifsc.ctds.stinghen.recycle_it_api.services.goals.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe de controle para as metas
 * Implementa todos os métodos públicos disponíveis na GoalService
 * @see GoalService, Goal
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/goals")
public class GoalController {

    /**
     * O serviço de metas que permite criar, atualizar, buscar, listar e deletar metas
     * @see GoalService
     */
    private final GoalService service;

    /**
     * Método PATCH para atualizar o progresso de uma meta
     * @param id O ID da meta a ser atualizada
     * @param progress O novo progresso
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see GoalService#editProgress(Long, Float)
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Atualiza o progresso da meta", description = "Atualiza o progresso de uma meta existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Progresso atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta atualizada com sucesso",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar progresso")
    @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/progress")
    public ResponseEntity<FeedbackResponseDTO> updateProgress(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "75.5") Float progress) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editProgress(id, progress), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para incrementar o progresso de uma meta
     * @param id O ID da meta a ser atualizada
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see GoalService#incrementProgress(Long, Float)
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Incrementa o progresso da meta", description = "Incrementa o progresso de uma meta existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Progresso incrementado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta atualizada com sucesso",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar progresso")
    @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/progress/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementProgress(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "10.0") Float amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementProgress(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a dificuldade de uma meta
     * @param id O ID da meta a ser atualizada
     * @param difficult A nova dificuldade
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see GoalService#editDifficult(Long, GoalDifficult)
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Atualiza a dificuldade da meta", description = "Atualiza a dificuldade de uma meta existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Dificuldade atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta atualizada com sucesso",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar dificuldade")
    @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/difficulty")
    public ResponseEntity<FeedbackResponseDTO> updateDifficulty(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "NORMAL") GoalDifficult difficult) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editDifficult(id, difficult), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a frequência de uma meta
     * @param id O ID da meta a ser atualizada
     * @param frequency A nova frequência
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see GoalService#editFrequency(Long, GoalFrequency)
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Atualiza a frequência da meta", description = "Atualiza a frequência de uma meta existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Frequência atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta atualizada com sucesso",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar frequência")
    @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/frequency")
    public ResponseEntity<FeedbackResponseDTO> updateFrequency(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "WEEKLY") GoalFrequency frequency) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editFrequency(id, frequency), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a próxima verificação de uma meta
     * @param id O ID da meta a ser atualizada
     * @param nextCheck A nova data de verificação
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see GoalService#editNextCheck(Long, LocalDate)
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Atualiza a próxima verificação da meta", description = "Atualiza a próxima verificação de uma meta existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Próxima verificação atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta atualizada com sucesso",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar próxima verificação")
    @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/next-check")
    public ResponseEntity<FeedbackResponseDTO> updateNextCheck(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "2026-03-01") LocalDate nextCheck) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editNextCheck(id, nextCheck), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o multiplicador de uma meta
     * @param id O ID da meta a ser atualizada
     * @param multiplier O novo multiplicador
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see GoalService#editMultiplier(Long, Float)
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Atualiza o multiplicador da meta", description = "Atualiza o multiplicador de uma meta existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Multiplicador atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta atualizada com sucesso",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar multiplicador")
    @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/multiplier")
    public ResponseEntity<FeedbackResponseDTO> updateMultiplier(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "1.5") Float multiplier) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editMultiplier(id, multiplier), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar uma meta pelo ID
     * @param id O ID da meta a ser buscada
     * @return Um ResponseEntity contendo a meta e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getObjectById(Long), Goal
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Busca meta pelo ID", description = "Busca uma meta pelo ID e retorna a meta com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Meta encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoalById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as metas
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getAll(), List<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista todas as metas", description = "Lista todas as metas e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<Goal>> getAllGoals() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as metas com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getAll(Pageable), Page<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista todas as metas com paginação", description = "Lista todas as metas com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<Goal>> getAllGoalsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método PATCH para atualizar o status de uma meta
     * @param id O ID da meta a ser atualizada
     * @param status O novo status
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see GoalService#editStatus(Long, GoalStatus)
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Atualiza o status da meta", description = "Atualiza o status de uma meta existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Meta atualizada com sucesso",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar status")
    @ApiResponse(responseCode = "404", description = "Meta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/status")
    public ResponseEntity<FeedbackResponseDTO> updateStatus(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "COMPLETED") GoalStatus status) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editStatus(id, status), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para verificar se há metas para hoje por ID de usuário
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo true/false e o status HTTP 200 (OK).
     * @see GoalService#checkGoalsForTodayByUserId(Long), Boolean
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Verifica metas para hoje por ID de usuário", description = "Verifica se há metas marcadas para hoje para um usuário e retorna status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso",
            content = @Content(schema = @Schema(implementation = Boolean.class),
            examples = @ExampleObject(value = "true")))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/today/check")
    public ResponseEntity<Boolean> checkGoalsForTodayByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.checkGoalsForTodayByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método GET para verificar se há metas para hoje por email de usuário
     * @param email O email do usuário
     * @return Um ResponseEntity contendo true/false e o status HTTP 200 (OK).
     * @see GoalService#checkGoalsForTodayByUserEmail(String), Boolean
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Verifica metas para hoje por email de usuário", description = "Verifica se há metas marcadas para hoje para um usuário por email e retorna status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso",
            content = @Content(schema = @Schema(implementation = Boolean.class),
            examples = @ExampleObject(value = "false")))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/today/check")
    public ResponseEntity<Boolean> checkGoalsForTodayByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.checkGoalsForTodayByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método GET para listar IDs das metas para hoje por ID de usuário
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de IDs e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getGoalsForTodayByUserId(Long), List<Long>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista IDs das metas para hoje por ID de usuário", description = "Lista os IDs das metas marcadas para hoje para um usuário e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "IDs listados com sucesso",
            content = @Content(schema = @Schema(implementation = Long.class),
            examples = @ExampleObject(value = "[1, 2, 3]")))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para hoje")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/today")
    public ResponseEntity<List<Long>> getGoalsForTodayByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getGoalsForTodayByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar IDs das metas para hoje por email de usuário
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de IDs e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getGoalsForTodayByUserEmail(String), List<Long>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista IDs das metas para hoje por email de usuário", description = "Lista os IDs das metas marcadas para hoje para um usuário por email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "IDs listados com sucesso",
            content = @Content(schema = @Schema(implementation = Long.class),
            examples = @ExampleObject(value = "[4, 5, 6]")))
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para hoje")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/today")
    public ResponseEntity<List<Long>> getGoalsForTodayByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getGoalsForTodayByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas por status
     * @param status O status das metas a serem listadas
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getActive(), List<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas por status", description = "Lista metas filtrando por status e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Goal>> getGoalsByStatus(
            @PathVariable @Parameter(required = true, example = "ACTUAL") @NotNull GoalStatus status) {
        try {
            List<Goal> goals;
            switch (status) {
                case ACTUAL -> goals = service.getActive();
                case INACTIVE -> goals = service.getInactive();
                case NEXT -> goals = service.getNext();
                default -> goals = List.of();
            }
            return new ResponseEntity<>(goals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas por status com paginação
     * @param status O status das metas a serem listadas
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getActive(Pageable), Page<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas por status com paginação", description = "Lista metas filtrando por status com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/status/{status}/paged")
    public ResponseEntity<Page<Goal>> getGoalsByStatusPaged(
            @PathVariable @Parameter(required = true, example = "ACTUAL") @NotNull GoalStatus status,
            Pageable pageable) {
        try {
            Page<Goal> goals;
            switch (status) {
                case ACTUAL -> goals = service.getActive(pageable);
                case INACTIVE -> goals = service.getInactive(pageable);
                case NEXT -> goals = service.getNext(pageable);
                default -> goals = Page.empty();
            }
            return new ResponseEntity<>(goals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas ativas de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getActiveByUserId(Long), List<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas ativas de usuário por ID", description = "Lista metas ativas de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<Goal>> getActiveGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getActiveByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas ativas de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getActiveByUserId(Long, Pageable), Page<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas ativas de usuário por ID com paginação", description = "Lista metas ativas de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/active/paged")
    public ResponseEntity<Page<Goal>> getActiveGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getActiveByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas inativas de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getInactiveByUserId(Long), List<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas inativas de usuário por ID", description = "Lista metas inativas de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/inactive")
    public ResponseEntity<List<Goal>> getInactiveGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas inativas de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getInactiveByUserId(Long, Pageable), Page<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas inativas de usuário por ID com paginação", description = "Lista metas inativas de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/inactive/paged")
    public ResponseEntity<Page<Goal>> getInactiveGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas "next" de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getNextByUserId(Long), List<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas next de usuário por ID", description = "Lista metas next de um usuário pelo ID e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/next")
    public ResponseEntity<List<Goal>> getNextGoalsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getNextByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas "next" de um usuário pelo ID com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getNextByUserId(Long, Pageable), Page<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas next de usuário por ID com paginação", description = "Lista metas next de um usuário pelo ID com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/next/paged")
    public ResponseEntity<Page<Goal>> getNextGoalsByUserIdPaged(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getNextByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas ativas de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getActiveByUserEmail(String), List<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas ativas de usuário por email", description = "Lista metas ativas de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/active")
    public ResponseEntity<List<Goal>> getActiveGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getActiveByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas ativas de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getActiveByUserEmail(String, Pageable), Page<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas ativas de usuário por email com paginação", description = "Lista metas ativas de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/active/paged")
    public ResponseEntity<Page<Goal>> getActiveGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getActiveByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas inativas de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getInactiveByUserEmail(String), List<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas inativas de usuário por email", description = "Lista metas inativas de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/inactive")
    public ResponseEntity<List<Goal>> getInactiveGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas inativas de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getInactiveByUserEmail(String, Pageable), Page<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas inativas de usuário por email com paginação", description = "Lista metas inativas de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/inactive/paged")
    public ResponseEntity<Page<Goal>> getInactiveGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getInactiveByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas "next" de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getNextByUserEmail(String), List<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas next de usuário por email", description = "Lista metas next de um usuário pelo email e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Goal.class),
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
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/next")
    public ResponseEntity<List<Goal>> getNextGoalsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getNextByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar metas "next" de um usuário pelo email com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de metas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see GoalService#getNextByUserEmail(String, Pageable), Page<Goal>
     */
    @Tag(name = "Metas", description = "Recurso para gerenciamento de metas")
    @Operation(summary = "Lista metas next de usuário por email com paginação", description = "Lista metas next de um usuário pelo email com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Metas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma meta encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/next/paged")
    public ResponseEntity<Page<Goal>> getNextGoalsByUserEmailPaged(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getNextByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
