package com.ifsc.ctds.stinghen.recycle_it_api.controllers.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.services.project.ProjectMaterialService;
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
 * Classe de controle para os materiais de projeto
 * Implementa todos os métodos públicos disponíveis na ProjectMaterialService
 * @see ProjectMaterialService, ProjectMaterial
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/project-materials")
public class ProjectMaterialController {

    /**
     * O serviço de materiais de projeto que permite atualizar, buscar, listar e deletar materiais de projeto
     * @see ProjectMaterialService
     */
    private final ProjectMaterialService service;

    /**
     * Método PUT para atualizar um material de projeto existente
     * @param id O ID do material a ser atualizado
     * @param projectMaterial O material atualizado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectMaterialService#update(Long, ProjectMaterial)
     */
    @Tag(name = "Materiais de Projeto", description = "Recurso para gerenciamento de materiais de projeto")
    @Operation(summary = "Atualiza um material de projeto", description = "Atualiza um material de projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Material de projeto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Material de projeto atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar material de projeto")
    @ApiResponse(responseCode = "404", description = "Material de projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateProjectMaterial(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = ProjectMaterial.class)),
            example = """
                    {
                        "quantity": 15
                    }
                    """) @Valid ProjectMaterial projectMaterial) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, projectMaterial), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a quantidade de um material de projeto
     * @param id O ID do material a ser atualizado
     * @param quantity A nova quantidade do material
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectMaterialService#editQuantity(Long, Long)
     */
    @Tag(name = "Materiais de Projeto", description = "Recurso para gerenciamento de materiais de projeto")
    @Operation(summary = "Atualiza a quantidade do material de projeto", description = "Atualiza a quantidade de um material de projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Quantidade do material atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Quantidade do material atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar quantidade do material")
    @ApiResponse(responseCode = "404", description = "Material de projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<FeedbackResponseDTO> updateProjectMaterialQuantity(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "20") @NotNull @Positive Long quantity) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editQuantity(id, quantity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um material de projeto pelo ID
     * @param id O ID do material a ser buscado
     * @return Um ResponseEntity contendo o material e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectMaterialService#getObjectById(Long), ProjectMaterial
     */
    @Tag(name = "Materiais de Projeto", description = "Recurso para gerenciamento de materiais de projeto")
    @Operation(summary = "Busca material de projeto pelo ID", description = "Busca um material de projeto pelo ID e retorna o material com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Material de projeto encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectMaterial.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "quantity": 10,
                        "materialKind": "RECYCLED"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Material de projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectMaterial> getProjectMaterialById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os materiais de projeto
     * @return Um ResponseEntity contendo a lista de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectMaterialService#getAll(), List<ProjectMaterial>
     */
    @Tag(name = "Materiais de Projeto", description = "Recurso para gerenciamento de materiais de projeto")
    @Operation(summary = "Lista todos os materiais de projeto", description = "Lista todos os materiais de projeto e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais de projeto listados com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectMaterial.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "quantity": 10,
                            "materialKind": "RECYCLED"
                        },
                        {
                            "id": 2,
                            "quantity": 5,
                            "materialKind": "OTHER"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum material de projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<ProjectMaterial>> getAllProjectMaterials() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os materiais de projeto com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectMaterialService#getAll(Pageable), Page<ProjectMaterial>
     */
    @Tag(name = "Materiais de Projeto", description = "Recurso para gerenciamento de materiais de projeto")
    @Operation(summary = "Lista todos os materiais de projeto com paginação", description = "Lista todos os materiais de projeto com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais de projeto listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum material de projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<ProjectMaterial>> getAllProjectMaterialsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar materiais de projeto por quantidade mínima
     * @param minQuantity A quantidade mínima
     * @return Um ResponseEntity contendo a lista de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectMaterialService#getByMinQuantity(Long), List<ProjectMaterial>
     */
    @Tag(name = "Materiais de Projeto", description = "Recurso para gerenciamento de materiais de projeto")
    @Operation(summary = "Lista materiais de projeto por quantidade mínima", description = "Lista materiais de projeto por quantidade mínima e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais de projeto listados com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectMaterial.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "quantity": 15,
                            "materialKind": "RECYCLED"
                        },
                        {
                            "id": 3,
                            "quantity": 20,
                            "materialKind": "OTHER"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum material de projeto encontrado para a quantidade mínima")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/min-quantity/{minQuantity}")
    public ResponseEntity<List<ProjectMaterial>> getProjectMaterialsByMinQuantity(
            @PathVariable @Parameter(required = true, example = "10") @NotNull @Positive Long minQuantity) {
        try {
            return new ResponseEntity<>(service.getByMinQuantity(minQuantity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar materiais de projeto por quantidade mínima com paginação
     * @param minQuantity A quantidade mínima
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectMaterialService#getByMinQuantity(Long, Pageable), Page<ProjectMaterial>
     */
    @Tag(name = "Materiais de Projeto", description = "Recurso para gerenciamento de materiais de projeto")
    @Operation(summary = "Lista materiais de projeto por quantidade mínima com paginação", description = "Lista materiais de projeto por quantidade mínima com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais de projeto listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum material de projeto encontrado para a quantidade mínima")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/min-quantity/{minQuantity}/paged")
    public ResponseEntity<Page<ProjectMaterial>> getProjectMaterialsByMinQuantityPaged(
            @PathVariable @Parameter(required = true, example = "10") @NotNull @Positive Long minQuantity,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByMinQuantity(minQuantity, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um material de projeto
     * @param id O ID do material a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectMaterialService#deleteById(Long)
     */
    @Tag(name = "Materiais de Projeto", description = "Recurso para gerenciamento de materiais de projeto")
    @Operation(summary = "Deleta um material de projeto", description = "Deleta um material de projeto e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Material de projeto deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Material de projeto deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar material de projeto")
    @ApiResponse(responseCode = "404", description = "Material de projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteProjectMaterial(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
