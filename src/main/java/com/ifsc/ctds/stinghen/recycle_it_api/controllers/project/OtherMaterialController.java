package com.ifsc.ctds.stinghen.recycle_it_api.controllers.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project.OtherMaterialRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.FullOtherMaterialResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.OtherMaterialResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.OtherMaterials;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.OtherMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.services.project.OtherMaterialService;
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
 * Classe de controle para os outros materiais
 * Implementa todos os métodos públicos disponíveis na OtherMaterialService
 * @see OtherMaterialService, OtherMaterial
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/other-materials")
public class OtherMaterialController {

    /**
     * O serviço de outros materiais que permite criar, atualizar, buscar, listar e deletar outros materiais
     * @see OtherMaterialService
     */
    private final OtherMaterialService service;

    /**
     * Método PATCH para atualizar o tipo de um outro material
     * @param id O ID do outro material a ser atualizado
     * @param type O novo tipo do outro material
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see OtherMaterialService#editType(Long, OtherMaterials)
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Atualiza o tipo do outro material", description = "Atualiza o tipo de um outro material existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Tipo do outro material atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Tipo do outro material atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar tipo do outro material")
    @ApiResponse(responseCode = "404", description = "Outro material não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/type")
    public ResponseEntity<FeedbackResponseDTO> updateOtherMaterialType(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "METAL") OtherMaterials type) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editType(id, type), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a descrição de um outro material
     * @param id O ID do outro material a ser atualizado
     * @param description A nova descrição do outro material
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see OtherMaterialService#editDescription(Long, String)
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Atualiza a descrição do outro material", description = "Atualiza a descrição de um outro material existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Descrição do outro material atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Descrição do outro material atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar descrição do outro material")
    @ApiResponse(responseCode = "404", description = "Outro material não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/description")
    public ResponseEntity<FeedbackResponseDTO> updateOtherMaterialDescription(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Ferro reciclável para construção") String description) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editDescription(id, description), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um outro material pelo ID
     * @param id O ID do outro material a ser buscado
     * @return Um ResponseEntity contendo o outro material e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see OtherMaterialService#getObjectById(Long), OtherMaterial
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Busca outro material pelo ID", description = "Busca um outro material pelo ID e retorna o outro material com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Outro material encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = OtherMaterial.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "type": "METAL",
                        "quantity": 5,
                        "description": "Ferro reciclável para construção"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Outro material não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<OtherMaterial> getOtherMaterialById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os outros materiais
     * @return Um ResponseEntity contendo a lista de outros materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see OtherMaterialService#getAll(), List<OtherMaterial>
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Lista todos os outros materiais", description = "Lista todos os outros materiais e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Outros materiais listados com sucesso",
            content = @Content(schema = @Schema(implementation = OtherMaterial.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "type": "METAL",
                            "quantity": 5,
                            "description": "Ferro reciclável para construção"
                        },
                        {
                            "id": 2,
                            "type": "GLASS",
                            "quantity": 10,
                            "description": "Vidro colorido para artesanato"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum outro material encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<OtherMaterial>> getAllOtherMaterials() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os outros materiais com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de outros materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see OtherMaterialService#getAll(Pageable), Page<OtherMaterial>
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Lista todos os outros materiais com paginação", description = "Lista todos os outros materiais com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Outros materiais listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum outro material encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<OtherMaterial>> getAllOtherMaterialsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar outros materiais por tipo
     * @param type O tipo do outro material
     * @return Um ResponseEntity contendo a lista de outros materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see OtherMaterialService#getByType(OtherMaterials), List<OtherMaterial>
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Lista outros materiais por tipo", description = "Lista outros materiais por tipo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Outros materiais listados com sucesso",
            content = @Content(schema = @Schema(implementation = OtherMaterial.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "type": "METAL",
                            "quantity": 5,
                            "description": "Ferro reciclável para construção"
                        },
                        {
                            "id": 3,
                            "type": "METAL",
                            "quantity": 8,
                            "description": "Alumínio para reciclagem"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum outro material encontrado para o tipo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<OtherMaterial>> getOtherMaterialsByType(
            @PathVariable @Parameter(required = true, example = "METAL") OtherMaterials type) {
        try {
            return new ResponseEntity<>(service.getByType(type), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar outros materiais por tipo com paginação
     * @param type O tipo do outro material
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de outros materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see OtherMaterialService#getByType(OtherMaterials, Pageable), Page<OtherMaterial>
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Lista outros materiais por tipo com paginação", description = "Lista outros materiais por tipo com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Outros materiais listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum outro material encontrado para o tipo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/type/{type}/paged")
    public ResponseEntity<Page<OtherMaterial>> getOtherMaterialsByTypePaged(
            @PathVariable @Parameter(required = true, example = "METAL") OtherMaterials type,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByType(type, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar outros materiais por descrição contendo
     * @param description A descrição a ser buscada
     * @return Um ResponseEntity contendo a lista de outros materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see OtherMaterialService#getByDescriptionContaining(String), List<OtherMaterial>
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Lista outros materiais por descrição contendo", description = "Lista outros materiais por descrição contendo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Outros materiais listados com sucesso",
            content = @Content(schema = @Schema(implementation = OtherMaterial.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "type": "METAL",
                            "quantity": 5,
                            "description": "Ferro reciclável para construção"
                        },
                        {
                            "id": 4,
                            "type": "METAL",
                            "quantity": 3,
                            "description": "Ferro para construção civil"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum outro material encontrado para a descrição")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/search")
    public ResponseEntity<List<OtherMaterial>> getOtherMaterialsByDescriptionContaining(
            @RequestParam @Parameter(required = true, example = "construção") String description) {
        try {
            return new ResponseEntity<>(service.getByDescriptionContaining(description), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar outros materiais por descrição contendo com paginação
     * @param description A descrição a ser buscada
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de outros materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see OtherMaterialService#getByDescriptionContaining(String, Pageable), Page<OtherMaterial>
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Lista outros materiais por descrição contendo com paginação", description = "Lista outros materiais por descrição contendo com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Outros materiais listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum outro material encontrado para a descrição")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/search/paged")
    public ResponseEntity<Page<OtherMaterial>> getOtherMaterialsByDescriptionContainingPaged(
            @RequestParam @Parameter(required = true, example = "construção") String description,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getByDescriptionContaining(description, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um outro material
     * @param id O ID do outro material a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see OtherMaterialService#deleteById(Long)
     */
    @Tag(name = "Outros Materiais", description = "Recurso para gerenciamento de outros materiais")
    @Operation(summary = "Deleta um outro material", description = "Deleta um outro material e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Outro material deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Outro material deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar outro material")
    @ApiResponse(responseCode = "404", description = "Outro material não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deleteOtherMaterial(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
