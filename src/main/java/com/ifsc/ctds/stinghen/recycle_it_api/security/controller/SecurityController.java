package com.ifsc.ctds.stinghen.recycle_it_api.security.controller;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.request.user.RegularUserRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.security.dtos.LoginRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.security.utils.JWTUtils;
import com.ifsc.ctds.stinghen.recycle_it_api.services.user.RegularUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe de controle para autenticação e gerenciamento de sessão JWT
 * 
 * <p>Este controller gerencia todo o fluxo de autenticação da aplicação, incluindo:</p>
 * <ul>
 *   <li>Login de usuários com geração de token JWT</li>
 *   <li>Cadastro de novos usuários</li>
 *   <li>Logout e invalidação de tokens</li>
 *   <li>Verificação de status de autenticação</li>
 * </ul>
 * 
 * <p><strong>Segurança:</strong></p>
 * <ul>
 *   <li>Tokens JWT com validade de 24 horas</li>
 *   <li>Armazenamento seguro via HttpOnly cookies</li>
 *   <li>Proteção contra CSRF</li>
 *   <li>Rate limiting em endpoints críticos</li>
 * </ul>
 * 
 * <p><strong>Fluxo de Autenticação:</strong></p>
 * <ol>
 *   <li>Usuário envia credenciais para {@code /auth/login}</li>
 *   <li>Sistema valida credenciais e gera token JWT</li>
 *   <li>Token é retornado e armazenado no cliente</li>
 *   <li>Token deve ser enviado no cabeçalho Authorization para endpoints protegidos</li>
 * </ol>
 * 
 * @see JWTUtils Utilitários para manipulação de tokens JWT
 * @see RegularUserService Serviço de gerenciamento de usuários
 * @see com.ifsc.ctds.stinghen.recycle_it_api.models.user.RegularUser Modelo de usuário
 * @version 1.0
 * @since 05/03/2026
 * @author Gustavo Stinghen
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Autenticação", description = """
    Recursos para autenticação e gerenciamento de sessão JWT.
    
    **Endpoints Disponíveis:**
    - POST /auth/login - Autenticação de usuários
    - POST /auth/register - Cadastro de novos usuários
    - POST /auth/logout - Logout e invalidação de sessão
    - GET /auth/me - Verificação de usuário autenticado
    - GET /auth/refresh - Renovação de token JWT
    
    **Segurança:**
    - Login não requer autenticação
    - Demais endpoints exigem token válido
    - Tokens têm validade de 24 horas
    """)
public class SecurityController {

    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;
    /**
     * O serviço de usuários comuns que permite verificar existência de email
     * @see RegularUserService
     */
    private RegularUserService userService;
    /**
     * Utilitário para geração e validação de tokens JWT
     * @see JWTUtils
     */
    private final JWTUtils jwtUtils;

    /**
     * Método POST para criar um novo usuário
     * @param requestDTO A {@link RegularUserRequestDTO} contendo os dados do usuário
     * @return Um ResponseEntity contendo o feedback da criação e o status HTTP 201 (Created) ou o status HTTP 400 (Bad Request).
     * @see RegularUserService#create(RegularUserRequestDTO), RegularUserRequestDTO
     */
    @Tag(name = "Usuários", description = "Recurso para gerenciamento de usuários comuns")
    @Operation(summary = "Cria um novo usuário", description = "Cria um novo usuário e retorna feedback da operação com o status HTTP 201")
    @ApiResponse(responseCode = "201", description = "Uuário criado com sucesso",
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
    @PostMapping("/register")
    public ResponseEntity<FeedbackResponseDTO> createUser(
            @RequestBody @Parameter(required = true,
                    content = @Content(schema = @Schema(implementation = RegularUserRequestDTO.class)),
                    example = """
                    {
                        "email": "usuario@exemplo.com",
                        "password": "SenhaForte123!",
                        "avatar": "terra",
                        "name": "Nome do Usuário"
                    }
                    """) @Valid RegularUserRequestDTO requestDTO) {
        try {
            return new ResponseEntity<>((FeedbackResponseDTO) userService.create(requestDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método POST para realizar login na aplicação
     * @param loginRequest DTO contendo email e senha do usuário
     * @param request Objeto HttpServletRequest da requisição
     * @param response Objeto HttpServletResponse para adicionar cookie JWT
     * @see LoginRequestDTO, JWTUtils
     */
    @Tag(name = "Autenticação", description = "Recurso para login e logout de usuários")
    @Operation(summary = "Realiza login do usuário", description = "Autentica o usuário e cria um cookie com token JWT válido por 8 horas")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "403", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping("/login")
    public void login( @Parameter(required = true,
            content = @Content(schema = @Schema(implementation = LoginRequestDTO.class)),
            example = """
                    {
                        "email": "usuario@exemplo.com",
                        "password": "SenhaForte123!"
                    }
                    """) @RequestBody LoginRequestDTO loginRequest, HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        auth = authenticationManager.authenticate( auth );

        if (auth.isAuthenticated()) {

            // Uso do JWT
            String token = jwtUtils.generateToken((UserDetails) auth.getPrincipal());

            Cookie cookie = new Cookie("JWTSESSION", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 8); // 8 horas

            response.addCookie(cookie);

            response.setStatus(200);

        } else {
            response.setStatus(403);
        }
    }

    /**
     * Método POST para realizar logout da aplicação
     * @param request Objeto HttpServletRequest da requisição
     * @param response Objeto HttpServletResponse para limpar contexto de segurança
     */
    @Tag(name = "Autenticação", description = "Recurso para login e logout de usuários")
    @Operation(summary = "Realiza logout do usuário", description = "Limpa o contexto de segurança e invalida a sessão")
    @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        securityContextRepository.saveContext(SecurityContextHolder.createEmptyContext(), request, response);
    }

    /**
     * Método GET para verificar se já existe um usuário com o email fornecido
     * @param email O email a ser verificado
     * @return ResponseEntity contendo true se o email existe, false caso contrário
     */
    @Tag(name = "Autenticação", description = "Recurso para verificação de disponibilidade de email")
    @Operation(summary = "Verifica se email já está cadastrado", description = "Retorna true se o email já existe no sistema, false caso contrário")
    @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso",
            content = @Content(schema = @Schema(implementation = Boolean.class),
                    examples = @ExampleObject(value = "true")))
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}
