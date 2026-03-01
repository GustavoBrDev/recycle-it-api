package com.ifsc.ctds.stinghen.recycle_it_api.controllers.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Materials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.RecycledMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.services.project.RecycledMaterialService;
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

import java.util.List;

/**
 * Classe de controle para os materiais reciclados
 * Implementa todos os métodos públicos disponíveis na RecycledMaterialService
 * @see RecycledMaterialService, RecycledMaterial
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/recycled-materials")
public class RecycledMaterialController {

    /**
     * O serviço de materiais reciclados que permite atualizar, buscar, listar e deletar materiais reciclados
     * @see RecycledMaterialService
     */
    private final RecycledMaterialService service;

    /**
     * Método PATCH para atualizar o tipo de um material reciclado
     * @param id O ID do material reciclado a ser atualizado
     * @param type O novo tipo do material reciclado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RecycledMaterialService#editType(Long, Materials)
     */
    @Tag(name = "Materiais Reciclados", description = "Recurso para gerenciamento de materiais reciclados")
    @Operation(summary = "Atualiza o tipo do material reciclado", description = "Atualiza o tipo de um material reciclado existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Tipo do material reciclado atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Tipo do material reciclado atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar tipo do material reciclado")
    @ApiResponse(responseCode = "404", description = "Material reciclado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/type")
    public ResponseEntity<FeedbackResponseDTO> updateRecycledMaterialType(
            @PathVariable @Parameter(required = true, example = "1") Long id,
            @RequestBody @Parameter(required = true, example = "PLASTIC") Materials type) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editType(id, type), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um material reciclado pelo ID
     * @param id O ID do material reciclado a ser buscado
     * @return Um ResponseEntity contendo o material e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycledMaterialService#getObjectById(Long), RecycledMaterial
     */
    @Tag(name = "Materiais Reciclados", description = "Recurso para gerenciamento de materiais reciclados")
    @Operation(summary = "Busca material reciclado pelo ID", description = "Busca um material reciclado pelo ID e retorna o material com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Material reciclado encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = RecycledMaterial.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "type": "PLASTIC",
                        "quantity": 10
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Material reciclado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<RecycledMaterial> getRecycledMaterialById(
            @PathVariable @Parameter(required = true, example = "1") Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os materiais reciclados
     * @return Um ResponseEntity contendo a lista de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycledMaterialService#getAll(), List<RecycledMaterial>
     */
    @Tag(name = "Materiais Reciclados", description = "Recurso para gerenciamento de materiais reciclados")
    @Operation(summary = "Lista todos os materiais reciclados", description = "Lista todos os materiais reciclados e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais reciclados listados com sucesso",
            content = @Content(schema = @Schema(implementation = RecycledMaterial.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "type": "PLASTIC",
                            "quantity": 10
                        },
                        {
                            "id": 2,
                            "type": "PAPER",
                            "quantity": 5
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum material reciclado encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<RecycledMaterial>> getAllRecycledMaterials() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os materiais reciclados com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycledMaterialService#getAll(Pageable), Page<RecycledMaterial>
     */
    @Tag(name = "Materiais Reciclados", description = "Recurso para gerenciamento de materiais reciclados")
    @Operation(summary = "Lista todos os materiais reciclados com paginação", description = "Lista todos os materiais reciclados com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais reciclados listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum material reciclado encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<RecycledMaterial>> getAllRecycledMaterialsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar materiais reciclados por tipo
     * @param type O tipo do material reciclado
     * @return Um ResponseEntity contendo a lista de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycledMaterialService#getByType(Materials), List<RecycledMaterial>
     */
    @Tag(name = "Materiais Reciclados", description = "Recurso para gerenciamento de materiais reciclados")
    @Operation(summary = "Lista materiais reciclados por tipo", description = "Lista materiais reciclados por tipo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais reciclados listados com sucesso",
            content = @Content(schema = @Schema(implementation = RecycledMaterial.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "type": "PLASTIC",
                            "quantity": 10
                        },
                        {
                            "id": 3,
                            "type": "PLASTIC",
                            "quantity": 8
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum material reciclado encontrado para o tipo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<RecycledMaterial>> getRecycledMaterialsByType(
            @PathVariable @Parameter(required = true, example = "PLASTIC") Materials type) {
        try {
            return new ResponseEntity<>(service.getByType(type), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar materiais reciclados por tipo com paginação
     * @param type O tipo do material reciclado
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see RecycledMaterialService#getByType(Materials, Pageable), Page<RecycledMaterial>
     */
    @Tag(name = "Materiais Reciclados", description = "Recurso para gerenciamento de materiais reciclados")
    @Operation(summary = "Lista materiais reciclados por tipo com paginação", description = "Lista materiais reciclados por tipo com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais reciclados listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum material reciclado encontrado para o tipo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/type/{type}/paged")
    public ResponseEntity<Page<RecycledMaterial>> getRecycledMaterialsByTypePaged(
            @PathVariable @Parameter(required = true, example = "PLASTIC") Materials type,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByType(type, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um material reciclado
     * @param id O ID do material reciclado a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see RecycledMaterialService#deleteById(Long)
     */
    @Tag(name = "Materiais Reciclados", description = "Recurso para gerenciamento de materiais reciclados")
    @Operation(summary = "Deleta um material reciclado", description = "Deleta um material reciclado e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Material reciclado deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Material reciclado deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar material reciclado")
    @ApiResponse(responseCode = "404", description = "Material reciclado não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteRecycledMaterial(
            @PathVariable @Parameter(required = true, example = "1") Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
