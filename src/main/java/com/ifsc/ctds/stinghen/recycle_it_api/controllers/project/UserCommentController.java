package com.ifsc.ctds.stinghen.recycle_it_api.controllers.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.UserCommentResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.UserComment;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.services.project.UserCommentService;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de controle para os comentários de usuários
 * Implementa todos os métodos públicos disponíveis na UserCommentService
 * @see UserCommentService, UserComment
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user-comments")
public class UserCommentController {

    /**
     * O serviço de comentários de usuários que permite criar, atualizar, buscar, listar e deletar comentários
     * @see UserCommentService
     */
    private final UserCommentService service;

    /**
     * Método POST para criar um novo comentário de usuário
     * @param comment O texto do comentário
     * @param userEmail O email do usuário
     * @param projectId O ID do projeto
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see UserCommentService#create(String, String, Long)
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Cria um novo comentário de usuário", description = "Cria um novo comentário de usuário e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Comentário criado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Comentário criado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar comentário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createUserComment(
            @RequestParam @Parameter(required = true, example = "Ótimo projeto de reciclagem!") String comment,
            @RequestParam @Parameter(required = true, example = "usuario@exemplo.com") String userEmail,
            @RequestParam @Parameter(required = true, example = "1") @NotNull @Positive Long projectId) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(comment, userEmail, projectId), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar um comentário de usuário existente
     * @param id O ID do comentário a ser atualizado
     * @param comment O comentário atualizado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see UserCommentService#update(Long, UserComment)
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Atualiza um comentário de usuário", description = "Atualiza um comentário de usuário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Comentário atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar comentário")
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateUserComment(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = UserComment.class)),
            example = """
                    {
                        "text": "Comentário atualizado",
                        "date": "2026-03-01T10:00:00"
                    }
                    """) UserComment comment) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, comment), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o usuário de um comentário
     * @param id O ID do comentário a ser atualizado
     * @param user O novo usuário
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see UserCommentService#editUser(Long, User)
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Atualiza o usuário do comentário", description = "Atualiza o usuário de um comentário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuário do comentário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Usuário do comentário atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar usuário do comentário")
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/user")
    public ResponseEntity<FeedbackResponseDTO> updateUserCommentUser(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = RegularUser.class)),
            example = """
                    {
                        "id": 2,
                        "name": "Maria Santos",
                        "email": "maria@exemplo.com"
                    }
                    """) User user) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editUser(id, user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o texto de um comentário
     * @param id O ID do comentário a ser atualizado
     * @param text O novo texto do comentário
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see UserCommentService#editText(Long, String)
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Atualiza o texto do comentário", description = "Atualiza o texto de um comentário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Texto do comentário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Texto do comentário atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar texto do comentário")
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/text")
    public ResponseEntity<FeedbackResponseDTO> updateUserCommentText(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Novo texto do comentário") String text) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editText(id, text), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a data de um comentário
     * @param id O ID do comentário a ser atualizado
     * @param date A nova data do comentário
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see UserCommentService#editDate(Long, LocalDateTime)
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Atualiza a data do comentário", description = "Atualiza a data de um comentário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Data do comentário atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Data do comentário atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar data do comentário")
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/date")
    public ResponseEntity<FeedbackResponseDTO> updateUserCommentDate(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "2026-03-01T10:00:00") LocalDateTime date) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editDate(id, date), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um comentário de usuário pelo ID
     * @param id O ID do comentário a ser buscado
     * @return Um ResponseEntity contendo o comentário e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see UserCommentService#getObjectById(Long), UserComment
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Busca comentário de usuário pelo ID", description = "Busca um comentário de usuário pelo ID e retorna o comentário com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentário encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = UserComment.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "text": "Ótimo projeto de reciclagem!",
                        "date": "2026-03-01T10:00:00",
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "project": {
                            "id": 1,
                            "description": "Projeto de reciclagem"
                        }
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<UserComment> getUserCommentById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um comentário de usuário como DTO pelo ID
     * @param id O ID do comentário a ser buscado
     * @return Um ResponseEntity contendo o comentário e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see UserCommentService#getById(Long), UserCommentResponseDTO
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Busca comentário de usuário como DTO pelo ID", description = "Busca um comentário de usuário pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentário encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = UserCommentResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "user": {
                            "id": 1,
                            "name": "João Silva",
                            "email": "joao@exemplo.com"
                        },
                        "text": "Ótimo projeto de reciclagem!",
                        "date": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<UserCommentResponseDTO> getUserCommentDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((UserCommentResponseDTO) service.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os comentários de usuários
     * @return Um ResponseEntity contendo a lista de comentários e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see UserCommentService#getAll(), List<UserComment>
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Lista todos os comentários de usuários", description = "Lista todos os comentários de usuários e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentários listados com sucesso",
            content = @Content(schema = @Schema(implementation = UserComment.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "text": "Ótimo projeto de reciclagem!",
                            "date": "2026-03-01T10:00:00",
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "project": {
                                "id": 1,
                                "description": "Projeto de reciclagem"
                            }
                        },
                        {
                            "id": 2,
                            "text": "Excelente iniciativa!",
                            "date": "2026-03-02T15:30:00",
                            "user": {
                                "id": 2,
                                "name": "Maria Santos",
                                "email": "maria@exemplo.com"
                            },
                            "project": {
                                "id": 1,
                                "description": "Projeto de reciclagem"
                            }
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum comentário encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<UserComment>> getAllUserComments() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os comentários de usuários com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de comentários e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see UserCommentService#getAll(Pageable), Page<UserComment>
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Lista todos os comentários de usuários com paginação", description = "Lista todos os comentários de usuários com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentários listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum comentário encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<UserComment>> getAllUserCommentsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar comentários de usuários por ID de usuário
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de comentários e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see UserCommentService#getByUserId(Long), List<UserComment>
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Lista comentários de usuários por ID de usuário", description = "Lista comentários de usuários por ID de usuário e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentários listados com sucesso",
            content = @Content(schema = @Schema(implementation = UserComment.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "text": "Ótimo projeto de reciclagem!",
                            "date": "2026-03-01T10:00:00",
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "project": {
                                "id": 1,
                                "description": "Projeto de reciclagem"
                            }
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum comentário encontrado para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserComment>> getUserCommentsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar comentários de usuários por ID de usuário ordenados por data descendente
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de comentários e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see UserCommentService#getByUserIdOrderByDateDesc(Long), List<UserComment>
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Lista comentários de usuários por ID de usuário ordenados por data", description = "Lista comentários de usuários por ID de usuário ordenados por data descendente e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentários listados com sucesso",
            content = @Content(schema = @Schema(implementation = UserComment.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 2,
                            "text": "Excelente iniciativa!",
                            "date": "2026-03-02T15:30:00",
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "project": {
                                "id": 1,
                                "description": "Projeto de reciclagem"
                            }
                        },
                        {
                            "id": 1,
                            "text": "Ótimo projeto de reciclagem!",
                            "date": "2026-03-01T10:00:00",
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "project": {
                                "id": 1,
                                "description": "Projeto de reciclagem"
                            }
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum comentário encontrado para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/ordered")
    public ResponseEntity<List<UserComment>> getUserCommentsByUserIdOrdered(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getByUserIdOrderByDateDesc(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar comentários de usuários por email de usuário
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de comentários e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see UserCommentService#getByUserEmail(String), List<UserComment>
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Lista comentários de usuários por email de usuário", description = "Lista comentários de usuários por email de usuário e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentários listados com sucesso",
            content = @Content(schema = @Schema(implementation = UserComment.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "text": "Ótimo projeto de reciclagem!",
                            "date": "2026-03-01T10:00:00",
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "project": {
                                "id": 1,
                                "description": "Projeto de reciclagem"
                            }
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum comentário encontrado para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}")
    public ResponseEntity<List<UserComment>> getUserCommentsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar comentários de usuários por email de usuário ordenados por data descendente
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de comentários e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see UserCommentService#getByUserEmailOrderByDateDesc(String), List<UserComment>
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Lista comentários de usuários por email de usuário ordenados por data", description = "Lista comentários de usuários por email de usuário ordenados por data descendente e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentários listados com sucesso",
            content = @Content(schema = @Schema(implementation = UserComment.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 2,
                            "text": "Excelente iniciativa!",
                            "date": "2026-03-02T15:30:00",
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "project": {
                                "id": 1,
                                "description": "Projeto de reciclagem"
                            }
                        },
                        {
                            "id": 1,
                            "text": "Ótimo projeto de reciclagem!",
                            "date": "2026-03-01T10:00:00",
                            "user": {
                                "id": 1,
                                "name": "João Silva",
                                "email": "joao@exemplo.com"
                            },
                            "project": {
                                "id": 1,
                                "description": "Projeto de reciclagem"
                            }
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum comentário encontrado para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/ordered")
    public ResponseEntity<List<UserComment>> getUserCommentsByUserEmailOrdered(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getByUserEmailOrderByDateDesc(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um comentário de usuário
     * @param id O ID do comentário a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see UserCommentService#deleteById(Long)
     */
    @Tag(name = "Comentários de Usuários", description = "Recurso para gerenciamento de comentários de usuários")
    @Operation(summary = "Deleta um comentário de usuário", description = "Deleta um comentário de usuário e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Comentário deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Comentário deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar comentário")
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteUserComment(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
