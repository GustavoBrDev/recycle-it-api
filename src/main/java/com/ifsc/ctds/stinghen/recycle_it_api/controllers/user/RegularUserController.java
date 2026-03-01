package com.ifsc.ctds.stinghen.recycle_it_api.controllers.user;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user.RegularUserPutRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user.RegularUserRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.FullUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.user.SimpleUserResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
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
 * Classe de controle para os usuários comuns
 * @see RegularUserService, com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class RegularUserController {

    /**
     * O serviço de usuários comuns que permite criar, atualizar, buscar, listar e deletar usuários
     * @see RegularUserService
     */
    private RegularUserService service;

    /**
     * Método POST para criar um novo usuário
     * @param requestDTO A {@link RegularUserRequestDTO} contendo os dados do usuário
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#create(RegularUserRequestDTO), RegularUserRequestDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo usuário e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Cadastro Efetuado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar usuário - email já existe ou dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createUser(
            @RequestBody @Parameter(required = true, 
            content = @Content(schema = @Schema(implementation = RegularUserRequestDTO.class)),
            example = """
                    {
                        "email": "usuario@exemplo.com",
                        "password": "SenhaForte123!",
                        "avatar": "AVATAR_1",
                        "name": "Nome do Usuário"
                    }
                    """) @Valid RegularUserRequestDTO requestDTO) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(requestDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar um usuário existente
     * @param id O ID do usuário a ser atualizado
     * @param requestDTO A {@link RegularUserPutRequestDTO} contendo os dados do usuário
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#update(Long, RegularUserPutRequestDTO), RegularUserPutRequestDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Atualiza um usuário", description = "Atualiza um usuário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Usuário atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar usuário")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateUser(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = RegularUserPutRequestDTO.class)),
            example = """
                    {
                        "email": "novoemail@exemplo.com",
                        "password": "NovaSenha123!",
                        "avatar": "AVATAR_2",
                        "name": "Novo Nome"
                    }
                    """) @Valid RegularUserPutRequestDTO requestDTO) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, requestDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o avatar de um usuário
     * @param id O ID do usuário a ser atualizado
     * @param avatar O novo avatar
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#editAvatar(Long, Avatar)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Atualiza o avatar do usuário", description = "Atualiza o avatar de um usuário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatar atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Avatar atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar avatar")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/avatar")
    public ResponseEntity<FeedbackResponseDTO> updateAvatar(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "AVATAR_1") Avatar avatar) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editAvatar(id, avatar), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o nome de um usuário
     * @param id O ID do usuário a ser atualizado
     * @param name O novo nome
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#editName(Long, String)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Atualiza o nome do usuário", description = "Atualiza o nome de um usuário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Nome atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Nome atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar nome")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/name")
    public ResponseEntity<FeedbackResponseDTO> updateName(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Novo Nome do Usuário") String name) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editName(id, name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a senha de um usuário
     * @param id O ID do usuário a ser atualizado
     * @param password A nova senha
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#editPassword(Long, String)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Atualiza a senha do usuário", description = "Atualiza a senha de um usuário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Senha atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar senha")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/password")
    public ResponseEntity<FeedbackResponseDTO> updatePassword(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "NovaSenhaForte123!") String password) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editPassword(id, password), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o email de um usuário
     * @param id O ID do usuário a ser atualizado
     * @param email O novo email
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#editEmail(Long, String)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Atualiza o email do usuário", description = "Atualiza o email de um usuário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Email atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Email atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar email - email já existe")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/email")
    public ResponseEntity<FeedbackResponseDTO> updateEmail(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "novoemail@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editEmail(id, email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar as gems de um usuário
     * @param id O ID do usuário a ser atualizado
     * @param gems A nova quantidade de gems
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#editGems(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Atualiza as gems do usuário", description = "Atualiza a quantidade de gems de um usuário existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Gems atualizadas com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Gems atualizadas",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar gems")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/gems")
    public ResponseEntity<FeedbackResponseDTO> updateGems(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "1000") Long gems) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editGems(id, gems), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para incrementar as gems de um usuário
     * @param id O ID do usuário a ser atualizado
     * @param amount A quantidade de gems a ser adicionada
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#incrementGems(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Incrementa as gems do usuário", description = "Adiciona uma quantidade de gems ao usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Gems incrementadas com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Gems incrementadas",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar gems")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/gems/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementGems(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "500") Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementGems(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para decrementar as gems de um usuário
     * @param id O ID do usuário a ser atualizado
     * @param amount A quantidade de gems a ser removida
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#decrementGems(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Decrementa as gems do usuário", description = "Remove uma quantidade de gems do usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Gems decrementadas com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Gems decrementadas",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar gems - saldo insuficiente")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/gems/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementGems(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "200") Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementGems(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para adicionar um amigo ao usuário
     * @param userId O ID do usuário
     * @param friendId O ID do amigo a ser adicionado
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#addFriend(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Adiciona um amigo ao usuário", description = "Adiciona um amigo à lista de amigos do usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Amigo adicionado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Amigo adicionado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao adicionar amigo - relação inválida ou já existente")
    @ApiResponse(responseCode = "404", description = "Usuário ou amigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<FeedbackResponseDTO> addFriend(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @PathVariable @Parameter(required = true, example = "2") @NotNull @Positive Long friendId) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.addFriend(userId, friendId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método DELETE para remover um amigo do usuário
     * @param userId O ID do usuário
     * @param friendId O ID do amigo a ser removido
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#removeFriend(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Remove um amigo do usuário", description = "Remove um amigo da lista de amigos do usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Amigo removido com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Amigo removido",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao remover amigo - relação não existente")
    @ApiResponse(responseCode = "404", description = "Usuário ou amigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<FeedbackResponseDTO> removeFriend(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @PathVariable @Parameter(required = true, example = "2") @NotNull @Positive Long friendId) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.removeFriend(userId, friendId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para adicionar um projeto ao usuário
     * @param userId O ID do usuário
     * @param projectId O ID do projeto a ser adicionado
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#addProject(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Adiciona um projeto ao usuário", description = "Adiciona um projeto à lista de projetos do usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto adicionado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Projeto adicionado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao adicionar projeto - relação inválida ou já existente")
    @ApiResponse(responseCode = "404", description = "Usuário ou projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<FeedbackResponseDTO> addProject(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long projectId) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.addProject(userId, projectId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método DELETE para remover um projeto do usuário
     * @param userId O ID do usuário
     * @param projectId O ID do projeto a ser removido
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#removeProject(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Remove um projeto do usuário", description = "Remove um projeto da lista de projetos do usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto removido com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Projeto removido",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao remover projeto - relação não existente")
    @ApiResponse(responseCode = "404", description = "Usuário ou projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<FeedbackResponseDTO> removeProject(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long projectId) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.removeProject(userId, projectId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para adicionar um artigo ao usuário
     * @param userId O ID do usuário
     * @param articleId O ID do artigo a ser adicionado
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#addArticle(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Adiciona um artigo ao usuário", description = "Adiciona um artigo à lista de artigos do usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo adicionado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Artigo adicionado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao adicionar artigo - relação inválida ou já existente")
    @ApiResponse(responseCode = "404", description = "Usuário ou artigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{userId}/articles/{articleId}")
    public ResponseEntity<FeedbackResponseDTO> addArticle(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long articleId) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.addArticle(userId, articleId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método DELETE para remover um artigo do usuário
     * @param userId O ID do usuário
     * @param articleId O ID do artigo a ser removido
     * @return Um ResponseEntity contendo o feedback da operação e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#removeArticle(Long, Long)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Remove um artigo do usuário", description = "Remove um artigo da lista de artigos do usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Artigo removido com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Artigo removido",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao remover artigo - relação não existente")
    @ApiResponse(responseCode = "404", description = "Usuário ou artigo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{userId}/articles/{articleId}")
    public ResponseEntity<FeedbackResponseDTO> removeArticle(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long articleId) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.removeArticle(userId, articleId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um usuário completo pelo ID
     * @param id O ID do usuário a ser buscado
     * @return Um ResponseEntity contendo o usuário completo e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RegularUserService#getFullById(Long), FullUserResponseDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Busca usuário completo pelo ID", description = "Busca um usuário completo pelo ID e retorna o usuário com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = FullUserResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Nome do Usuário",
                        "currentAvatar": "AVATAR_1",
                        "gems": 1000,
                        "friends": [],
                        "projects": [],
                        "articles": [],
                        "punctuation": {
                            "id": 1,
                            "recyclePoints": 500,
                            "position": 1
                        },
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        }
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/full")
    public ResponseEntity<FullUserResponseDTO> getFullById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FullUserResponseDTO) service.getFullById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um usuário simplificado pelo ID
     * @param id O ID do usuário a ser buscado
     * @return Um ResponseEntity contendo o usuário simplificado e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RegularUserService#getSimpleById(Long), SimpleUserResponseDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Busca usuário simplificado pelo ID", description = "Busca um usuário simplificado pelo ID e retorna o usuário com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = SimpleUserResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Nome do Usuário",
                        "avatar": "AVATAR_1"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/simple")
    public ResponseEntity<SimpleUserResponseDTO> getSimpleById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((SimpleUserResponseDTO) service.getSimpleById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um usuário completo pelo email
     * @param email O email do usuário a ser buscado
     * @return Um ResponseEntity contendo o usuário completo e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RegularUserService#getFullByEmail(String), FullUserResponseDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Busca usuário completo pelo email", description = "Busca um usuário completo pelo email e retorna o usuário com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = FullUserResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Nome do Usuário",
                        "currentAvatar": "AVATAR_1",
                        "gems": 1000,
                        "friends": [],
                        "projects": [],
                        "articles": [],
                        "punctuation": {
                            "id": 1,
                            "recyclePoints": 500,
                            "position": 1
                        },
                        "league": {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1
                        }
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/full")
    public ResponseEntity<FullUserResponseDTO> getFullByEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((FullUserResponseDTO) service.getFullByEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um usuário simplificado pelo email
     * @param email O email do usuário a ser buscado
     * @return Um ResponseEntity contendo o usuário simplificado e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RegularUserService#getSimpleByEmail(String), SimpleUserResponseDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Busca usuário simplificado pelo email", description = "Busca um usuário simplificado pelo email e retorna o usuário com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = SimpleUserResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Nome do Usuário",
                        "avatar": "AVATAR_1"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/simple")
    public ResponseEntity<SimpleUserResponseDTO> getSimpleByEmail(
            @PathVariable @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((SimpleUserResponseDTO) service.getSimpleByEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os usuários simplificados
     * @return Um ResponseEntity contendo a lista de usuários simplificados e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RegularUserService#getAllSimple(), SimpleUserResponseDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Lista todos os usuários simplificados", description = "Lista todos os usuários de forma simplificada e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso",
            content = @Content(schema = @Schema(implementation = SimpleUserResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "name": "Nome do Usuário 1",
                            "avatar": "AVATAR_1"
                        },
                        {
                            "id": 2,
                            "name": "Nome do Usuário 2",
                            "avatar": "AVATAR_2"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/simple")
    public ResponseEntity<List<SimpleUserResponseDTO>> getAllSimple() {
        try {
            return new ResponseEntity<>(service.getAllSimple(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os usuários completos
     * @return Um ResponseEntity contendo a lista de usuários completos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RegularUserService#getAllFull(), FullUserResponseDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Lista todos os usuários completos", description = "Lista todos os usuários de forma completa e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso",
            content = @Content(schema = @Schema(implementation = FullUserResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "name": "Nome do Usuário 1",
                            "currentAvatar": "AVATAR_1",
                            "gems": 1000,
                            "friends": [],
                            "projects": [],
                            "articles": [],
                            "punctuation": {
                                "id": 1,
                                "recyclePoints": 500,
                                "position": 1
                            },
                            "league": {
                                "id": 1,
                                "name": "Liga Bronze",
                                "tier": 1
                            }
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/full")
    public ResponseEntity<List<FullUserResponseDTO>> getAllFull() {
        try {
            return new ResponseEntity<>(service.getAllFull(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os usuários com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de usuários e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RegularUserService#getAll(Pageable)
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Lista todos os usuários com paginação", description = "Lista todos os usuários com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<Page<FullUserResponseDTO>> getAll(Pageable pageable) {
        try {
            Page<FullUserResponseDTO> users = service.getAll(pageable).map(FullUserResponseDTO::new);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
