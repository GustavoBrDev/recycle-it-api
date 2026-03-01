package com.ifsc.ctds.stinghen.recycle_it_api.controllers.punctuation;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.punctuation.Punctuation;
import com.ifsc.ctds.stinghen.recycle_it_api.services.punctuation.PunctuationService;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de controle para as pontuações
 * Implementa todos os métodos públicos disponíveis na PunctuationService
 * @see PunctuationService, Punctuation
 * @version 1.0
 * @since 01/03/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/punctuations")
public class PunctuationController {

    /**
     * O serviço de pontuações que permite atualizar, buscar, listar e deletar pontuações
     * @see PunctuationService
     */
    private final PunctuationService service;

    /**
     * Método PATCH para atualizar a data da última atualização de uma pontuação
     * @param id O ID da pontuação a ser atualizada
     * @param lastUpdated A nova data da última atualização
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PunctuationService#editLastUpdated(Long, LocalDateTime)
     */
    @Tag(name = "Pontuações", description = "Recurso para gerenciamento de pontuações")
    @Operation(summary = "Atualiza a data da última atualização da pontuação", description = "Atualiza a data da última atualização de uma pontuação existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Data da última atualização atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Data da última atualização atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar data da última atualização")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/last-updated")
    public ResponseEntity<FeedbackResponseDTO> updatePunctuationLastUpdated(
            @PathVariable @Parameter(required = true, example = "1") Long id,
            @RequestBody @Parameter(required = true, example = "2026-03-01T10:00:00") LocalDateTime lastUpdated) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editLastUpdated(id, lastUpdated), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar uma pontuação pelo ID
     * @param id O ID da pontuação a ser buscada
     * @return Um ResponseEntity contendo a pontuação e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PunctuationService#getObjectById(Long), Punctuation
     */
    @Tag(name = "Pontuações", description = "Recurso para gerenciamento de pontuações")
    @Operation(summary = "Busca pontuação pelo ID", description = "Busca uma pontuação pelo ID e retorna a pontuação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = Punctuation.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "lastUpdated": "2026-03-01T10:00:00"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<Punctuation> getPunctuationById(
            @PathVariable @Parameter(required = true, example = "1") Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as pontuações
     * @return Um ResponseEntity contendo a lista de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PunctuationService#getAll(), List<Punctuation>
     */
    @Tag(name = "Pontuações", description = "Recurso para gerenciamento de pontuações")
    @Operation(summary = "Lista todas as pontuações", description = "Lista todas as pontuações e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Punctuation.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "lastUpdated": "2026-03-01T10:00:00"
                        },
                        {
                            "id": 2,
                            "lastUpdated": "2026-03-02T15:30:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<Punctuation>> getAllPunctuations() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as pontuações com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PunctuationService#getAll(Pageable), Page<Punctuation>
     */
    @Tag(name = "Pontuações", description = "Recurso para gerenciamento de pontuações")
    @Operation(summary = "Lista todas as pontuações com paginação", description = "Lista todas as pontuações com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<Punctuation>> getAllPunctuationsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar pontuações por intervalo de data da última atualização
     * @param startDate A data inicial do intervalo
     * @param endDate A data final do intervalo
     * @return Um ResponseEntity contendo a lista de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PunctuationService#getByLastUpdatedRange(LocalDateTime, LocalDateTime), List<Punctuation>
     */
    @Tag(name = "Pontuações", description = "Recurso para gerenciamento de pontuações")
    @Operation(summary = "Lista pontuações por intervalo de data da última atualização", description = "Lista pontuações por intervalo de data da última atualização e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações listadas com sucesso",
            content = @Content(schema = @Schema(implementation = Punctuation.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "lastUpdated": "2026-03-01T10:00:00"
                        },
                        {
                            "id": 2,
                            "lastUpdated": "2026-03-01T15:30:00"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o intervalo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/date-range")
    public ResponseEntity<List<Punctuation>> getPunctuationsByDateRange(
            @RequestParam @Parameter(required = true, example = "2026-03-01T00:00:00") LocalDateTime startDate,
            @RequestParam @Parameter(required = true, example = "2026-03-01T23:59:59") LocalDateTime endDate) {
        try {
            return new ResponseEntity<>(service.getByLastUpdatedRange(startDate, endDate), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar pontuações por intervalo de data da última atualização com paginação
     * @param startDate A data inicial do intervalo
     * @param endDate A data final do intervalo
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de pontuações e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PunctuationService#getByLastUpdatedRange(LocalDateTime, LocalDateTime, Pageable), Page<Punctuation>
     */
    @Tag(name = "Pontuações", description = "Recurso para gerenciamento de pontuações")
    @Operation(summary = "Lista pontuações por intervalo de data da última atualização com paginação", description = "Lista pontuações por intervalo de data da última atualização com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuações listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma pontuação encontrada para o intervalo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/date-range/paged")
    public ResponseEntity<Page<Punctuation>> getPunctuationsByDateRangePaged(
            @RequestParam @Parameter(required = true, example = "2026-03-01T00:00:00") LocalDateTime startDate,
            @RequestParam @Parameter(required = true, example = "2026-03-01T23:59:59") LocalDateTime endDate,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByLastUpdatedRange(startDate, endDate, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar uma pontuação
     * @param id O ID da pontuação a ser deletada
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PunctuationService#deleteById(Long)
     */
    @Tag(name = "Pontuações", description = "Recurso para gerenciamento de pontuações")
    @Operation(summary = "Deleta uma pontuação", description = "Deleta uma pontuação e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Pontuação deletada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Pontuação deletada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar pontuação")
    @ApiResponse(responseCode = "404", description = "Pontuação não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deletePunctuation(
            @PathVariable @Parameter(required = true, example = "1") Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
