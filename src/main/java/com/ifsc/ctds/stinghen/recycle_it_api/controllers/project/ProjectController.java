package com.ifsc.ctds.stinghen.recycle_it_api.controllers.project;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.project.ProjectRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.ResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.FullProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.ProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.project.QuickProjectResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.Project;
import com.ifsc.ctds.stinghen.recycle_it_api.models.project.ProjectMaterial;
import com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser;
import com.ifsc.ctds.stinghen.recycle_it_api.services.project.ProjectService;
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
 * Classe de controle para os projetos
 * Implementa todos os métodos públicos disponíveis na ProjectService
 * @see ProjectService, Project
 * @version 1.0
 * @since 28/02/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    /**
     * O serviço de projetos que permite criar, atualizar, buscar, listar e deletar projetos
     * @see ProjectService
     */
    private final ProjectService service;

    /**
     * Método POST para criar um novo projeto
     * @param requestDTO A {@link ProjectRequestDTO} contendo os dados do projeto
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see ProjectService#create(ProjectRequestDTO), ProjectRequestDTO
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Cria um novo projeto", description = "Cria um novo projeto e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Projeto criado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar projeto")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createProject(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = ProjectRequestDTO.class)),
            example = """
                    {
                        "text": "Projeto de reciclagem de garrafas plásticas",
                        "description": "Transforme garrafas plásticas em objetos úteis",
                        "materials": [
                            {
                                "material": "PLASTIC",
                                "quantity": 10
                            }
                        ],
                        "instructions": "Lave bem as garrafas, corte e monte o objeto desejado"
                    }
                    """) @Valid ProjectRequestDTO requestDTO) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(requestDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar um projeto existente
     * @param id O ID do projeto a ser atualizado
     * @param requestDTO A {@link ProjectRequestDTO} contendo os dados atualizados
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectService#update(Long, ProjectRequestDTO), ProjectRequestDTO
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Atualiza um projeto", description = "Atualiza um projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Projeto atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar projeto")
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateProject(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = ProjectRequestDTO.class)),
            example = """
                    {
                        "text": "Projeto atualizado de reciclagem",
                        "description": "Descrição atualizada do projeto",
                        "materials": [
                            {
                                "material": "PLASTIC",
                                "quantity": 15
                            }
                        ],
                        "instructions": "Instruções atualizadas"
                    }
                    """) @Valid ProjectRequestDTO requestDTO) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, requestDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o texto de um projeto
     * @param id O ID do projeto a ser atualizado
     * @param text O novo texto do projeto
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectService#editText(Long, String)
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Atualiza o texto do projeto", description = "Atualiza o texto de um projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Texto do projeto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Texto do projeto atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar texto do projeto")
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/text")
    public ResponseEntity<FeedbackResponseDTO> updateProjectText(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Novo texto do projeto") String text) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editText(id, text), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a descrição de um projeto
     * @param id O ID do projeto a ser atualizado
     * @param description A nova descrição do projeto
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectService#editDescription(Long, String)
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Atualiza a descrição do projeto", description = "Atualiza a descrição de um projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Descrição do projeto atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Descrição do projeto atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar descrição do projeto")
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/description")
    public ResponseEntity<FeedbackResponseDTO> updateProjectDescription(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Nova descrição do projeto") String description) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editDescription(id, description), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar as instruções de um projeto
     * @param id O ID do projeto a ser atualizado
     * @param instructions As novas instruções do projeto
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectService#editInstructions(Long, String)
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Atualiza as instruções do projeto", description = "Atualiza as instruções de um projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Instruções do projeto atualizadas com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Instruções do projeto atualizadas",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar instruções do projeto")
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/instructions")
    public ResponseEntity<FeedbackResponseDTO> updateProjectInstructions(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "Novas instruções do projeto") String instructions) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editInstructions(id, instructions), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar a quantidade de material de um projeto
     * @param projectId O ID do projeto a ser atualizado
     * @param materialId O ID do material a ser atualizado
     * @param newQuantity A nova quantidade do material
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectService#updateMaterialQuantity(Long, Long, Long)
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Atualiza a quantidade de material do projeto", description = "Atualiza a quantidade de material de um projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Quantidade de material atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Quantidade de material atualizada",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar quantidade de material")
    @ApiResponse(responseCode = "404", description = "Projeto ou material não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{projectId}/materials/{materialId}")
    public ResponseEntity<FeedbackResponseDTO> updateProjectMaterialQuantity(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long projectId,
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long materialId,
            @RequestBody @Parameter(required = true, example = "15") @NotNull @Positive Long newQuantity) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.updateMaterialQuantity(projectId, materialId, newQuantity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para iniciar um projeto
     * @param email O email do usuário que está iniciando
     * @param projectId O ID do projeto a ser iniciado
     * @return Um ResponseEntity contendo o feedback do início e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectService#start(String, Long)
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Inicia um projeto", description = "Inicia um projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto iniciado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Projeto iniciado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao iniciar projeto")
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{projectId}/start")
    public ResponseEntity<FeedbackResponseDTO> startProject(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long projectId,
            @RequestParam @Parameter(required = true, example = "usuario@exemplo.com") String email) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.start(email, projectId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para finalizar um projeto
     * @param projectId O ID do projeto a ser finalizado
     * @param user O usuário que está finalizando
     * @return Um ResponseEntity contendo o feedback da finalização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see ProjectService#finalize(RegularUser, Long)
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Finaliza um projeto", description = "Finaliza um projeto existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto finalizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Projeto finalizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao finalizar projeto")
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping("/{projectId}/finalize")
    public ResponseEntity<FeedbackResponseDTO> finalizeProject(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long projectId,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = RegularUser.class)),
            example = """
                    {
                        "id": 1,
                        "name": "João Silva",
                        "email": "joao@exemplo.com"
                    }
                    """) RegularUser user) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.finalize(user, projectId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar os materiais de um projeto
     * @param projectId O ID do projeto
     * @return Um ResponseEntity contendo a lista de materiais e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getProjectMaterials(Long), List<ProjectMaterial>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Busca materiais do projeto", description = "Busca os materiais de um projeto pelo ID e retorna a lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Materiais encontrados com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectMaterial.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "material": "PLASTIC",
                            "quantity": 10
                        },
                        {
                            "id": 2,
                            "material": "PAPER",
                            "quantity": 5
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{projectId}/materials")
    public ResponseEntity<List<ProjectMaterial>> getProjectMaterials(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long projectId) {
        try {
            return new ResponseEntity<>(service.getProjectMaterials(projectId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um projeto pelo ID
     * @param id O ID do projeto a ser buscado
     * @return Um ResponseEntity contendo o projeto e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getObjectById(Long), Project
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Busca projeto pelo ID", description = "Busca um projeto pelo ID e retorna o projeto com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = Project.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "text": "Projeto de reciclagem de garrafas plásticas",
                        "description": "Transforme garrafas plásticas em objetos úteis",
                        "materials": [
                            {
                                "id": 1,
                                "material": "PLASTIC",
                                "quantity": 10
                            }
                        ],
                        "instructions": "Lave bem as garrafas, corte e monte o objeto desejado"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um projeto completo como DTO pelo ID
     * @param id O ID do projeto a ser buscado
     * @return Um ResponseEntity contendo o projeto e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getFullById(Long), FullProjectResponseDTO
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Busca projeto completo como DTO pelo ID", description = "Busca um projeto pelo ID e retorna como DTO completo com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = FullProjectResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "text": "Projeto de reciclagem de garrafas plásticas",
                        "description": "Transforme garrafas plásticas em objetos úteis",
                        "materials": [
                            {
                                "id": 1,
                                "material": "PLASTIC",
                                "quantity": 10
                            }
                        ],
                        "instructions": "Lave bem as garrafas, corte e monte o objeto desejado",
                        "userComments": []
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/full")
    public ResponseEntity<FullProjectResponseDTO> getFullProjectById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FullProjectResponseDTO) service.getFullById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um projeto como DTO pelo ID
     * @param id O ID do projeto a ser buscado
     * @return Um ResponseEntity contendo o projeto e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getResponseById(Long), ProjectResponseDTO
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Busca projeto como DTO pelo ID", description = "Busca um projeto pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "text": "Projeto de reciclagem de garrafas plásticas",
                        "description": "Transforme garrafas plásticas em objetos úteis",
                        "materials": [
                            {
                                "id": 1,
                                "material": "PLASTIC",
                                "quantity": 10
                            }
                        ],
                        "instructions": "Lave bem as garrafas, corte e monte o objeto desejado",
                        "userComments": []
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<ProjectResponseDTO> getProjectDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((ProjectResponseDTO) service.getResponseById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um projeto resumido como DTO pelo ID
     * @param id O ID do projeto a ser buscado
     * @return Um ResponseEntity contendo o projeto e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getQuickById(Long), QuickProjectResponseDTO
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Busca projeto resumido como DTO pelo ID", description = "Busca um projeto pelo ID e retorna como DTO resumido com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projeto encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = QuickProjectResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "description": "Transforme garrafas plásticas em objetos úteis",
                        "materials": [
                            {
                                "id": 1,
                                "material": "PLASTIC",
                                "quantity": 10
                            }
                        ]
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/quick")
    public ResponseEntity<QuickProjectResponseDTO> getQuickProjectById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((QuickProjectResponseDTO) service.getQuickById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os projetos
     * @return Um ResponseEntity contendo a lista de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getAll(), List<Project>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Lista todos os projetos", description = "Lista todos os projetos e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso",
            content = @Content(schema = @Schema(implementation = Project.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "text": "Projeto de reciclagem de garrafas plásticas",
                            "description": "Transforme garrafas plásticas em objetos úteis",
                            "materials": [
                                {
                                    "id": 1,
                                    "material": "PLASTIC",
                                    "quantity": 10
                                }
                            ],
                            "instructions": "Lave bem as garrafas, corte e monte o objeto desejado"
                        },
                        {
                            "id": 2,
                            "text": "Projeto de reciclagem de papel",
                            "description": "Crie objetos úteis com papel reciclado",
                            "materials": [
                                {
                                    "id": 2,
                                    "material": "PAPER",
                                    "quantity": 5
                                }
                            ],
                            "instructions": "Cole o papel, corte e monte o objeto desejado"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os projetos como DTO resumido
     * @return Um ResponseEntity contendo a lista de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getAllQuick(), List<QuickProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Lista todos os projetos como DTO resumido", description = "Lista todos os projetos como DTO resumido e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso",
            content = @Content(schema = @Schema(implementation = QuickProjectResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "description": "Transforme garrafas plásticas em objetos úteis",
                            "materials": [
                                {
                                    "id": 1,
                                    "material": "PLASTIC",
                                    "quantity": 10
                                }
                            ]
                        },
                        {
                            "id": 2,
                            "description": "Crie objetos úteis com papel reciclado",
                            "materials": [
                                {
                                    "id": 2,
                                    "material": "PAPER",
                                    "quantity": 5
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/quick")
    public ResponseEntity<List<QuickProjectResponseDTO>> getAllQuickProjects() {
        try {
            return new ResponseEntity<>(service.getAllQuick(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os projetos como DTO
     * @return Um ResponseEntity contendo a lista de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getAllResponse(), List<ProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Lista todos os projetos como DTO", description = "Lista todos os projetos como DTO e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "text": "Projeto de reciclagem de garrafas plásticas",
                            "description": "Transforme garrafas plásticas em objetos úteis",
                            "materials": [
                                {
                                    "id": 1,
                                    "material": "PLASTIC",
                                    "quantity": 10
                                }
                            ],
                            "instructions": "Lave bem as garrafas, corte e monte o objeto desejado",
                            "userComments": []
                        },
                        {
                            "id": 2,
                            "text": "Projeto de reciclagem de papel",
                            "description": "Crie objetos úteis com papel reciclado",
                            "materials": [
                                {
                                    "id": 2,
                                    "material": "PAPER",
                                    "quantity": 5
                                }
                            ],
                            "instructions": "Cole o papel, corte e monte o objeto desejado",
                            "userComments": []
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjectsAsDto() {
        try {
            return new ResponseEntity<>(service.getAllResponse(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os projetos como DTO completo
     * @return Um ResponseEntity contendo a lista de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getAllFull(), List<FullProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Lista todos os projetos como DTO completo", description = "Lista todos os projetos como DTO completo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso",
            content = @Content(schema = @Schema(implementation = FullProjectResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "text": "Projeto de reciclagem de garrafas plásticas",
                            "description": "Transforme garrafas plásticas em objetos úteis",
                            "materials": [
                                {
                                    "id": 1,
                                    "material": "PLASTIC",
                                    "quantity": 10
                                }
                            ],
                            "instructions": "Lave bem as garrafas, corte e monte o objeto desejado",
                            "userComments": []
                        },
                        {
                            "id": 2,
                            "text": "Projeto de reciclagem de papel",
                            "description": "Crie objetos úteis com papel reciclado",
                            "materials": [
                                {
                                    "id": 2,
                                    "material": "PAPER",
                                    "quantity": 5
                                }
                            ],
                            "instructions": "Cole o papel, corte e monte o objeto desejado",
                            "userComments": []
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/full")
    public ResponseEntity<List<FullProjectResponseDTO>> getAllFullProjects() {
        try {
            return new ResponseEntity<>(service.getAllFull(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os projetos com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getAll(Pageable), Page<Project>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Lista todos os projetos com paginação", description = "Lista todos os projetos com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<Project>> getAllProjectsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os projetos como DTO resumido com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getAllQuick(Pageable), Page<QuickProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Lista todos os projetos como DTO resumido com paginação", description = "Lista todos os projetos como DTO resumido com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/quick/paged")
    public ResponseEntity<Page<QuickProjectResponseDTO>> getAllQuickProjectsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllQuick(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os projetos como DTO com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getAllResponse(Pageable), Page<ProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Lista todos os projetos como DTO com paginação", description = "Lista todos os projetos como DTO com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/dto/paged")
    public ResponseEntity<Page<ProjectResponseDTO>> getAllProjectsAsDtoPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllResponse(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os projetos como DTO completo com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getAllFull(Pageable), Page<FullProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Lista todos os projetos como DTO completo com paginação", description = "Lista todos os projetos como DTO completo com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/full/paged")
    public ResponseEntity<Page<FullProjectResponseDTO>> getAllFullProjectsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllFull(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar projetos filtrados
     * @param search Termo de busca
     * @return Um ResponseEntity contendo a página de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getFiltered(String), Page<ProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Busca projetos filtrados", description = "Busca projetos filtrados por termo de busca e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "text": "Projeto de reciclagem de garrafas plásticas",
                            "description": "Transforme garrafas plásticas em objetos úteis",
                            "materials": [
                                {
                                    "id": 1,
                                    "material": "PLASTIC",
                                    "quantity": 10
                                }
                            ],
                            "instructions": "Lave bem as garrafas, corte e monte o objeto desejado",
                            "userComments": []
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/filtered")
    public ResponseEntity<Page<ProjectResponseDTO>> getFilteredProjects(
            @RequestParam @Parameter(required = true, example = "plástico") String search) {
        try {
            return new ResponseEntity<>(service.getFiltered(search), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar projetos filtrados com paginação
     * @param search Termo de busca
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getFiltered(String, Pageable), Page<ProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Busca projetos filtrados com paginação", description = "Busca projetos filtrados por termo de busca com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/filtered/paged")
    public ResponseEntity<Page<ProjectResponseDTO>> getFilteredProjectsPaged(
            @RequestParam @Parameter(required = true, example = "plástico") String search,
            Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getFiltered(search, pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar projetos filtrados como DTO resumido
     * @param search Termo de busca
     * @return Um ResponseEntity contendo a página de projetos e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see ProjectService#getFilteredAsQuick(String), Page<QuickProjectResponseDTO>
     */
    @Tag(name = "Projetos", description = "Recurso para gerenciamento de projetos")
    @Operation(summary = "Busca projetos filtrados como DTO resumido", description = "Busca projetos filtrados por termo de busca e retorna como DTO resumido com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados com sucesso",
            content = @Content(schema = @Schema(implementation = QuickProjectResponseDTO.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "description": "Transforme garrafas plásticas em objetos úteis",
                            "materials": [
                                {
                                    "id": 1,
                                    "material": "PLASTIC",
                                    "quantity": 10
                                }
                            ]
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum projeto encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/filtered/quick")
    public ResponseEntity<Page<QuickProjectResponseDTO>> getFilteredProjectsAsQuick(
            @RequestParam @Parameter(required = true, example = "plástico") String search) {
        try {
            return new ResponseEntity<>(service.getFilteredAsQuick(search), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
