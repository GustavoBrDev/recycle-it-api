package com.ifsc.ctds.stinghen.recycle_it_api.controllers.league;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.league.LeagueRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.FullLeagueResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.league.SimpleLeagueResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.league.League;
import com.ifsc.ctds.stinghen.recycle_it_api.services.league.LeagueService;
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
 * Classe de controle para as ligas
 * Implementa todos os métodos públicos disponíveis na LeagueService
 * @see LeagueService, League
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/leagues")
public class LeagueController {

    /**
     * O serviço de ligas que permite criar, atualizar, buscar, listar e deletar ligas
     * @see LeagueService
     */
    private final LeagueService service;

    /**
     * Método POST para criar uma nova liga
     * @param dto A {@link LeagueRequestDTO} contendo os dados da liga
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#create(LeagueRequestDTO), LeagueRequestDTO
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Cria uma nova liga", description = "Cria uma nova liga e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Liga criada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Liga criada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar liga")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createLeague(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = LeagueRequestDTO.class)),
            example = """
                    {
                        "name": "Liga Bronze",
                        "tier": 1,
                        "membersCount": 100,
                        "promotedCount": 10,
                        "relegatedCount": 5
                    }
                    """) @Valid LeagueRequestDTO dto) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar uma liga existente
     * @param id O ID da liga a ser atualizada
     * @param league A {@link League} contendo os dados atualizados
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#update(Long, League), League
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Atualiza uma liga", description = "Atualiza uma liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Liga atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Liga atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar liga")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateLeague(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = League.class)),
            example = """
                    {
                        "name": "Liga Prata",
                        "tier": 2,
                        "membersCount": 150,
                        "promotedCount": 15,
                        "relegatedCount": 8,
                        "promotionEnabled": true,
                        "relegationEnabled": true
                    }
                    """) @Valid League league) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, league), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o nome de uma liga
     * @param id O ID da liga a ser atualizada
     * @param name O novo nome da liga
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#editName(Long, String)
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Atualiza o nome da liga", description = "Atualiza o nome de uma liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Nome da liga atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Nome da liga atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar nome da liga")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/name")
    public ResponseEntity<FeedbackResponseDTO> updateLeagueName(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Liga Ouro") String name) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editName(id, name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o tier de uma liga
     * @param id O ID da liga a ser atualizada
     * @param tier O novo tier da liga
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#editTier(Long, int)
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Atualiza o tier da liga", description = "Atualiza o tier de uma liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Tier da liga atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Tier da liga atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar tier da liga")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/tier")
    public ResponseEntity<FeedbackResponseDTO> updateLeagueTier(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "3") int tier) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editTier(id, tier), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o número de membros de uma liga
     * @param id O ID da liga a ser atualizada
     * @param membersCount O novo número de membros
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#editMembersCount(Long, int)
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Atualiza o número de membros da liga", description = "Atualiza o número de membros de uma liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Número de membros atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Número de membros atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar número de membros")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/members-count")
    public ResponseEntity<FeedbackResponseDTO> updateLeagueMembersCount(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "200") int membersCount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editMembersCount(id, membersCount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o número de promovidos de uma liga
     * @param id O ID da liga a ser atualizada
     * @param promotedCount O novo número de promovidos
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#editPromotedCount(Long, int)
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Atualiza o número de promovidos da liga", description = "Atualiza o número de promovidos de uma liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Número de promovidos atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Número de promovidos atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar número de promovidos")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/promoted-count")
    public ResponseEntity<FeedbackResponseDTO> updateLeaguePromotedCount(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "25") int promotedCount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editPromotedCount(id, promotedCount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o número de rebaixados de uma liga
     * @param id O ID da liga a ser atualizada
     * @param relegatedCount O novo número de rebaixados
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#editRelegatedCount(Long, int)
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Atualiza o número de rebaixados da liga", description = "Atualiza o número de rebaixados de uma liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Número de rebaixados atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Número de rebaixados atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar número de rebaixados")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/relegated-count")
    public ResponseEntity<FeedbackResponseDTO> updateLeagueRelegatedCount(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "3") int relegatedCount) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editRelegatedCount(id, relegatedCount), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para habilitar ou desabilitar promoção de uma liga
     * @param id O ID da liga a ser atualizada
     * @param promotionEnabled Indica se a promoção está habilitada
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#editPromotionEnabled(Long, boolean)
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Atualiza status de promoção da liga", description = "Atualiza o status de promoção de uma liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Status de promoção atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Status de promoção atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar status de promoção")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/promotion-enabled")
    public ResponseEntity<FeedbackResponseDTO> updateLeaguePromotionEnabled(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "true") boolean promotionEnabled) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editPromotionEnabled(id, promotionEnabled), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para habilitar ou desabilitar rebaixamento de uma liga
     * @param id O ID da liga a ser atualizada
     * @param relegationEnabled Indica se o rebaixamento está habilitado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#editRelegationEnabled(Long, boolean)
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Atualiza status de rebaixamento da liga", description = "Atualiza o status de rebaixamento de uma liga existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Status de rebaixamento atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Status de rebaixamento atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar status de rebaixamento")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/relegation-enabled")
    public ResponseEntity<FeedbackResponseDTO> updateLeagueRelegationEnabled(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "false") boolean relegationEnabled) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editRelegationEnabled(id, relegationEnabled), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar uma liga pelo ID
     * @param id O ID da liga a ser buscada
     * @return Um ResponseEntity contendo a liga e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getObjectById(Long), League
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Busca liga pelo ID", description = "Busca uma liga pelo ID e retorna a liga com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Liga encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = League.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Liga Bronze",
                        "tier": 1,
                        "membersCount": 100,
                        "promotedCount": 10,
                        "relegatedCount": 5,
                        "promotionEnabled": true,
                        "relegationEnabled": true
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<League> getLeagueById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma liga completa como DTO pelo ID
     * @param id O ID da liga a ser buscada
     * @return Um ResponseEntity contendo a liga e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getFullById(Long), FullLeagueResponseDTO
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Busca liga completa como DTO pelo ID", description = "Busca uma liga pelo ID e retorna como DTO completo com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Liga encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = FullLeagueResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "name": "Liga Bronze",
                        "tier": 1,
                        "promotedCount": 10,
                        "relegatedCount": 5,
                        "promotionEnabled": true,
                        "relegationEnabled": true
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/full")
    public ResponseEntity<FullLeagueResponseDTO> getFullLeagueById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FullLeagueResponseDTO) service.getFullById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma liga simplificada como DTO pelo ID
     * @param id O ID da liga a ser buscada
     * @return Um ResponseEntity contendo a liga e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getSimpleById(Long), SimpleLeagueResponseDTO
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Busca liga simplificada como DTO pelo ID", description = "Busca uma liga pelo ID e retorna como DTO simplificado com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Liga encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = SimpleLeagueResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "name": "Liga Bronze",
                        "tier": 1
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/simple")
    public ResponseEntity<SimpleLeagueResponseDTO> getSimpleLeagueById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((SimpleLeagueResponseDTO) service.getSimpleById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma liga pelo tier
     * @param tier O tier da liga a ser buscada
     * @return Um ResponseEntity contendo a liga e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getObjectByTier(int), League
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Busca liga pelo tier", description = "Busca uma liga pelo tier e retorna a liga com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Liga encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = League.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Liga Bronze",
                        "tier": 1,
                        "membersCount": 100,
                        "promotedCount": 10,
                        "relegatedCount": 5,
                        "promotionEnabled": true,
                        "relegationEnabled": true
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/tier/{tier}")
    public ResponseEntity<League> getLeagueByTier(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive int tier) {
        try {
            return new ResponseEntity<>(service.getObjectByTier(tier), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma liga completa como DTO pelo tier
     * @param tier O tier da liga a ser buscada
     * @return Um ResponseEntity contendo a liga e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getFullByTier(int), FullLeagueResponseDTO
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Busca liga completa como DTO pelo tier", description = "Busca uma liga pelo tier e retorna como DTO completo com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Liga encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = FullLeagueResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "name": "Liga Bronze",
                        "tier": 1,
                        "promotedCount": 10,
                        "relegatedCount": 5,
                        "promotionEnabled": true,
                        "relegationEnabled": true
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/tier/{tier}/full")
    public ResponseEntity<FullLeagueResponseDTO> getFullLeagueByTier(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive int tier) {
        try {
            return new ResponseEntity<>((FullLeagueResponseDTO) service.getFullByTier(tier), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar uma liga simplificada como DTO pelo tier
     * @param tier O tier da liga a ser buscada
     * @return Um ResponseEntity contendo a liga e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getSimpleByTier(int), SimpleLeagueResponseDTO
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Busca liga simplificada como DTO pelo tier", description = "Busca uma liga pelo tier e retorna como DTO simplificado com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Liga encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = SimpleLeagueResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "name": "Liga Bronze",
                        "tier": 1
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/tier/{tier}/simple")
    public ResponseEntity<SimpleLeagueResponseDTO> getSimpleLeagueByTier(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive int tier) {
        try {
            return new ResponseEntity<>((SimpleLeagueResponseDTO) service.getSimpleByTier(tier), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as ligas
     * @return Um ResponseEntity contendo a lista de ligas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getAll(), List<League>
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Lista todas as ligas", description = "Lista todas as ligas e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Ligas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = League.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "name": "Liga Bronze",
                            "tier": 1,
                            "membersCount": 100,
                            "promotedCount": 10,
                            "relegatedCount": 5,
                            "promotionEnabled": true,
                            "relegationEnabled": true
                        },
                        {
                            "id": 2,
                            "name": "Liga Prata",
                            "tier": 2,
                            "membersCount": 150,
                            "promotedCount": 15,
                            "relegatedCount": 8,
                            "promotionEnabled": true,
                            "relegationEnabled": true
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<League>> getAllLeagues() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as ligas como DTO simplificado
     * @return Um ResponseEntity contendo a lista de ligas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getAllSimple(), List<SimpleLeagueResponseDTO>
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Lista todas as ligas como DTO simplificado", description = "Lista todas as ligas como DTO simplificado e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Ligas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = SimpleLeagueResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "name": "Liga Bronze",
                            "tier": 1
                        },
                        {
                            "name": "Liga Prata",
                            "tier": 2
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/simple")
    public ResponseEntity<List<SimpleLeagueResponseDTO>> getAllSimpleLeagues() {
        try {
            return new ResponseEntity<>(service.getAllSimple(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as ligas como DTO completo
     * @return Um ResponseEntity contendo a lista de ligas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getAllFull(), List<FullLeagueResponseDTO>
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Lista todas as ligas como DTO completo", description = "Lista todas as ligas como DTO completo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Ligas listadas com sucesso",
            content = @Content(schema = @Schema(implementation = FullLeagueResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "name": "Liga Bronze",
                            "tier": 1,
                            "promotedCount": 10,
                            "relegatedCount": 5,
                            "promotionEnabled": true,
                            "relegationEnabled": true
                        },
                        {
                            "name": "Liga Prata",
                            "tier": 2,
                            "promotedCount": 15,
                            "relegatedCount": 8,
                            "promotionEnabled": true,
                            "relegationEnabled": true
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhuma liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/full")
    public ResponseEntity<List<FullLeagueResponseDTO>> getAllFullLeagues() {
        try {
            return new ResponseEntity<>(service.getAllFull(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as ligas com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de ligas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getAll(Pageable), Page<League>
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Lista todas as ligas com paginação", description = "Lista todas as ligas com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Ligas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<League>> getAllLeaguesPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as ligas como DTO simplificado com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de ligas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getAllSimple(Pageable), Page<SimpleLeagueResponseDTO>
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Lista todas as ligas como DTO simplificado com paginação", description = "Lista todas as ligas como DTO simplificado com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Ligas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/simple/paged")
    public ResponseEntity<Page<SimpleLeagueResponseDTO>> getAllSimpleLeaguesPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllSimple(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todas as ligas como DTO completo com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de ligas e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see LeagueService#getAllFull(Pageable), Page<FullLeagueResponseDTO>
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Lista todas as ligas como DTO completo com paginação", description = "Lista todas as ligas como DTO completo com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Ligas listadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma liga encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/full/paged")
    public ResponseEntity<Page<FullLeagueResponseDTO>> getAllFullLeaguesPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllFull(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar uma liga
     * @param id O ID da liga a ser deletada
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see LeagueService#deleteById(Long)
     */
    @Tag(name = "Ligas", description = "Recurso para gerenciamento de ligas")
    @Operation(summary = "Deleta uma liga", description = "Deleta uma liga e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Liga deletada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Liga deletada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar liga")
    @ApiResponse(responseCode = "404", description = "Liga não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteLeague(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
