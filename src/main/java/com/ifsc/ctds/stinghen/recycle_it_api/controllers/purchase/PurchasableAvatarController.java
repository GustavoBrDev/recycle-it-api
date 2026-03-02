package com.ifsc.ctds.stinghen.recycle_it_api.controllers.purchase;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.purchase.PurchasableAvatarResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.enums.Avatar;
import com.ifsc.ctds.stinghen.recycle_it_api.models.purchase.PurchasableAvatar;
import com.ifsc.ctds.stinghen.recycle_it_api.services.purchase.PurchasableAvatarService;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Classe de controle para os avatares compráveis
 * Implementa todos os métodos públicos disponíveis na PurchasableAvatarService
 * @see PurchasableAvatarService, PurchasableAvatar
 * @version 1.0
 * @since 01/03/2026
 * @author Gustavo Stinghen
 */
@RestController
@AllArgsConstructor
@RequestMapping("/purchasable-avatars")
public class PurchasableAvatarController {

    /**
     * O serviço de avatares compráveis que permite criar, atualizar, buscar, listar e deletar avatares compráveis
     * @see PurchasableAvatarService
     */
    private final PurchasableAvatarService service;

    /**
     * Método POST para criar um novo avatar comprável
     * @param avatar O avatar comprável a ser criado
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see PurchasableAvatarService#create(PurchasableAvatar)
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Cria um novo avatar comprável", description = "Cria um novo avatar comprável e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Avatar comprável criado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Avatar comprável criado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao criar avatar comprável")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createPurchasableAvatar(
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = PurchasableAvatar.class)),
            example = """
                    {
                        "price": 100,
                        "isOnSale": true,
                        "avatar": "DEFAULT"
                    }
                    """) @Valid PurchasableAvatar avatar) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.create(avatar), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PUT para atualizar um avatar comprável existente
     * @param id O ID do avatar a ser atualizado
     * @param avatar O avatar comprável atualizado
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasableAvatarService#update(Long, PurchasableAvatar)
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Atualiza um avatar comprável", description = "Atualiza um avatar comprável existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatar comprável atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Avatar comprável atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar avatar comprável")
    @ApiResponse(responseCode = "404", description = "Avatar comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasableAvatar(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = PurchasableAvatar.class)),
            example = """
                    {
                        "price": 150,
                        "isOnSale": false,
                        "avatar": "PREMIUM"
                    }
                    """) @Valid PurchasableAvatar avatar) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.update(id, avatar), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método PATCH para atualizar o tipo de avatar de um avatar comprável
     * @param id O ID do avatar comprável a ser atualizado
     * @param avatarType O novo tipo de avatar
     * @return Um ResponseEntity contendo o feedback da atualização e o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasableAvatarService#editAvatar(Long, Avatar)
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Atualiza o tipo de avatar do avatar comprável", description = "Atualiza o tipo de avatar de um avatar comprável existente e retorna feedback da operação com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Tipo de avatar atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Tipo de avatar atualizado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar tipo de avatar")
    @ApiResponse(responseCode = "404", description = "Avatar comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @PatchMapping("/{id}/avatar-type")
    public ResponseEntity<FeedbackResponseDTO> updatePurchasableAvatarType(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id,
            @RequestBody @Parameter(required = true, example = "PREMIUM") Avatar avatarType) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.editAvatar(id, avatarType), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método GET para buscar um avatar comprável pelo ID
     * @param id O ID do avatar a ser buscado
     * @return Um ResponseEntity contendo o avatar e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableAvatarService#getObjectById(Long), PurchasableAvatar
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Busca avatar comprável pelo ID", description = "Busca um avatar comprável pelo ID e retorna o avatar com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatar comprável encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableAvatar.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "price": 100,
                        "isOnSale": true,
                        "avatar": "DEFAULT"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Avatar comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}")
    public ResponseEntity<PurchasableAvatar> getPurchasableAvatarById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>(service.getObjectById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para buscar um avatar comprável como DTO pelo ID
     * @param id O ID do avatar a ser buscado
     * @return Um ResponseEntity contendo o avatar e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableAvatarService#getById(Long), PurchasableAvatarResponseDTO
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Busca avatar comprável como DTO pelo ID", description = "Busca um avatar comprável pelo ID e retorna como DTO com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatar comprável encontrado com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableAvatarResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "price": 100,
                        "isOnSale": true,
                        "avatar": "DEFAULT",
                        "type": "PURCHASABLE_AVATAR"
                    }
                    """)))
    @ApiResponse(responseCode = "404", description = "Avatar comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/{id}/dto")
    public ResponseEntity<PurchasableAvatarResponseDTO> getPurchasableAvatarDtoById(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((PurchasableAvatarResponseDTO) service.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os avatares compráveis
     * @return Um ResponseEntity contendo a lista de avatares e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableAvatarService#getAll(), List<PurchasableAvatar>
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Lista todos os avatares compráveis", description = "Lista todos os avatares compráveis e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatares compráveis listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableAvatar.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "price": 100,
                            "isOnSale": true,
                            "avatar": "DEFAULT"
                        },
                        {
                            "id": 2,
                            "price": 150,
                            "isOnSale": false,
                            "avatar": "PREMIUM"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum avatar comprável encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<PurchasableAvatar>> getAllPurchasableAvatars() {
        try {
            return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os avatares compráveis com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de avatares e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableAvatarService#getAll(Pageable), Page<PurchasableAvatar>
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Lista todos os avatares compráveis com paginação", description = "Lista todos os avatares compráveis com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatares compráveis listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum avatar comprável encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/paged")
    public ResponseEntity<Page<PurchasableAvatar>> getAllPurchasableAvatarsPaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAll(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar avatares compráveis por tipo
     * @param avatarType O tipo de avatar
     * @return Um ResponseEntity contendo a lista de avatares e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableAvatarService#getByAvatarType(Avatar), List<PurchasableAvatar>
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Lista avatares compráveis por tipo", description = "Lista avatares compráveis por tipo e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatares compráveis listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableAvatar.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "price": 100,
                            "isOnSale": true,
                            "avatar": "DEFAULT"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum avatar comprável encontrado para o tipo")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/avatar-type/{avatarType}")
    public ResponseEntity<List<PurchasableAvatar>> getPurchasableAvatarsByAvatarType(
            @PathVariable @Parameter(required = true, example = "DEFAULT") Avatar avatarType) {
        try {
            return new ResponseEntity<>(service.getByAvatarType(avatarType), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os avatares compráveis em promoção
     * @return Um ResponseEntity contendo a lista de avatares e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableAvatarService#getAllOnSale(), List<PurchasableAvatar>
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Lista todos os avatares compráveis em promoção", description = "Lista todos os avatares compráveis em promoção e retorna uma lista com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatares compráveis em promoção listados com sucesso",
            content = @Content(schema = @Schema(implementation = PurchasableAvatar.class),
            examples = @ExampleObject(value = """
                    [
                        {
                            "id": 1,
                            "price": 100,
                            "isOnSale": true,
                            "avatar": "DEFAULT"
                        }
                    ]
                    """)))
    @ApiResponse(responseCode = "404", description = "Nenhum avatar comprável em promoção encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/on-sale")
    public ResponseEntity<List<PurchasableAvatar>> getAllPurchasableAvatarsOnSale() {
        try {
            return new ResponseEntity<>(service.getAllOnSale(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método GET para listar todos os avatares compráveis em promoção com paginação
     * @param pageable Configurações de paginação
     * @return Um ResponseEntity contendo a página de avatares e o status HTTP 200 (OK) ou o status HTTP 404 (Not Found).
     * @see PurchasableAvatarService#getAllOnSale(Pageable), Page<PurchasableAvatar>
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Lista todos os avatares compráveis em promoção com paginação", description = "Lista todos os avatares compráveis em promoção com paginação e retorna uma página com o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatares compráveis em promoção listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum avatar comprável em promoção encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @GetMapping("/on-sale/paged")
    public ResponseEntity<Page<PurchasableAvatar>> getAllPurchasableAvatarsOnSalePaged(Pageable pageable) {
        try {
            return new ResponseEntity<>(service.getAllOnSale(pageable), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Método DELETE para deletar um avatar comprável
     * @param id O ID do avatar a ser deletado
     * @return Um ResponseEntity contendo o status HTTP 200 (OK) ou o status HTTP 400 (Bad Request).
     * @see PurchasableAvatarService#deleteById(Long)
     */
    @Tag(name = "Avatares Compráveis", description = "Recurso para gerenciamento de avatares compráveis")
    @Operation(summary = "Deleta um avatar comprável", description = "Deleta um avatar comprável e retorna o status HTTP 200")
    @ApiResponse(responseCode = "200", description = "Avatar comprável deletado com sucesso",
            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class),
            examples = @ExampleObject(value = """
                    {
                        "mainMessage": "Avatar comprável deletado",
                        "content": null,
                        "isAlert": false,
                        "isError": false
                    }
                    """)))
    @ApiResponse(responseCode = "400", description = "Erro ao deletar avatar comprável")
    @ApiResponse(responseCode = "404", description = "Avatar comprável não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @SecurityRequirement(name = "Bearer")
    @DeleteMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> deletePurchasableAvatar(
            @PathVariable @Parameter(required = true, example = "1") @NotNull @Positive Long id) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) service.deleteById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
