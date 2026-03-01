package com.ifsc.ctds.stinghen.recycle_it_api.controllers.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.LeagueSessionResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.UserPunctuationResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.LeagueSession;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.UserPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.services.league.LeagueSessionService;
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

import java.time.LocalDate;
import java.util.List;

/**
 * Classe de controle para as sessões de liga
 * Implementa todos os métodos públicos disponíveis na LeagueSessionService
 * @see LeagueSessionService, LeagueSession
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/league-sessions")
public class LeagueSessionController {

    /**
     * O serviço de sessões de liga que permite criar, atualizar, buscar, listar e deletar sessões de liga
     * @see LeagueSessionService
     */
    private final LeagueSessionService service;

    /**
     * Método POST para criar uma nova sessão de liga
     * @param session A {@link LeagueSession} contendo os dados da sessão
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see LeagueSessionService#create(LeagueSession), LeagueSession
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Cria uma nova sessão de liga", description = "Cria uma nova sessão de liga e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Sessão de liga criada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Sessão de liga criada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar sessão de liga")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createLeagueSession(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = LeagueSession.class)),
            example = """
                    {
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        },
                        "startDate": "2026-03-01",
                        "endDate": "2026-03-31"
                    }
                    """) @Valid LeagueSession session) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(session), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a data de início de uma sessão de liga
     * @param id O ID da sessão a ser atualizada
     * @param startDate A nova data de início
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueSessionService#editStartDate(Long, LocalDate)
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Atualiza a data de início da sessão de liga", description = "Atualiza a data de início de uma sessão de liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Data de início atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Data de início atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar data de início")
    @ApiResponse(responseCode = "404", description = "Sessão de liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/start-date")
    public ResponseEntity<FeedbackResponseDTO> updateLeagueSessionStartDate(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "2026-03-01") LocalDate startDate) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editStartDate(id, startDate), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a data de término de uma sessão de liga
     * @param id O ID da sessão a ser atualizada
     * @param endDate A nova data de término
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueSessionService#editEndDate(Long, LocalDate)
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Atualiza a data de término da sessão de liga", description = "Atualiza a data de término de uma sessão de liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Data de término atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Data de término atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar data de término")
    @ApiResponse(responseCode = "404", description = "Sessão de liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/end-date")
    public ResponseEntity<FeedbackResponseDTO> updateLeagueSessionEndDate(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "2026-03-31") LocalDate endDate) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editEndDate(id, endDate), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar uma sessão de liga pelo ID
     * @param id O ID da sessão a ser buscada
     * @return Um ResponseEntity contendo a sessão e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getObjectById(Long), LeagueSession
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca sessão de liga pelo ID", description = "Busca uma sessão de liga pelo ID e retorna a sessão com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessão de liga encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = LeagueSession.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        },
                        "startDate": "2026-03-01",
                        "endDate": "2026-03-31"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Sessão de liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<LeagueSession> getLeagueSessionById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma sessão de liga como DTO pelo ID
     * @param id O ID da sessão a ser buscada
     * @return Um ResponseEntity contendo a sessão e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getById(Long), LeagueSessionResponseDTO
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca sessão de liga como DTO pelo ID", description = "Busca uma sessão de liga pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessão de liga encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = LeagueSessionResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        },
                        "startDate": "2026-03-01",
                        "endDate": "2026-03-31",
                        "users": [
                            {
                                "user": {
                                    "id": 1,
                                    "name": "João Silva",
                                    "email": "joao@exemplo.com"
                                },
                                "punctuation": 150
                            }
                        ]
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Sessão de liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<LeagueSessionResponseDTO> getLeagueSessionDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((LeagueSessionResponseDTO) service.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as sessões de liga
     * @return Um ResponseEntity contendo a lista de sessões e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getAll(), List<LeagueSession>
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Lista todas as sessões de liga", description = "Lista todas as sessões de liga e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessões de liga listadas com sucesso",
            content = @Content(schema = @Schema(implementation = LeagueSession.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "league": {
                                "id": 1,
                                "name": "Liga Bronze",
                                "tier": 1
                            },
                            "startDate": "2026-03-01",
                            "endDate": "2026-03-31"
                        },
                        {
                            "id": 2,
                            "league": {
                                "id": 2,
                                "name": "Liga Prata",
                                "tier": 2
                            },
                            "startDate": "2026-02-01",
                            "endDate": "2026-02-28"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão de liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<LeagueSession>> getAllLeagueSessions() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as sessões de liga ordenadas por data de término com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de sessões e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getAllOrderedByEndDate(Pageable), Page<LeagueSession>
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Lista todas as sessões de liga ordenadas por data de término com paginação", description = "Lista todas as sessões de liga ordenadas por data de término com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessões de liga listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão de liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/ordered-by-end-date")
    public ResponseEntity<Page<LeagueSession>> getAllLeagueSessionsOrderedByEndDate(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllOrderedByEndDate(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as sessões de liga com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de sessões e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getAll(Pageable), Page<LeagueSession>
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Lista todas as sessões de liga com paginação", description = "Lista todas as sessões de liga com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessões de liga listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão de liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<LeagueSession>> getAllLeagueSessionsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar sessões de liga por ID de usuário com paginação
     * @param userId O ID do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de sessões e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getByUserId(Long, Pageable), Page<LeagueSession>
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Lista sessões de liga por ID de usuário com paginação", description = "Lista sessões de liga por ID de usuário com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessões de liga listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão de liga encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<LeagueSession>> getLeagueSessionsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByUserId(userId, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar sessões de liga por email de usuário com paginação
     * @param email O email do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de sessões e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getByUserEmail(String, Pageable), Page<LeagueSession>
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Lista sessões de liga por email de usuário com paginação", description = "Lista sessões de liga por email de usuário com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessões de liga listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão de liga encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}")
    public ResponseEntity<Page<LeagueSession>> getLeagueSessionsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByUserEmail(email, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a sessão ativa de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a sessão e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActiveSessionByUserId(Long), LeagueSession
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca sessão ativa de usuário por ID", description = "Busca a sessão ativa de um usuário pelo ID e retorna a sessão com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessão ativa encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = LeagueSession.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        },
                        "startDate": "2026-03-01",
                        "endDate": "2026-03-31"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão ativa encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<LeagueSession> getActiveLeagueSessionByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getActiveSessionByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a sessão ativa de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a sessão e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActiveSessionByUserEmail(String), LeagueSession
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca sessão ativa de usuário por email", description = "Busca a sessão ativa de um usuário pelo email e retorna a sessão com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessão ativa encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = LeagueSession.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        },
                        "startDate": "2026-03-01",
                        "endDate": "2026-03-31"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão ativa encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/active")
    public ResponseEntity<LeagueSession> getActiveLeagueSessionByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getActiveSessionByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a sessão ativa de um usuário como DTO pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a sessão e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActiveSessionResponseByUserId(Long), LeagueSessionResponseDTO
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca sessão ativa de usuário como DTO por ID", description = "Busca a sessão ativa de um usuário pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessão ativa encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = LeagueSessionResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        },
                        "startDate": "2026-03-01",
                        "endDate": "2026-03-31",
                        "users": [
                            {
                                "user": {
                                    "id": 1,
                                    "name": "João Silva",
                                    "email": "joao@exemplo.com"
                                },
                                "punctuation": 150
                            }
                        ]
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão ativa encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/active/dto")
    public ResponseEntity<LeagueSessionResponseDTO> getActiveLeagueSessionDtoByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>((LeagueSessionResponseDTO) service.getActiveSessionResponseByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a sessão ativa de um usuário como DTO pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a sessão e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActiveSessionResponseByUserEmail(String), LeagueSessionResponseDTO
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca sessão ativa de usuário como DTO por email", description = "Busca a sessão ativa de um usuário pelo email e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessão ativa encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = LeagueSessionResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        },
                        "startDate": "2026-03-01",
                        "endDate": "2026-03-31",
                        "users": [
                            {
                                "user": {
                                    "id": 1,
                                    "name": "João Silva",
                                    "email": "joao@exemplo.com"
                                },
                                "punctuation": 150
                            }
                        ]
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma sessão ativa encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/active/dto")
    public ResponseEntity<LeagueSessionResponseDTO> getActiveLeagueSessionDtoByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((LeagueSessionResponseDTO) service.getActiveSessionResponseByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a pontuação ativa de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActivePunctuationByUserId(Long), UserPunctuation
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca pontuação ativa de usuário por ID", description = "Busca a pontuação ativa de um usuário pelo ID e retorna a pontuação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação ativa encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = UserPunctuation.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "session": {
                            "id": 1,
                            "league": {
                                "id": 1,
                                "name": "Liga Bronze",
                                "tier": 1
                            }
                        },
                        "punctuation": {
                            "points": 150,
                            "gems": 10,
                            "badges": 5
                        }
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação ativa encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/punctuation")
    public ResponseEntity<UserPunctuation> getActivePunctuationByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getActivePunctuationByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a pontuação ativa de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActivePunctuationByUserEmail(String), UserPunctuation
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca pontuação ativa de usuário por email", description = "Busca a pontuação ativa de um usuário pelo email e retorna a pontuação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação ativa encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = UserPunctuation.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "session": {
                            "id": 1,
                            "league": {
                                "id": 1,
                                "name": "Liga Bronze",
                                "tier": 1
                            }
                        },
                        "punctuation": {
                            "points": 150,
                            "gems": 10,
                            "badges": 5
                        }
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação ativa encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/punctuation")
    public ResponseEntity<UserPunctuation> getActivePunctuationByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getActivePunctuationByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar os pontos ativos de um usuário pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo os pontos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActivePointsPunctuationByUserId(Long), PointsPunctuation
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca pontos ativos de usuário por ID", description = "Busca os pontos ativos de um usuário pelo ID e retorna os pontos com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos ativos encontrados com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuation.class),
            examples = @ExampleObject(value = """
                    {
                        "points": 150, 
                        "gems": 10,
                        "badges": 5
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum ponto ativo encontrado para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/points")
    public ResponseEntity<PointsPunctuation> getActivePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getActivePointsPunctuationByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar os pontos ativos de um usuário pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo os pontos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActivePointsPunctuationByUserEmail(String), PointsPunctuation
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca pontos ativos de usuário por email", description = "Busca os pontos ativos de um usuário pelo email e retorna os pontos com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos ativos encontrados com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuation.class),
            examples = @ExampleObject(value = """
                    {
                        "points": 150,
                        "gems": 10,
                        "badges": 5
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum ponto ativo encontrado para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/points")
    public ResponseEntity<PointsPunctuation> getActivePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getActivePointsPunctuationByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a pontuação ativa de um usuário como DTO pelo ID
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActivePunctuationResponseByUserId(Long), UserPunctuationResponseDTO
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca pontuação ativa de usuário como DTO por ID", description = "Busca a pontuação ativa de um usuário pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação ativa encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = UserPunctuationResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "punctuation": 150
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação ativa encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/punctuation/dto")
    public ResponseEntity<UserPunctuationResponseDTO> getActivePunctuationDtoByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>((UserPunctuationResponseDTO) service.getActivePunctuationResponseByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a pontuação ativa de um usuário como DTO pelo email
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueSessionService#getActivePunctuationResponseByUserEmail(String), UserPunctuationResponseDTO
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Busca pontuação ativa de usuário como DTO por email", description = "Busca a pontuação ativa de um usuário pelo email e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação ativa encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = UserPunctuationResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "punctuation": 150
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação ativa encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/punctuation/dto")
    public ResponseEntity<UserPunctuationResponseDTO> getActivePunctuationDtoByUserEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((UserPunctuationResponseDTO) service.getActivePunctuationResponseByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar uma sessão de liga
     * @param id O ID da sessão a ser deletada
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueSessionService#deleteById(Long)
     */
    @Tag(name = "Sessões de Liga", description = "Recurso para gerenciamento de sessões de liga")
    @Operation(summary = "Deleta uma sessão de liga", description = "Deleta uma sessão de liga e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Sessão de liga deletada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Sessão de liga deletada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar sessão de liga")
    @ApiResponse(responseCode = "404", description = "Sessão de liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteLeagueSession(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
