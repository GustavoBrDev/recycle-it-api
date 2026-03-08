# Recycle It API

🌱 **API completa para plataforma gamificada de reciclagem e sustentabilidade**

## 📋 Sumário

- [Visão Geral](#-visão-geral)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Endpoints](#-endpoints)
- [Autenticação](#-autenticação)
- [Como Executar](#-como-executar)
- [Documentação](#-documentação)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

## 🌍 Visão Geral

A **Recycle It API** é o backend completo para o aplicativo Recycle It, uma plataforma inovadora que combina sustentabilidade com gamificação para incentivar práticas ecológicas. A API oferece um ecossistema completo para gestão de metas de reciclagem, sistema de ligas competitivas, conteúdo educativo e muito mais.

### 🎯 Objetivos

- **Educação Ambiental**: Fornecer conteúdo educativo sobre reciclagem e sustentabilidade
- **Engajamento**: Gamificar o processo de reciclagem através de metas e recompensas
- **Comunidade**: Criar uma rede social focada em sustentabilidade
- **Impacto**: Medir e visualizar o impacto ambiental positivo gerado pelos usuários

## ⚡ Funcionalidades

### 🔄 Sistema de Metas
- **Metas de Reciclagem**: Definir e acompanhar metas de reciclagem por material
- **Metas de Redução**: Estabelecer metas para redução de consumo
- **Progresso**: Acompanhamento detalhado do progresso com estatísticas

### 🏆 Gamificação
- **Ligas Competitivas**: Sistema de ligas com rankings semanais
- **Pontuação**: Sistema complexo de pontos por atividades ecológicas
- **Recompensas**: Loja virtual com avatares e itens personalizáveis
- **Conquistas**: Sistema de badges e conquistas especiais

### 👥 Rede Social
- **Conexões**: Sistema de amizades entre usuários
- **Compartilhamento**: Compartilhamento de conquistas e progresso
- **Notificações**: Sistema completo de notificações em tempo real

### 📚 Conteúdo Educativo
- **Artigos**: Conteúdo educativo sobre reciclagem e sustentabilidade
- **Projetos**: Tutoriais passo a passo para reutilização de materiais
- **Comentários**: Sistema de comentários e interação com conteúdo

### 🛍️ Economia Virtual
- **Loja**: Sistema de compras com moedas virtuais
- **Avatares**: Personalização de avatares com itens ecológicos
- **Power-ups**: Funcionalidades especiais compráveis

## 🛠 Tecnologias

### Backend
- **Java 17**: Linguagem principal da aplicação
- **Spring Boot 3.x**: Framework principal
- **Spring Security**: Autenticação e autorização
- **Spring Data JPA**: Persistência de dados
- **MySQL**: Banco de dados relacional
- **JWT**: Tokens de autenticação

### Documentação
- **OpenAPI 3.0**: Especificação da API
- **Swagger UI**: Interface interativa de documentação
- **SpringDoc**: Geração automática de documentação

### Segurança
- **Role-Based Access Control**: Controle de acesso por roles
- **JWT Authentication**: Tokens seguros e assinados
- **Password Encryption**: Hashing BCrypt
- **CORS**: Configuração segura de cross-origin

## 🚀 Endpoints

### Autenticação
```
POST   /api/auth/login          - Login de usuário
POST   /api/auth/register       - Cadastro de novo usuário
POST   /api/auth/logout         - Logout
GET    /api/auth/me             - Verificar usuário autenticado
```

### Usuários
```
GET    /api/users               - Listar usuários (DEV)
GET    /api/users/{id}          - Obter usuário por ID
PUT    /api/users/{id}          - Atualizar usuário
DELETE /api/users/{id}          - Deletar usuário (DEV)
```

### Metas de Redução
```
GET    /api/goals/reduce/item/user/active           - Itens do usuário autenticado [DEV]
GET    /api/goals/reduce/item/user/{userId}/active  - Itens por usuário ID [DEV]
GET    /api/goals/reduce/item/{id}                   - Obter item por ID
POST   /api/goals/reduce/item                        - Criar item
PUT    /api/goals/reduce/item/{id}                   - Atualizar item
DELETE /api/goals/reduce/item/{id}                   - Deletar item
```

### Projetos
```
GET    /api/projects                       - Listar projetos
GET    /api/projects/{id}                  - Obter projeto por ID
POST   /api/projects                       - Criar projeto [DEV]
PUT    /api/projects/{id}                  - Atualizar projeto [DEV]
DELETE /api/projects/{id}                  - Deletar projeto [DEV]

GET    /api/projects/recommended/reduce-goal              - Recomendados para usuário [DEV]
GET    /api/projects/recommended/reduce-goal/user/{id}   - Recomendados por usuário ID [DEV]
```

## 🔐 Autenticação

### JWT Token Flow

1. **Login**: Envie credenciais para `/api/auth/login`
2. **Token**: Receba um JWT com validade de 8 horas
3. **Uso**: Inclua o token no cabeçalho `Authorization: Bearer <token>`

### Exemplo de Requisição

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "usuario@email.com", "password": "senha123"}'

# Requisição Autenticada
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <seu_jwt_token>"
```

### Roles de Acesso

- **USER**: Usuário comum com acesso básico (padrão)
- **PUBLIC**: Rotas públicas sem necessidade de alteração
- **DEV**: Desenvolvedor com acesso completo à API

## 🏃‍♂️ Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Configuração

1. **Clone o repositório**
   ```bash
   git clone https://github.com/GustavoBrDev/recycle-it-api.git
   cd recycle-it-api
   ```

2. **Configure o banco de dados**
   ```sql
   CREATE DATABASE db_recycle_it;
   ```

3. **Variáveis de ambiente**
   ```bash
   export JWT_PASSWORD="sua_chave_secreta_jwt"
   export JWT_ISSUER="recycle-it-api"
   ```

4. **Execute a aplicação**
   ```bash
   mvn spring-boot:run
   ```

## 📚 Documentação

### Swagger UI

Acesse a documentação interativa em:
- **Local**: http://localhost:8080/swagger-ui.html
- **Produção**: https://api.recycleit.com/swagger-ui.html

### OpenAPI Spec

Especificação completa disponível em:
- **Local**: http://localhost:8080/api-docs
- **Produção**: https://api.recycleit.com/api-docs

## 📄 Licença

Este projeto está licenciado sob a **MIT License**.

---

**📞 Contato**

- **Desenvolvedor**: Gustavo Stinghen
- **GitHub**: https://github.com/GustavoBrDev

**🌱 Juntos por um planeta mais sustentável! 🌍**
