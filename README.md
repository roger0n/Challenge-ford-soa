# Ford Competitive Intelligence API

## Descrição

Este projeto foi desenvolvido como solução para o desafio proposto pela Ford na disciplina de **Arquitetura Orientada a Serviços e Web Services**.

O objetivo da aplicação é permitir a consulta, comparação e gerenciamento de especificações técnicas de veículos concorrentes através de uma API REST.

A solução permite que o usuário informe:

- Marca
- Modelo
- Versão
- Lista de atributos desejados

E receba uma resposta padronizada contendo as especificações solicitadas.

A aplicação também possui autenticação e autorização utilizando **JWT (JSON Web Token)**, com diferentes níveis de acesso para usuários `USER` e `ADMIN`.

---

# Objetivo do Projeto

O projeto busca apoiar o processo de Inteligência Competitiva Automotiva, permitindo:

- Consulta padronizada de veículos concorrentes
- Comparação entre veículos
- Organização consistente dos dados
- Retorno estruturado em JSON
- Busca dinâmica de atributos
- Cadastro e gerenciamento de veículos
- Autenticação de usuários
- Controle de acesso por perfil
- Proteção de endpoints utilizando JWT

---

# Tecnologias Utilizadas

- Java 25
- Spring Boot 4.0.6
- Spring Web
- Spring Security
- Spring Data JPA
- JWT (java-jwt)
- H2 Database
- Maven
- Lombok
- Bean Validation
- OpenAPI 3.1
- Swagger UI
- JUnit 5
- Mockito
- REST API
- JSON

---

# Arquitetura da Aplicação

A aplicação foi organizada utilizando separação de responsabilidades entre diferentes componentes.

## Controller

Responsável por receber as requisições HTTP e retornar as respostas da API.

Principais controllers:

- `AuthController`
- `VehicleController`

## Service

Responsável pelas regras de negócio da aplicação.

Principais serviços:

- `AuthService`
- `TokenService`
- `VehicleService`
- `SpecificationNormalizationService`
- `CompetitorIntegrationService`

## Security

Responsável pela autenticação e autorização.

Principais componentes:

- `SecurityConfig`
- `JwtAuthenticationFilter`
- `TokenService`

O filtro JWT intercepta as requisições protegidas, valida o token enviado e adiciona o usuário autenticado ao contexto de segurança do Spring.

## Repository

Responsável pelo acesso aos dados através do Spring Data JPA.

Repositories:

- `UserRepository`
- `VehicleRepository`

## Database

O projeto utiliza banco **H2 em memória** para persistência durante a execução.

As principais entidades são:

- `User`
- `Vehicle`

---

# Fluxo de Autenticação

O fluxo de autenticação funciona da seguinte forma:

```text
Cliente
   |
   | POST /auth/login
   v
AuthController
   |
   v
AuthService
   |
   | valida usuário e senha
   v
TokenService
   |
   | gera JWT
   v
Cliente recebe o token
   |
   | Authorization: Bearer <token>
   v
JwtAuthenticationFilter
   |
   | valida JWT
   v
Spring Security
   |
   v
Endpoint protegido
```

O JWT possui tempo de expiração de **2 horas**.

---

# Perfis e Permissões

A aplicação possui dois perfis:

## USER

Usuários comuns podem:

- Listar veículos
- Consultar especificações
- Comparar veículos

## ADMIN

Administradores possuem as permissões de `USER` e também podem:

- Cadastrar veículos
- Atualizar veículos
- Excluir veículos

Resumo:

| Operação | USER | ADMIN |
|---|---:|---:|
| Listar veículos | Sim | Sim |
| Consultar especificações | Sim | Sim |
| Comparar veículos | Sim | Sim |
| Cadastrar veículo | Não | Sim |
| Atualizar veículo | Não | Sim |
| Excluir veículo | Não | Sim |

---

# Cadastro de Usuários

O endpoint público de cadastro cria usuários exclusivamente com o perfil:

```text
USER
```

O cliente não pode escolher o perfil `ADMIN` durante o cadastro.

### Endpoint

```http
POST /auth/register
```

### Exemplo

```json
{
  "nome": "Usuario Teste",
  "email": "user@ford.com",
  "password": "123456"
}
```

### Resposta

```json
{
  "id": 2,
  "nome": "Usuario Teste",
  "email": "user@ford.com",
  "role": "USER"
}
```

Status:

```http
201 Created
```

---

# Administrador Inicial

Para permitir a administração do ambiente de desenvolvimento, a aplicação possui um `AdminInitializer`.

Ao iniciar a aplicação, um administrador é criado caso ainda não exista:

```text
E-mail: admin@ford.com
Senha: 123456
Perfil: ADMIN
```

> Essas credenciais são destinadas exclusivamente ao ambiente acadêmico/de desenvolvimento. Em um ambiente de produção, credenciais administrativas não devem permanecer fixas no código.

---

# Login e JWT

### Endpoint

```http
POST /auth/login
```

### Requisição

```json
{
  "email": "admin@ford.com",
  "password": "123456"
}
```

### Resposta

```json
{
  "token": "<JWT>"
}
```

O token deve ser enviado nos endpoints protegidos através do header:

```http
Authorization: Bearer <JWT>
```

---

# Funcionalidades Implementadas

- Cadastro de usuários
- Login
- Geração de JWT
- Validação de JWT
- Expiração de token
- Autorização por perfil
- Senhas armazenadas utilizando BCrypt
- Endpoints públicos e protegidos
- Consulta de veículos
- Cadastro de veículos
- Consulta dinâmica de especificações
- Comparação entre veículos concorrentes
- Atualização de veículos
- Remoção de veículos
- Tratamento global de exceções
- Respostas de erro estruturadas
- Documentação OpenAPI/Swagger
- Testes automatizados

---

# Endpoints

| Método | Endpoint | Função | Acesso |
|---|---|---|---|
| POST | `/auth/register` | Cadastrar usuário | Público |
| POST | `/auth/login` | Realizar login | Público |
| GET | `/vehicles` | Listar veículos | USER / ADMIN |
| POST | `/vehicles/specifications` | Buscar especificações | USER / ADMIN |
| POST | `/vehicles/compare` | Comparar veículos | USER / ADMIN |
| POST | `/vehicles` | Cadastrar veículo | ADMIN |
| PUT | `/vehicles/{id}` | Atualizar veículo | ADMIN |
| DELETE | `/vehicles/{id}` | Remover veículo | ADMIN |

---

# Principais Status HTTP

A API utiliza códigos HTTP de acordo com o resultado das operações.

| Status | Significado |
|---|---|
| `200 OK` | Operação realizada com sucesso |
| `201 Created` | Recurso criado com sucesso |
| `204 No Content` | Recurso removido com sucesso |
| `400 Bad Request` | Dados inválidos |
| `401 Unauthorized` | Autenticação necessária |
| `403 Forbidden` | Usuário autenticado sem permissão |
| `404 Not Found` | Recurso não encontrado |
| `500 Internal Server Error` | Erro interno inesperado |

---

# GET - Listar Veículos

```http
GET /vehicles
```

Requer autenticação `USER` ou `ADMIN`.

### Exemplo de resposta

```json
[
  {
    "id": 1,
    "marca": "Ford",
    "modelo": "Ranger",
    "versao": "Raptor",
    "motor": "3.0 V6 Biturbo",
    "potencia": "397 cv",
    "torque": "59,4 kgfm",
    "cambio": "Automático 10 marchas",
    "tracao": "4x4",
    "combustivel": "Gasolina",
    "capacidadeCarga": "1012 kg"
  }
]
```

---

# POST - Buscar Especificações

```http
POST /vehicles/specifications
```

Requer autenticação `USER` ou `ADMIN`.

### Requisição

```json
{
  "marca": "Ford",
  "modelo": "Ranger",
  "versao": "Raptor",
  "atributos": [
    "motor",
    "potencia",
    "torque"
  ]
}
```

### Resposta

```json
{
  "marca": "Ford",
  "modelo": "Ranger",
  "versao": "Raptor",
  "especificacoes": {
    "motor": "3.0 V6 Biturbo",
    "potencia": "397 cv",
    "torque": "59,4 kgfm"
  }
}
```

---

# POST - Comparar Veículos

```http
POST /vehicles/compare
```

Requer autenticação `USER` ou `ADMIN`.

### Requisição

```json
{
  "veiculos": [
    {
      "marca": "Ford",
      "modelo": "Ranger",
      "versao": "Raptor"
    },
    {
      "marca": "Toyota",
      "modelo": "Hilux",
      "versao": "GR-Sport"
    }
  ],
  "atributos": [
    "motor",
    "potencia",
    "torque"
  ]
}
```

---

# POST - Cadastrar Veículo

```http
POST /vehicles
```

Requer perfil `ADMIN`.

### Exemplo

```json
{
  "marca": "Ford",
  "modelo": "Maverick",
  "versao": "Lariat",
  "motor": "2.0 Turbo",
  "potencia": "253 cv",
  "torque": "38,7 kgfm",
  "cambio": "Automático",
  "tracao": "AWD",
  "combustivel": "Gasolina",
  "capacidadeCarga": "Teste"
}
```

Resposta:

```http
201 Created
```

---

# PUT - Atualizar Veículo

```http
PUT /vehicles/{id}
```

Requer perfil `ADMIN`.

Retorna:

```http
200 OK
```

quando o veículo é atualizado.

Caso o ID não exista:

```http
404 Not Found
```

---

# DELETE - Remover Veículo

```http
DELETE /vehicles/{id}
```

Requer perfil `ADMIN`.

Quando a exclusão é realizada:

```http
204 No Content
```

Um usuário com perfil `USER` tentando executar essa operação recebe:

```http
403 Forbidden
```

---

# Tratamento de Erros

A aplicação possui tratamento de exceções através de:

- `GlobalExceptionHandler`
- `VehicleNotFoundException`
- Validação de requisições
- Tratamento de acesso não autenticado
- Tratamento de acesso sem permissão

### Exemplo - 404

```json
{
  "error": "Not Found",
  "message": "Veículo não encontrado",
  "timestamp": "2026-05-21T18:00:00",
  "status": 404
}
```

### Exemplo - 401

```json
{
  "error": "Unauthorized",
  "message": "Autenticação necessária",
  "status": 401
}
```

### Exemplo - 403

```json
{
  "error": "Forbidden",
  "message": "Você não possui permissão para acessar este recurso",
  "status": 403
}
```

---

# Swagger / OpenAPI

A API possui documentação interativa utilizando **OpenAPI 3.1 e Swagger UI**.

Com a aplicação em execução, acesse:

```text
http://localhost:8085/swagger-ui/index.html
```

A especificação OpenAPI também está disponível em:

```text
http://localhost:8085/v3/api-docs
```

O Swagger apresenta os endpoints separados nas categorias:

- Autenticação
- Veículos

Os códigos HTTP e descrições das operações também estão documentados.

---

# Autenticação pelo Swagger

Para utilizar endpoints protegidos:

1. Realize login em `POST /auth/login`.
2. Copie o token retornado.
3. Clique no botão **Authorize**.
4. Informe o JWT no campo `bearerAuth`.
5. Confirme em **Authorize**.
6. Execute os endpoints protegidos.

O Swagger adicionará automaticamente:

```http
Authorization: Bearer <JWT>
```

às requisições.

---

# Banco de Dados

O projeto utiliza banco H2 em memória.

### Configuração

```properties
spring.datasource.url=jdbc:h2:mem:forddb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

Como o banco está em memória, os dados são recriados quando a aplicação é reiniciada.

---

# Testes Automatizados

Foram implementados testes automatizados utilizando **JUnit 5 e Mockito**.

As principais classes testadas são:

### AuthServiceTest

Testa:

- Cadastro de usuário
- E-mail já cadastrado
- Login com sucesso
- Login com senha incorreta

### TokenServiceTest

Testa:

- Geração e validação do JWT
- Rejeição de token inválido
- Presença do perfil no token

### VehicleServiceTest

Testa:

- Cadastro de veículo
- Consulta de especificações
- Veículo não encontrado
- Atualização de veículo
- Exclusão de veículo
- Comparação de veículos

### FordApplicationTests

Testa a inicialização do contexto da aplicação.

Resultado da execução completa:

```text
Tests passed: 14 of 14 tests
```

---

# Como Executar o Projeto

## Pré-requisitos

- Java 25
- Maven
- Git

## Clonar o projeto

```bash
git clone https://github.com/roger0n/Challenge-ford-soa.git
```

Entre na pasta:

```bash
cd Challenge-ford-soa
```

## Instalar e testar

```bash
mvn clean test
```

## Executar aplicação

```bash
mvn spring-boot:run
```

A aplicação será iniciada em:

```text
http://localhost:8085
```

---

# Configuração do JWT

A chave utilizada para assinatura do JWT pode ser definida através da variável de ambiente:

```text
JWT_SECRET
```

Para desenvolvimento, a aplicação possui uma chave padrão configurada.

Em ambientes de produção, deve ser utilizada uma chave segura através de variável de ambiente, sem armazenar o segredo diretamente no código-fonte.

---

# Segurança

A aplicação utiliza:

- Spring Security
- JWT
- BCrypt para armazenamento das senhas
- Autenticação stateless
- Controle de acesso por perfil
- Proteção de endpoints
- Expiração de token

Os endpoints `/auth/**` e da documentação Swagger são públicos.

Os endpoints de veículos são protegidos de acordo com o perfil do usuário.

---

# Considerações Finais

O projeto aplica conceitos de:

- Arquitetura Orientada a Serviços (SOA)
- Web Services
- APIs REST
- Maturidade REST Nível 2
- Autenticação e autorização
- JWT
- Controle de acesso baseado em perfis
- Separação de responsabilidades
- Tratamento de exceções
- Persistência de dados
- Testes automatizados
- OpenAPI / Swagger
- Inteligência Competitiva Automotiva

A solução permite consultas e comparações padronizadas de veículos concorrentes, além do gerenciamento protegido dos dados através de autenticação e autorização.

---

# Integrantes

- 3ESA- Augusto Ferreira / RM 557709
- 3ESA- Heitor Prestes / RM 554823
- 3ESR- Lucca Ribeiro / RM 556668

---
