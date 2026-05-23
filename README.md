# Ford Competitive Intelligence API

## Descrição

Este projeto foi desenvolvido como solução para o desafio proposto pela Ford na disciplina de Arquitetura Orientada a Serviços e Web Services.

O objetivo da aplicação é permitir a consulta e comparação de especificações técnicas de veículos concorrentes através de uma API RESTful.

A solução permite que o usuário informe:

- Marca
- Modelo
- Versão
- Lista de atributos desejados

E receba uma resposta padronizada contendo apenas as especificações solicitadas.

---

# Objetivo do Projeto

O projeto busca resolver o problema de Inteligência Competitiva Automotiva, permitindo:

- Consulta padronizada de veículos concorrentes
- Comparação entre veículos
- Organização consistente dos dados
- Retorno estruturado em JSON
- Busca dinâmica de atributos

---

# Tecnologias Utilizadas

- Java 25
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- Lombok
- REST API
- JSON

---

## Camadas do Projeto

### Controller
Responsável por receber as requisições HTTP.

### Service
Responsável pelas regras de negócio e pela integração e comunicação com a camada de persistência.

### Repository
Responsável pelo acesso ao banco de dados.

### Database
Banco H2 utilizado para persistência dos dados.

---

# Funcionalidades Implementadas

- Consulta de veículos
- Consulta dinâmica de especificações
- Comparação entre veículos concorrentes
- Atualização de veículos
- Remoção de veículos
- Tratamento global de exceções
- Respostas padronizadas
- Integração RESTful

---

# Métodos HTTP Implementados

| Método | Endpoint | Função |
|---|---|---|
| GET | /vehicles | Listar veículos |
| POST | /vehicles/specifications | Buscar especificações |
| POST | /vehicles/compare | Comparar veículos |
| PUT | /vehicles/{id} | Atualizar veículo |
| DELETE | /vehicles/{id} | Remover veículo |

---

# Endpoints e Testes Realizados

## GET - Listar Veículos

### Endpoint

```http
GET http://localhost:8085/vehicles
```

### Resposta Esperada

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

### Endpoint

```http
POST http://localhost:8085/vehicles/specifications
```

### Body da Requisição

```json
{
  "marca":"Ford",
  "modelo":"Ranger",
  "versao":"Raptor",
  "atributos":[
    "motor",
    "potencia",
    "torque"
  ]
}
```

### Resposta Esperada

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

### Endpoint

```http
POST http://localhost:8085/vehicles/compare
```

### Body da Requisição

```json
{
  "veiculos":[
    {
      "marca":"Ford",
      "modelo":"Ranger",
      "versao":"Raptor"
    },
    {
      "marca":"Toyota",
      "modelo":"Hilux",
      "versao":"GR-Sport"
    }
  ],
  "atributos":[
    "motor",
    "potencia",
    "torque"
  ]
}
```

### Resposta Esperada

```json
[
  {
    "veiculo": "Ford Ranger Raptor",
    "especificacoes": {
      "motor": "3.0 V6 Biturbo",
      "potencia": "397 cv",
      "torque": "59,4 kgfm"
    }
  },
  {
    "veiculo": "Toyota Hilux GR-Sport",
    "especificacoes": {
      "motor": "2.8 Turbo Diesel",
      "potencia": "224 cv",
      "torque": "55 kgfm"
    }
  }
]
```

---

# PUT - Atualizar Veículo

### Endpoint

```http
PUT http://localhost:8085/vehicles/1
```

### Body da Requisição

```json
{
  "marca": "Ford",
  "modelo": "Ranger",
  "versao": "Raptor",
  "motor": "3.0 V6 Atualizado",
  "potencia": "405 cv",
  "torque": "60 kgfm",
  "cambio": "Automático 10 marchas",
  "tracao": "4x4",
  "combustivel": "Gasolina",
  "capacidadeCarga": "1012 kg"
}
```

### Resposta Esperada

```json
{
  "id": 1,
  "marca": "Ford",
  "modelo": "Ranger",
  "versao": "Raptor",
  "motor": "3.0 V6 Atualizado",
  "potencia": "405 cv",
  "torque": "60 kgfm",
  "cambio": "Automático 10 marchas",
  "tracao": "4x4",
  "combustivel": "Gasolina",
  "capacidadeCarga": "1012 kg"
}
```

---

# DELETE - Remover Veículo

### Endpoint

```http
DELETE http://localhost:8085/vehicles/1
```

### Resposta Esperada

```http
204 No Content
```

---

# Tratamento de Erros

A aplicação possui tratamento global de exceções utilizando:

- GlobalExceptionHandler
- Exceptions personalizadas
- Responses padronizadas

## Exemplo de erro

### Requisição

```json
{
  "marca":"Ford",
  "modelo":"Ka",
  "versao":"Teste",
  "atributos":[
    "motor"
  ]
}
```

### Resposta

```json
{
  "error": "Not Found",
  "message": "Veículo não encontrado",
  "timestamp": "2026-05-21T18:00:00",
  "status": 404
}
```

---

# Banco de Dados

O projeto utiliza o banco H2 em memória.

## Configuração

```properties
spring.datasource.url=jdbc:h2:mem:forddb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

---

# Como Executar o Projeto

## Clonar o projeto

```bash
git clone <repositorio>
```

## Instalar dependências

```bash
mvn clean install
```

## Executar aplicação

```bash
mvn spring-boot:run
```

---

# Porta da Aplicação

```text
http://localhost:8085
```

---

# Considerações Finais

O projeto foi desenvolvido aplicando conceitos de:

- Arquitetura Orientada a Serviços (SOA)
- APIs RESTful
- Separação de responsabilidades
- Tratamento de exceções
- Integração por Web Services
- Persistência de dados
- Inteligência Competitiva Automotiva

A solução permite consultas e comparações padronizadas de veículos concorrentes.

---
# Integrantes

- Augusto Ferreira Rogel de Souza / RM 557709
- Heitor Prestes / RM 554823
- Lucca Ribeiro / RM 556668

---

