package com.ifsc.ctds.stinghen.recycle_it_api.controllers.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.FriendRequestResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.FriendRequest;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.FriendRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de controle para as solicitações de amizade
 * Implementa todos os métodos públicos disponíveis na FriendRequestService
 * @see FriendRequestService, FriendRequest
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/friend-requests")
public class FriendRequestController {

    /**
     * O serviço de solicitações de amizade que permite criar, atualizar, buscar, listar e deletar solicitações de amizade
     * @see FriendRequestService
     */
    private final FriendRequestService service;

    /**
     * Método POST para criar uma nova solicitação de amizade
     * @param targetId O ID do usuário alvo
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see FriendRequestService#create(String, Long)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Cria uma nova solicitação de amizade", description = "Cria uma nova solicitação de amizade e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Solicitação de amizade criada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Solicitação de amizade criada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar solicitação de amizade")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createFriendRequest( @RequestParam @Parameter(required = true, example = "2") @NotNull @Positive Long targetId, Authentication authentication) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(authentication.getName(), targetId), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o remetente de uma solicitação de amizade
     * @param id O ID da solicitação a ser atualizada
     * @param sender O novo remetente
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see FriendRequestService#editSender(Long, User)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "[DEV] Atualiza o remetente da solicitação de amizade", description = "Atualiza o remetente de uma solicitação de amizade existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Remetente atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Remetente atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar remetente")
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @PatchMapping("/{id}/sender")
    public ResponseEntity<FeedbackResponseDTO> updateFriendRequestSender(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = RegularUser.class)),
            example = """
                    {
                        "id": 1,
                        "name": "João Silva",
                        "email": "joao@exemplo.com"
                    }
                    """) RegularUser sender) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editSender(id, sender), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o alvo de uma solicitação de amizade
     * @param id O ID da solicitação a ser atualizada
     * @param target O novo alvo
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see FriendRequestService#editTarget(Long, User)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "[DEV] Atualiza o alvo da solicitação de amizade", description = "Atualiza o alvo de uma solicitação de amizade existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Alvo atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Alvo atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar alvo")
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @PatchMapping("/{id}/target")
    public ResponseEntity<FeedbackResponseDTO> updateFriendRequestTarget(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = RegularUser.class)),
            example = """
                    {
                        "id": 2,
                        "name": "Maria Santos",
                        "email": "maria@exemplo.com"
                    }
                    """) RegularUser target) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editTarget(id, target), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a data de envio de uma solicitação de amizade
     * @param id O ID da solicitação a ser atualizada
     * @param sendDate A nova data de envio
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see FriendRequestService#editSendDate(Long, LocalDateTime)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "[DEV] Atualiza a data de envio da solicitação de amizade", description = "Atualiza a data de envio de uma solicitação de amizade existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Data de envio atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Data de envio atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar data de envio")
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasRole('DEV')")
    @PatchMapping("/{id}/send-date")
    public ResponseEntity<FeedbackResponseDTO> updateFriendRequestSendDate(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "2026-03-01T10:00:00") LocalDateTime sendDate) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editSendDate(id, sendDate), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para aceitar uma solicitação de amizade
     * @param id O ID da solicitação a ser aceita
     * @return Um ResponseEntity contendo o feedback da aceitação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see FriendRequestService#accept(Long, String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Aceita uma solicitação de amizade", description = "Aceita uma solicitação de amizade existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitação de amizade aceita com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Solicitação de amizade aceita",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao aceitar solicitação de amizade")
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/accept")
    public ResponseEntity<FeedbackResponseDTO> acceptFriendRequest(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id, Authentication authentication ) {
        try {
            String email = authentication.getName();
            return new ResponseEntity<>((FeedbackResponseDTO) service.accept(id, email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para rejeitar uma solicitação de amizade
     * @param id O ID da solicitação a ser rejeitada
     * @return Um ResponseEntity contendo o feedback da rejeição e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see FriendRequestService#reject(Long, String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Rejeita uma solicitação de amizade", description = "Rejeita uma solicitação de amizade existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitação de amizade rejeitada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Solicitação de amizade rejeitada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao rejeitar solicitação de amizade")
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<FeedbackResponseDTO> rejectFriendRequest(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            return new ResponseEntity<>((FeedbackResponseDTO) service.reject(id, email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para cancelar uma solicitação de amizade
     * @param userId O ID do usuário que está cancelando
     * @return Um ResponseEntity contendo o feedback do cancelamento e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see FriendRequestService#cancel(Long, String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Cancela uma solicitação de amizade", description = "Cancela uma solicitação de amizade existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitação de amizade cancelada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Solicitação de amizade cancelada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao cancelar solicitação de amizade")
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/cancel")
    public ResponseEntity<ResponseDTO> cancelFriendRequest(
            @RequestParam @Parameter(required = true, example = "2") @NotNull @Positive Long userId,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            return new ResponseEntity<>(service.cancel(userId, email), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar uma solicitação de amizade pelo ID
     * @param id O ID da solicitação a ser buscada
     * @return Um ResponseEntity contendo a solicitação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getObjectById(Long), FriendRequest
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Busca solicitação de amizade pelo ID", description = "Busca uma solicitação de amizade pelo ID e retorna a solicitação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitação de amizade encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = FriendRequest.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "sender": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "target": {
                            "id": 2,
                            "name": "Maria Santos",
                            "email": "maria@exemplo.com"
                        },
                        "sendDate": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<FriendRequest> getFriendRequestById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma solicitação de amizade como DTO pelo ID
     * @param id O ID da solicitação a ser buscada
     * @return Um ResponseEntity contendo a solicitação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getObjectByIdAsResponse(Long), FriendRequestResponseDTO
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Busca solicitação de amizade como DTO pelo ID", description = "Busca uma solicitação de amizade pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitação de amizade encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = FriendRequestResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "sender": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        }
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<FriendRequestResponseDTO> getFriendRequestDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FriendRequestResponseDTO) service.getObjectByIdAsResponse(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as solicitações de amizade
     * @return Um ResponseEntity contendo a lista de solicitações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getAll(), List<FriendRequest>
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista todas as solicitações de amizade", description = "Lista todas as solicitações de amizade e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso",
            content = @Content(schema = @Schema(implementation = FriendRequest.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "sender": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "target": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "sendDate": "2026-03-01T10:00:00"
                        },
                        {
                            "id": 2,
                            "sender": {
                                "id": 3,
                                "name": "Pedro Costa",
                                "email": "pedro@exemplo.com"
                            },
                            "target": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "sendDate": "2026-03-02T15:30:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<FriendRequest>> getAllFriendRequests() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as solicitações de amizade com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de solicitações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getAll(Pageable), Page<FriendRequest>
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista todas as solicitações de amizade com paginação", description = "Lista todas as solicitações de amizade com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<FriendRequest>> getAllFriendRequestsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar solicitações de amizade por ID do alvo
     * @param targetId O ID do usuário alvo
     * @return Um ResponseEntity contendo a lista de solicitações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getByTargetId(Long), List<FriendRequest>
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações de amizade por ID do alvo", description = "Lista solicitações de amizade por ID do usuário alvo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso",
            content = @Content(schema = @Schema(implementation = FriendRequest.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "sender": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "target": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "sendDate": "2026-03-01T10:00:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/target/{targetId}")
    public ResponseEntity<List<FriendRequest>> getFriendRequestsByTargetId(
            @PathVariable @Parameter(required = true, example = "2") @NotNull @Positive Long targetId) {
        try {
            return new ResponseEntity<>(service.getByTargetId(targetId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar solicitações de amizade por email do alvo
     * @param targetEmail O email do usuário alvo
     * @return Um ResponseEntity contendo a lista de solicitações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getByTargetEmail(String), List<FriendRequest>
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações de amizade por email do alvo", description = "Lista solicitações de amizade por email do usuário alvo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso",
            content = @Content(schema = @Schema(implementation = FriendRequest.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "sender": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "target": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "sendDate": "2026-03-01T10:00:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/target-email/{targetEmail}")
    public ResponseEntity<List<FriendRequest>> getFriendRequestsByTargetEmail(
            @PathVariable @Parameter(required = true, example = "maria@exemplo.com") String targetEmail) {
        try {
            return new ResponseEntity<>(service.getByTargetEmail(targetEmail), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as solicitações de amizade como DTO
     * @return Um ResponseEntity contendo a lista de solicitações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getAllAsResponse(), List<FriendRequestResponseDTO>
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista todas as solicitações de amizade como DTO", description = "Lista todas as solicitações de amizade como DTO e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso",
            content = @Content(schema = @Schema(implementation = FriendRequestResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "sender": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            }
                        },
                        {
                            "id": 2,
                            "sender": {
                                "id": 3,
                                "name": "Pedro Costa",
                                "email": "pedro@exemplo.com"
                            }
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto")
    public ResponseEntity<List<FriendRequestResponseDTO>> getAllFriendRequestsAsDto() {
        try {
            return new ResponseEntity<>( service.getAllAsResponse(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as solicitações de amizade como DTO com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de solicitações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getAllAsResponse(Pageable), Page<FriendRequestResponseDTO>
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista todas as solicitações de amizade como DTO com paginação", description = "Lista todas as solicitações de amizade como DTO com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/paged")
    public ResponseEntity<Page<FriendRequestResponseDTO>> getAllFriendRequestsAsDtoPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>( service.getAllAsResponse(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar uma solicitação de amizade pelo ID
     * @param id O ID da solicitação a ser deletada
     * @return Um ResponseEntity contendo o feedback da deleção e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#deleteById(Long)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Deleta uma solicitação de amizade pelo ID", description = "Deleta uma solicitação de amizade existente pelo ID e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitação de amizade deletada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
                    examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Solicitação de amizade deletada",
                        "content": "A solicitação de amizade com o ID 1 foi removida do banco de dados",
                        "isAlert": true,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteFriendRequest(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para verificar se o usuário autenticado enviou uma solicitação de amizade para outro usuário
     * @param targetId O ID do usuário alvo da solicitação
     * @param authentication Informações de autenticação do usuário
     * @return Um ResponseEntity contendo true se existe solicitação, false caso contrário, e o status HTTP 200 (OK)
     * @see FriendRequestService#existsBySenderAndTargetId(String, String) 
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Verifica se usuário enviou solicitação para alvo", description = "Verifica se o usuário autenticado enviou uma solicitação de amizade para outro usuário pelo ID e retorna boolean com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso",
            content = @Content(schema = @Schema(implementation = Boolean.class),
            examples = @ExampleObject(value = "true")))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/check-sent/{targetId}")
    public ResponseEntity<Boolean> hasSentRequest(
            @PathVariable @Parameter(required = true, example = "2") @NotNull @Positive Long targetId,
            Authentication authentication) {
        try {
            String senderEmail = authentication.getName();
            boolean hasRequest = service.existsBySenderEmailAndTargetId(senderEmail, targetId);
            return new ResponseEntity<>(hasRequest, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para verificar se o usuário autenticado recebeu uma solicitação de amizade de outro usuário
     * @param senderId O ID do usuário que enviou a solicitação
     * @param authentication Informações de autenticação do usuário
     * @return Um ResponseEntity contendo true se existe solicitação, false caso contrário, e o status HTTP 200 (OK)
     * @see FriendRequestService#existsBySenderEmailAndTargetId(String, Long) (String, String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Verifica se usuário recebeu solicitação de remetente", description = "Verifica se o usuário autenticado recebeu uma solicitação de amizade de outro usuário pelo ID e retorna boolean com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso",
            content = @Content(schema = @Schema(implementation = Boolean.class),
            examples = @ExampleObject(value = "true")))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/check-received/{senderId}")
    public ResponseEntity<Boolean> hasReceivedRequest(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long senderId,
            Authentication authentication) {
        try {
            String targetEmail = authentication.getName();
            boolean hasRequest = service.existsBySenderIdAndTargetEmail(senderId, targetEmail);
            return new ResponseEntity<>(hasRequest, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para contar quantas solicitações de amizade o usuário logado recebeu
     * @param authentication O objeto de autenticação para extrair o email do usuário
     * @return Um ResponseEntity contendo o número de solicitações recebidas e o status HTTP 200 (OK)
     * @see FriendRequestService#countReceivedRequests(String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Contabiliza solicitações recebidas pelo usuário logado", description = "Contabiliza quantas solicitações de amizade o usuário autenticado recebeu e retorna o número com status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Contagem realizada com sucesso",
            content = @Content(schema = @Schema(implementation = Long.class),
            examples = @ExampleObject(value = "5")))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/count/received")
    public ResponseEntity<Long> countReceivedRequests(Authentication authentication) {
        try {
            String email = authentication.getName();
            long count = service.countReceivedRequests(email);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter uma solicitação de amizade como DTO por ID do destinatário e email do remetente
     * @param targetId O ID do usuário destinatário
     * @param senderEmail O email do usuário remetente
     * @return Um ResponseEntity contendo a solicitação como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getByTargetIdAndSenderEmailAsDTO(Long, String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Obtém solicitação como DTO por targetId e senderEmail", description = "Obtém uma solicitação de amizade como DTO por ID do destinatário e email do remetente e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitação de amizade encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/target/{targetId}/sender/{senderEmail}")
    public ResponseEntity<FriendRequestResponseDTO> getFriendRequestByTargetIdAndSenderEmail(
            @PathVariable @Parameter(required = true, example = "2") @NotNull @Positive Long targetId,
            @PathVariable @Parameter(required = true, example = "user@example.com") @NotNull @NotBlank String senderEmail) {
        try {
            FriendRequestResponseDTO dto = service.getByTargetIdAndSenderEmailAsDTO(targetId, senderEmail);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter uma solicitação de amizade como DTO por ID do destinatário e email do usuário autenticado
     * @param targetId O ID do usuário destinatário
     * @param authentication Informações de autenticação do usuário
     * @return Um ResponseEntity contendo a solicitação como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getByTargetIdAndSenderEmailAsDTO(Long, String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Obtém solicitação como DTO por targetId e email autenticado", description = "Obtém uma solicitação de amizade como DTO por ID do destinatário e email do usuário autenticado e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitação de amizade encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Solicitação de amizade não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/target/{targetId}/me")
    public ResponseEntity<FriendRequestResponseDTO> getFriendRequestByTargetIdAndAuthenticatedEmail(
            @PathVariable @Parameter(required = true, example = "2") @NotNull @Positive Long targetId,
            Authentication authentication) {
        try {
            String senderEmail = authentication.getName();
            FriendRequestResponseDTO dto = service.getByTargetIdAndSenderEmailAsDTO(targetId, senderEmail);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter solicitações de amizade como DTO por ID do destinatário (retornando os senders)
     * @param targetId O ID do usuário destinatário
     * @return Um ResponseEntity contendo a lista de solicitações como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getByTargetIdAsDTO(Long)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações como DTO por targetId", description = "Lista solicitações de amizade como DTO por ID do destinatário (retornando os senders) e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/target/{targetId}")
    public ResponseEntity<List<FriendRequestResponseDTO>> getFriendRequestsByTargetIdAsDTO(
            @PathVariable @Parameter(required = true, example = "2") @NotNull @Positive Long targetId) {
        try {
            List<FriendRequestResponseDTO> dtos = service.getByTargetIdAsDTO(targetId);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter solicitações de amizade como DTO com paginação por ID do destinatário (retornando os senders)
     * @param targetId O ID do usuário destinatário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de solicitações como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getByTargetIdAsDTO(Long, Pageable)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações como DTO com paginação por targetId", description = "Lista solicitações de amizade como DTO com paginação por ID do destinatário (retornando os senders) e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/target/{targetId}/paged")
    public ResponseEntity<Page<FriendRequestResponseDTO>> getFriendRequestsByTargetIdPaged(
            @PathVariable @Parameter(required = true, example = "2") @NotNull @Positive Long targetId,
            Pageable pageable) {
        try {
            Page<FriendRequestResponseDTO> dtos = service.getByTargetIdAsDTO(targetId, pageable);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // === MÉTODOS POR SENDER ===

    /**
     * Método GET para listar solicitações de amizade por ID do remetente
     * @param senderId O ID do usuário remetente
     * @return Um ResponseEntity contendo a lista de solicitações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getBySenderId(Long), List<FriendRequest>
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações de amizade por ID do remetente", description = "Lista solicitações de amizade por ID do usuário remetente e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso",
            content = @Content(schema = @Schema(implementation = FriendRequest.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "sender": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "target": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "sendDate": "2026-03-01T10:00:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<FriendRequest>> getFriendRequestsBySenderId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long senderId) {
        try {
            return new ResponseEntity<>(service.getBySenderId(senderId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar solicitações de amizade por email do remetente
     * @param senderEmail O email do usuário remetente
     * @return Um ResponseEntity contendo a lista de solicitações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see FriendRequestService#getBySenderEmail(String), List<FriendRequest>
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações de amizade por email do remetente", description = "Lista solicitações de amizade por email do usuário remetente e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso",
            content = @Content(schema = @Schema(implementation = FriendRequest.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "sender": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "target": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "sendDate": "2026-03-01T10:00:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/sender-email/{senderEmail}")
    public ResponseEntity<List<FriendRequest>> getFriendRequestsBySenderEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String senderEmail) {
        try {
            return new ResponseEntity<>(service.getBySenderEmail(senderEmail), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter solicitações de amizade como DTO por email do remetente (retornando os targets)
     * @param senderEmail O email do usuário remetente
     * @return Um ResponseEntity contendo a lista de solicitações como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getBySenderEmailAsDTO(String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações como DTO por senderEmail", description = "Lista solicitações de amizade como DTO por email do remetente (retornando os targets) e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/sender/{senderEmail}")
    public ResponseEntity<List<FriendRequestResponseDTO>> getFriendRequestsBySenderEmailAsDTO(
            @PathVariable @Parameter(required = true, example = "user@example.com") @NotNull @NotBlank String senderEmail) {
        try {
            List<FriendRequestResponseDTO> dtos = service.getBySenderEmailAsDTO(senderEmail);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter solicitações de amizade como DTO por email do usuário autenticado (retornando os targets)
     * @param authentication Informações de autenticação do usuário
     * @return Um ResponseEntity contendo a lista de solicitações como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getBySenderEmailAsDTO(String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações como DTO por email autenticado", description = "Lista solicitações de amizade como DTO por email do usuário autenticado (retornando os targets) e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/send/me")
    public ResponseEntity<List<FriendRequestResponseDTO>> getFriendRequestsByAuthenticatedEmail(
            Authentication authentication) {
        try {
            String senderEmail = authentication.getName();
            List<FriendRequestResponseDTO> dtos = service.getBySenderEmailAsDTO(senderEmail);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter solicitações de amizade como DTO com paginação por email do remetente (retornando os targets)
     * @param senderEmail O email do usuário remetente
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de solicitações como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getBySenderEmailAsDTO(String, Pageable)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações como DTO com paginação por senderEmail", description = "Lista solicitações de amizade como DTO com paginação por email do remetente (retornando os targets) e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/sender/{senderEmail}/paged")
    public ResponseEntity<Page<FriendRequestResponseDTO>> getFriendRequestsBySenderEmailPaged(
            @PathVariable @Parameter(required = true, example = "user@example.com") @NotNull @NotBlank String senderEmail,
            Pageable pageable) {
        try {
            Page<FriendRequestResponseDTO> dtos = service.getBySenderEmailAsDTO(senderEmail, pageable);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter solicitações de amizade como DTO com paginação por email do usuário autenticado (retornando os targets)
     * @param authentication Informações de autenticação do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de solicitações como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getBySenderEmailAsDTO(String, Pageable)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações como DTO com paginação por email autenticado", description = "Lista solicitações de amizade como DTO com paginação por email do usuário autenticado (retornando os targets) e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/send/me/paged")
    public ResponseEntity<Page<FriendRequestResponseDTO>> getFriendRequestsByAuthenticatedEmailPaged(
            Authentication authentication,
            Pageable pageable) {
        try {
            String senderEmail = authentication.getName();
            Page<FriendRequestResponseDTO> dtos = service.getBySenderEmailAsDTO(senderEmail, pageable);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // === MÉTODOS PARA PEDIDOS RECEBIDOS (TARGET) COM AUTH ===

    /**
     * Método GET para obter solicitações de amizade como DTO por email do usuário autenticado como target (retornando os senders)
     * @param authentication Informações de autenticação do usuário
     * @return Um ResponseEntity contendo a lista de solicitações como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getByTargetEmailAsDTO(String)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações recebidas como DTO por email autenticado", description = "Lista solicitações de amizade como DTO por email do usuário autenticado como target (retornando os senders) e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/target/me")
    public ResponseEntity<List<FriendRequestResponseDTO>> getReceivedFriendRequestsByAuthenticatedEmail(
            Authentication authentication) {
        try {
            String targetEmail = authentication.getName();
            List<FriendRequestResponseDTO> dtos = service.getByTargetEmailAsDTO(targetEmail);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para obter solicitações de amizade como DTO com paginação por email do usuário autenticado como target (retornando os senders)
     * @param authentication Informações de autenticação do usuário
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de solicitações como DTO e o status HTTP 200 (OK)
     * @see FriendRequestService#getByTargetEmailAsDTO(String, Pageable)
     */
    @Tag(name = "Solicitações de Amizade", description = "Recurso para gerenciamento de solicitações de amizade")
    @Operation(summary = "Lista solicitações recebidas como DTO com paginação por email autenticado", description = "Lista solicitações de amizade como DTO com paginação por email do usuário autenticado como target (retornando os senders) e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Solicitações de amizade listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma solicitação de amizade encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/target/me/paged")
    public ResponseEntity<Page<FriendRequestResponseDTO>> getReceivedFriendRequestsByAuthenticatedEmailPaged(
            Authentication authentication,
            Pageable pageable) {
        try {
            String targetEmail = authentication.getName();
            Page<FriendRequestResponseDTO> dtos = service.getByTargetEmailAsDTO(targetEmail, pageable);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
