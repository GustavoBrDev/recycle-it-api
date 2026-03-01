package com.ifsc.ctds.stinghen.recycle_it_api.controllers.punctuation;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.punctuation.PointsPunctuationResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.PointsPunctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.User;
import com.ifsc.ctds.stinghen.recycle_it_api.services.punctuation.PointsPunctuationService;
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

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Classe de controle para as pontuações por pontos
 * Implementa todos os métodos públicos disponíveis na PointsPunctuationService
 * @see PointsPunctuationService, PointsPunctuation
 * @version 1.0
 * @since 01/03/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/points-punctuations")
public class PointsPunctuationController {

    /**
     * O serviço de pontuações por pontos que permite criar, atualizar, buscar, listar e deletar pontuações por pontos
     * @see PointsPunctuationService
     */
    private final PointsPunctuationService service;

    /**
     * Método POST para criar uma nova pontuação por pontos para um usuário
     * @param user O usuário dono da pontuação
     * @return Um ResponseEntity contendo o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#create(User)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Cria uma nova pontuação por pontos", description = "Cria uma nova pontuação por pontos para um usuário e retorna o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Pontuação por pontos criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao criar pontuação por pontos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<Void> createPointsPunctuation(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = User.class)),
            example = """
                    {
                        "id": 1,
                        "name": "João Silva",
                        "email": "joao@exemplo.com"
                    }
                    """) User user) {
        try {
            service.create(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de redução de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param reducePoints Os novos pontos de redução
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editReducePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de redução da pontuação", description = "Atualiza os pontos de redução de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de redução atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de redução atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de redução")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/reduce-points")
    public ResponseEntity<FeedbackResponseDTO> updateReducePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "150") @NotNull @Positive Long reducePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editReducePoints(id, reducePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de reciclagem de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param recyclePoints Os novos pontos de reciclagem
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editRecyclePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de reciclagem da pontuação", description = "Atualiza os pontos de reciclagem de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/recycle-points")
    public ResponseEntity<FeedbackResponseDTO> updateRecyclePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "200") @NotNull @Positive Long recyclePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editRecyclePoints(id, recyclePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de reutilização de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param reusePoints Os novos pontos de reutilização
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editReusePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de reutilização da pontuação", description = "Atualiza os pontos de reutilização de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/reuse-points")
    public ResponseEntity<FeedbackResponseDTO> updateReusePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "100") @NotNull @Positive Long reusePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editReusePoints(id, reusePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de conhecimento de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param knowledgePoints Os novos pontos de conhecimento
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editKnowledgePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de conhecimento da pontuação", description = "Atualiza os pontos de conhecimento de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/knowledge-points")
    public ResponseEntity<FeedbackResponseDTO> updateKnowledgePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "50") @NotNull @Positive Long knowledgePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editKnowledgePoints(id, knowledgePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de reciclagem de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementRecyclePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de reciclagem da pontuação", description = "Incrementa os pontos de reciclagem de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/recycle-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementRecyclePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementRecyclePoints(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de reciclagem de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementRecyclePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de reciclagem da pontuação", description = "Decrementa os pontos de reciclagem de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/recycle-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementRecyclePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementRecyclePoints(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de reutilização de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementReusePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de reutilização da pontuação", description = "Incrementa os pontos de reutilização de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/reuse-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementReusePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementReusePoints(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de reutilização de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementReusePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de reutilização da pontuação", description = "Decrementa os pontos de reutilização de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/reuse-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementReusePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementReusePoints(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de conhecimento de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementKnowledgePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de conhecimento da pontuação", description = "Incrementa os pontos de conhecimento de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/knowledge-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementKnowledgePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementKnowledgePoints(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de conhecimento de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementKnowledgePoints(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de conhecimento da pontuação", description = "Decrementa os pontos de conhecimento de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{id}/knowledge-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementKnowledgePoints(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementKnowledgePoints(id, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de redução por ID de usuário
     * @param userId O ID do usuário
     * @param reducePoints Os novos pontos de redução
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editReducePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de redução por ID de usuário", description = "Atualiza os pontos de redução da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de redução atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de redução atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de redução")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/user/{userId}/reduce-points")
    public ResponseEntity<FeedbackResponseDTO> updateReducePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestBody @Parameter(required = true, example = "150") @NotNull @Positive Long reducePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editReducePointsByUserId(userId, reducePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de redução por email de usuário
     * @param email O email do usuário
     * @param reducePoints Os novos pontos de redução
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editReducePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de redução por email de usuário", description = "Atualiza os pontos de redução da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de redução atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de redução atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de redução")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/email/{email}/reduce-points")
    public ResponseEntity<FeedbackResponseDTO> updateReducePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestBody @Parameter(required = true, example = "150") @NotNull @Positive Long reducePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editReducePointsByUserEmail(email, reducePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de reciclagem por ID de usuário
     * @param userId O ID do usuário
     * @param recyclePoints Os novos pontos de reciclagem
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editRecyclePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de reciclagem por ID de usuário", description = "Atualiza os pontos de reciclagem da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/user/{userId}/recycle-points")
    public ResponseEntity<FeedbackResponseDTO> updateRecyclePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestBody @Parameter(required = true, example = "200") @NotNull @Positive Long recyclePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editRecyclePointsByUserId(userId, recyclePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de reciclagem por email de usuário
     * @param email O email do usuário
     * @param recyclePoints Os novos pontos de reciclagem
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editRecyclePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de reciclagem por email de usuário", description = "Atualiza os pontos de reciclagem da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/email/{email}/recycle-points")
    public ResponseEntity<FeedbackResponseDTO> updateRecyclePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestBody @Parameter(required = true, example = "200") @NotNull @Positive Long recyclePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editRecyclePointsByUserEmail(email, recyclePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de reutilização por ID de usuário
     * @param userId O ID do usuário
     * @param reusePoints Os novos pontos de reutilização
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editReusePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de reutilização por ID de usuário", description = "Atualiza os pontos de reutilização da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/user/{userId}/reuse-points")
    public ResponseEntity<FeedbackResponseDTO> updateReusePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestBody @Parameter(required = true, example = "100") @NotNull @Positive Long reusePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editReusePointsByUserId(userId, reusePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de reutilização por email de usuário
     * @param email O email do usuário
     * @param reusePoints Os novos pontos de reutilização
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editReusePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de reutilização por email de usuário", description = "Atualiza os pontos de reutilização da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/email/{email}/reuse-points")
    public ResponseEntity<FeedbackResponseDTO> updateReusePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestBody @Parameter(required = true, example = "100") @NotNull @Positive Long reusePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editReusePointsByUserEmail(email, reusePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de conhecimento por ID de usuário
     * @param userId O ID do usuário
     * @param knowledgePoints Os novos pontos de conhecimento
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editKnowledgePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de conhecimento por ID de usuário", description = "Atualiza os pontos de conhecimento da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/user/{userId}/knowledge-points")
    public ResponseEntity<FeedbackResponseDTO> updateKnowledgePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestBody @Parameter(required = true, example = "50") @NotNull @Positive Long knowledgePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editKnowledgePointsByUserId(userId, knowledgePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar os pontos de conhecimento por email de usuário
     * @param email O email do usuário
     * @param knowledgePoints Os novos pontos de conhecimento
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#editKnowledgePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Atualiza os pontos de conhecimento por email de usuário", description = "Atualiza os pontos de conhecimento da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento atualizados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento atualizados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/email/{email}/knowledge-points")
    public ResponseEntity<FeedbackResponseDTO> updateKnowledgePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestBody @Parameter(required = true, example = "50") @NotNull @Positive Long knowledgePoints) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editKnowledgePointsByUserEmail(email, knowledgePoints), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de redução por ID de usuário
     * @param userId O ID do usuário
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementReducePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de redução por ID de usuário", description = "Incrementa os pontos de redução da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de redução incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de redução incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de redução")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/user/{userId}/reduce-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementReducePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementReducePointsByUserId(userId, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de redução por email de usuário
     * @param email O email do usuário
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementReducePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de redução por email de usuário", description = "Incrementa os pontos de redução da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de redução incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de redução incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de redução")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/email/{email}/reduce-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementReducePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementReducePointsByUserEmail(email, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de redução por ID de usuário
     * @param userId O ID do usuário
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementReducePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de redução por ID de usuário", description = "Decrementa os pontos de redução da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de redução decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de redução decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de redução")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/user/{userId}/reduce-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementReducePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementReducePointsByUserId(userId, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de redução por email de usuário
     * @param email O email do usuário
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementReducePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de redução por email de usuário", description = "Decrementa os pontos de redução da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de redução decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de redução decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de redução")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/email/{email}/reduce-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementReducePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementReducePointsByUserEmail(email, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de reciclagem por ID de usuário
     * @param userId O ID do usuário
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementRecyclePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de reciclagem por ID de usuário", description = "Incrementa os pontos de reciclagem da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/user/{userId}/recycle-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementRecyclePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementRecyclePointsByUserId(userId, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de reciclagem por email de usuário
     * @param email O email do usuário
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementRecyclePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de reciclagem por email de usuário", description = "Incrementa os pontos de reciclagem da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/email/{email}/recycle-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementRecyclePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementRecyclePointsByUserEmail(email, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de reciclagem por ID de usuário
     * @param userId O ID do usuário
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementRecyclePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de reciclagem por ID de usuário", description = "Decrementa os pontos de reciclagem da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/user/{userId}/recycle-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementRecyclePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementRecyclePointsByUserId(userId, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de reciclagem por email de usuário
     * @param email O email do usuário
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementRecyclePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de reciclagem por email de usuário", description = "Decrementa os pontos de reciclagem da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reciclagem decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reciclagem decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de reciclagem")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/email/{email}/recycle-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementRecyclePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementRecyclePointsByUserEmail(email, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de reutilização por ID de usuário
     * @param userId O ID do usuário
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementReusePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de reutilização por ID de usuário", description = "Incrementa os pontos de reutilização da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/user/{userId}/reuse-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementReusePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementReusePointsByUserId(userId, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de reutilização por email de usuário
     * @param email O email do usuário
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementReusePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de reutilização por email de usuário", description = "Incrementa os pontos de reutilização da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/email/{email}/reuse-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementReusePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementReusePointsByUserEmail(email, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de reutilização por ID de usuário
     * @param userId O ID do usuário
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementReusePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de reutilização por ID de usuário", description = "Decrementa os pontos de reutilização da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/user/{userId}/reuse-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementReusePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementReusePointsByUserId(userId, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de reutilização por email de usuário
     * @param email O email do usuário
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementReusePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de reutilização por email de usuário", description = "Decrementa os pontos de reutilização da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de reutilização decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de reutilização decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de reutilização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/email/{email}/reuse-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementReusePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementReusePointsByUserEmail(email, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de conhecimento por ID de usuário
     * @param userId O ID do usuário
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementKnowledgePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de conhecimento por ID de usuário", description = "Incrementa os pontos de conhecimento da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/user/{userId}/knowledge-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementKnowledgePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementKnowledgePointsByUserId(userId, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para incrementar os pontos de conhecimento por email de usuário
     * @param email O email do usuário
     * @param amount O valor a ser incrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#incrementKnowledgePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Incrementa os pontos de conhecimento por email de usuário", description = "Incrementa os pontos de conhecimento da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento incrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento incrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao incrementar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/email/{email}/knowledge-points/increment")
    public ResponseEntity<FeedbackResponseDTO> incrementKnowledgePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestParam @Parameter(required = true, example = "10") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.incrementKnowledgePointsByUserEmail(email, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de conhecimento por ID de usuário
     * @param userId O ID do usuário
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementKnowledgePointsByUserId(Long, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de conhecimento por ID de usuário", description = "Decrementa os pontos de conhecimento da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/user/{userId}/knowledge-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementKnowledgePointsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementKnowledgePointsByUserId(userId, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para decrementar os pontos de conhecimento por email de usuário
     * @param email O email do usuário
     * @param amount O valor a ser decrementado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PointsPunctuationService#decrementKnowledgePointsByUserEmail(String, Long)
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Decrementa os pontos de conhecimento por email de usuário", description = "Decrementa os pontos de conhecimento da pontuação mais recente de um usuário e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontos de conhecimento decrementados com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontos de conhecimento decrementados",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao decrementar pontos de conhecimento")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/email/{email}/knowledge-points/decrement")
    public ResponseEntity<FeedbackResponseDTO> decrementKnowledgePointsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email,
            @RequestParam @Parameter(required = true, example = "5") @NotNull @Positive Long amount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.decrementKnowledgePointsByUserEmail(email, amount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar pontuações por pontos como DTO por ID de usuário
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getResponseByUserId(Long), List<PointsPunctuationResponseDTO>
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuações por pontos como DTO por ID de usuário", description = "Busca pontuações por pontos por ID de usuário e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações encontradas com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuationResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "reducePoints": 150,
                            "recyclePoints": 200,
                            "reusePoints": 100,
                            "knowledgePoints": 50,
                            "totalPoints": 500,
                            "lastUpdated": "2026-03-01T10:00:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/dto")
    public ResponseEntity<List<PointsPunctuationResponseDTO>> getPointsPunctuationsDtoByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getResponseByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar pontuações por pontos como DTO por email de usuário
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getResponseByUserEmail(String), List<PointsPunctuationResponseDTO>
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuações por pontos como DTO por email de usuário", description = "Busca pontuações por pontos por email de usuário e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações encontradas com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuationResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "reducePoints": 150,
                            "recyclePoints": 200,
                            "reusePoints": 100,
                            "knowledgePoints": 50,
                            "totalPoints": 500,
                            "lastUpdated": "2026-03-01T10:00:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/dto")
    public ResponseEntity<List<PointsPunctuationResponseDTO>> getPointsPunctuationsDtoByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getResponseByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a pontuação mais recente por ID de usuário
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getMostRecentObjectByUserId(Long), PointsPunctuation
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuação mais recente por ID de usuário", description = "Busca a pontuação mais recente por ID de usuário e retorna a pontuação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuation.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "reducePoints": 150,
                        "recyclePoints": 200,
                        "reusePoints": 100,
                        "knowledgePoints": 50,
                        "totalPoints": 500,
                        "lastUpdated": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/most-recent/object")
    public ResponseEntity<PointsPunctuation> getMostRecentPointsPunctuationObjectByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getMostRecentObjectByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a pontuação mais recente por email de usuário
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getMostRecentObjectByUserEmail(String), PointsPunctuation
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuação mais recente por email de usuário", description = "Busca a pontuação mais recente por email de usuário e retorna a pontuação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuation.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "reducePoints": 150,
                        "recyclePoints": 200,
                        "reusePoints": 100,
                        "knowledgePoints": 50,
                        "totalPoints": 500,
                        "lastUpdated": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/most-recent/object")
    public ResponseEntity<PointsPunctuation> getMostRecentPointsPunctuationObjectByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getMostRecentObjectByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

    /**
     * Método GET para buscar uma pontuação por pontos pelo ID
     * @param id O ID da pontuação a ser buscada
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getObjectById(Long), PointsPunctuation
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuação por pontos pelo ID", description = "Busca uma pontuação por pontos pelo ID e retorna a pontuação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuation.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "reducePoints": 150,
                        "recyclePoints": 200,
                        "reusePoints": 100,
                        "knowledgePoints": 50,
                        "totalPoints": 500,
                        "lastUpdated": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<PointsPunctuation> getPointsPunctuationById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as pontuações por pontos
     * @return Um ResponseEntity contendo a lista de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getAll(), List<PointsPunctuation>
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Lista todas as pontuações por pontos", description = "Lista todas as pontuações por pontos e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações listadas com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuation.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "reducePoints": 150,
                            "recyclePoints": 200,
                            "reusePoints": 100,
                            "knowledgePoints": 50,
                            "totalPoints": 500,
                            "lastUpdated": "2026-03-01T10:00:00"
                        },
                        {
                            "id": 2,
                            "reducePoints": 100,
                            "recyclePoints": 150,
                            "reusePoints": 80,
                            "knowledgePoints": 30,
                            "totalPoints": 360,
                            "lastUpdated": "2026-03-02T15:30:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<PointsPunctuation>> getAllPointsPunctuations() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as pontuações por pontos com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getAll(Pageable), Page<PointsPunctuation>
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Lista todas as pontuações por pontos com paginação", description = "Lista todas as pontuações por pontos com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<PointsPunctuation>> getAllPointsPunctuationsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar pontuações por pontos por ID de usuário
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a lista de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getObjectByUserId(Long), List<PointsPunctuation>
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuações por pontos por ID de usuário", description = "Busca pontuações por pontos por ID de usuário e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações encontradas com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuation.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "reducePoints": 150,
                            "recyclePoints": 200,
                            "reusePoints": 100,
                            "knowledgePoints": 50,
                            "totalPoints": 500,
                            "lastUpdated": "2026-03-01T10:00:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PointsPunctuation>> getPointsPunctuationsByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getObjectByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar pontuações por pontos por email de usuário
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a lista de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getObjectByUserEmail(String), List<PointsPunctuation>
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuações por pontos por email de usuário", description = "Busca pontuações por pontos por email de usuário e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações encontradas com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuation.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "reducePoints": 150,
                            "recyclePoints": 200,
                            "reusePoints": 100,
                            "knowledgePoints": 50,
                            "totalPoints": 500,
                            "lastUpdated": "2026-03-01T10:00:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}")
    public ResponseEntity<List<PointsPunctuation>> getPointsPunctuationsByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getObjectByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a pontuação mais recente por ID de usuário como DTO
     * @param userId O ID do usuário
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getMostRecentResponseByUserId(Long), PointsPunctuationResponseDTO
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuação mais recente por ID de usuário como DTO", description = "Busca a pontuação mais recente por ID de usuário e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuationResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "reducePoints": 150,
                        "recyclePoints": 200,
                        "reusePoints": 100,
                        "knowledgePoints": 50,
                        "totalPoints": 500,
                        "lastUpdated": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/user/{userId}/most-recent")
    public ResponseEntity<PointsPunctuationResponseDTO> getMostRecentPointsPunctuationByUserId(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long userId) {
        try {
            return new ResponseEntity<>(service.getMostRecentResponseByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar a pontuação mais recente por email de usuário como DTO
     * @param email O email do usuário
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PointsPunctuationService#getMostRecentResponseByUserEmail(String), PointsPunctuationResponseDTO
     */
    @Tag(name = "Pontuações por Pontos", description = "Recurso para gerenciamento de pontuações por pontos")
    @Operation(summary = "Busca pontuação mais recente por email de usuário como DTO", description = "Busca a pontuação mais recente por email de usuário e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = PointsPunctuationResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "reducePoints": 150,
                        "recyclePoints": 200,
                        "reusePoints": 100,
                        "knowledgePoints": 50,
                        "totalPoints": 500,
                        "lastUpdated": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o usuário")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/email/{email}/most-recent")
    public ResponseEntity<PointsPunctuationResponseDTO> getMostRecentPointsPunctuationByUserEmail(
            @PathVariable @Parameter(required = true, example = "joao@exemplo.com") String email) {
        try {
            return new ResponseEntity<>(service.getMostRecentResponseByUserEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
