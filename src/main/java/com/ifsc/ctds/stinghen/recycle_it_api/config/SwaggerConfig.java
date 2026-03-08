package com.ifsc.ctds.stinghen.recycle_it_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Configuração do Swagger/OpenAPI para documentação da API
 * @author Gustavo Stinghen
 * @version 1.0
 * @since 08/03/2026
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configuração principal do OpenAPI
     * @return objeto OpenAPI com todas as configurações de documentação
     */
    @Bean
    public OpenAPI recycleItOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(devServer(), prodServer()))
                .security(List.of(securityRequirement()))
                .components(components());
    }

    /**
     * Informações gerais da API
     * @return objeto Info com detalhes do projeto
     */
    private Info apiInfo() {
        return new Info()
                .title("Recycle It API")
                .description("""
                        API completa para o aplicativo Recycle It - Plataforma gamificada de reciclagem e sustentabilidade.\n
                        \n
                        ## Funcionalidades Principais\n
                        \n
                        🌱 **Sustentabilidade**: Sistema completo para acompanhamento de metas de reciclagem e redução\n
                        🏆 **Gamificação**: Ligas competitivas, sistema de pontuação e recompensas\n
                        👥 **Social**: Conexões entre usuários e compartilhamento de conquistas\n
                        📚 **Educação**: Artigos e projetos educativos sobre reciclagem\n
                        🛍️ **Economia**: Loja virtual com avatares e itens personalizáveis\n
                        \n
                        ## Fluxo Principal\n
                        \n
                        1. **Autenticação**: Login via JWT com email e senha\n
                        2. **Definição de Metas**: Usuário define metas de reciclagem/redução\n
                        3. **Acompanhamento**: Registro de atividades e progresso\n
                        4. **Recompensas**: Pontuação, subida de liga e compras na loja\n
                        \n
                        ## Segurança\n
                        \n
                        🔐 **JWT Token**: Autenticação via Bearer Token\n
                        🛡️ **Role-Based Access**: Controle de acesso por roles (USER, ADMIN, DEV)\n
                        🔒 **HTTPS**: Comunicação segura em produção\n
                        \n
                        ## Padrões REST\n
                        \n
                        - Prefixo `/api` em todos os endpoints\n
                        - Rotas `/public` não exigem autenticação\n
                        - Paginação padrão com `Pageable`\n
                        - Respostas padronizadas com DTOs\n
                        - Tratamento de exceções global\n
                        """)
                .version("1.0.0")
                .contact(apiContact())
                .license(apiLicense());
    }

    /**
     * Informações de contato do desenvolvedor
     * @return objeto Contact com detalhes de contato
     */
    private Contact apiContact() {
        return new Contact()
                .name("Gustavo Stinghen")
                .email("gustavo.stinghen@example.com")
                .url("https://github.com/GustavoBrDev");
    }

    /**
     * Licença da API
     * @return objeto License com detalhes da licença
     */
    private License apiLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")
                .identifier("MIT");
    }

    /**
     * Configuração do servidor de desenvolvimento
     * @return objeto Server para ambiente de desenvolvimento
     */
    private Server devServer() {
        return new Server()
                .url("http://localhost:8080/api")
                .description("Servidor de Desenvolvimento")
                .variables(null);
    }

    /**
     * Configuração do servidor de produção
     * @return objeto Server para ambiente de produção
     */
    private Server prodServer() {
        return new Server()
                .url("https://api.recycleit.com/api")
                .description("Servidor de Produção")
                .variables(null);
    }

    /**
     * Requisito de segurança para autenticação JWT
     * @return objeto SecurityRequirement
     */
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("Bearer");
    }

    /**
     * Componentes de segurança e esquemas
     * @return objeto Components com configurações de segurança
     */
    private io.swagger.v3.oas.models.Components components() {
        return new io.swagger.v3.oas.models.Components()
                .securitySchemes(Map.of("Bearer", bearerSecurityScheme()));
    }

    /**
     * Esquema de segurança JWT Bearer
     * @return objeto SecurityScheme configurado
     */
    private SecurityScheme bearerSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("""
                        **Autenticação JWT Bearer Token**\n
                        \n
                        Para acessar endpoints protegidos, inclua o token JWT no cabeçalho Authorization:\n
                        \n
                        `Authorization: Bearer <seu_token_jwt>`\n
                        \n
                        **Como obter o token:**\n
                        1. Faça login em `/api/auth/login`\n
                        2. Copie o token retornado\n
                        3. Use nos endpoints protegidos\n
                        \n
                        **Validade do Token:** 24 horas\n
                        **Refresh Token:** Implementado para renovação automática\n
                        """);
    }
}
