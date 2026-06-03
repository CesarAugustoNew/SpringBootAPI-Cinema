# 🎬 Filmes API

API REST desenvolvida com Spring Boot para gerenciamento de filmes e salas de cinema, utilizando PostgreSQL hospedado na Microsoft Azure para persistência de dados.

<img width="1280" height="591" alt="image" src="https://github.com/user-attachments/assets/7506bba4-a058-428c-9b40-cf4ee2593223" />

---

## 📖 Sobre o Projeto

A Filmes API foi criada para fornecer uma solução backend para gerenciamento de catálogos de filmes e salas de exibição.

O projeto segue boas práticas de desenvolvimento utilizando arquitetura em camadas, separação de responsabilidades e documentação automática da API através do Swagger/OpenAPI.

---

# 🚀 Tecnologias Utilizadas

### Backend

- Java 25
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Hibernate ORM
- Bean Validation

### Banco de Dados

- PostgreSQL
- Azure Database for PostgreSQL

### Documentação

- Swagger UI

### Ferramentas

- Maven
- Lombok
- Git

---

# 🏗 Arquitetura da Aplicação

O projeto utiliza arquitetura em camadas:

```text
┌─────────────────┐
│   Controller    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Service      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Repository    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ PostgreSQL Azure│
└─────────────────┘
```

### Controller

Responsável pelos endpoints REST da aplicação.

Exemplos:

```http
GET /filmes
POST /filmes
PUT /filmes/{id}
DELETE /filmes/{id}
```

### Service

Camada responsável pelas regras de negócio.

Funções:

- Validação de dados
- Processamento das requisições
- Integração entre Controller e Repository

### Repository

Camada de acesso ao banco de dados utilizando Spring Data JPA.

Responsável por:

- CRUD
- Consultas
- Persistência das entidades

### DTO

Implementação do padrão DTO (Data Transfer Object) para:

- Transferência segura de dados
- Redução de acoplamento
- Controle das informações expostas pela API

---

# 📂 Estrutura do Projeto

```text
src/main/java/com/Senai/Filmes

├── Controller
│   ├── FilmeController
│   └── SalaController
│
├── DTO
│   ├── FilmeDTO
│   └── SalaDTO
│
├── Model
│   ├── Filme
│   └── Sala
│
├── Repository
│   ├── FilmeRepository
│   └── SalaRepository
│
├── Service
│   ├── FilmeService
│   └── SalaService
│
└── FilmesApplication.java
```

---

# ☁️ Infraestrutura

O banco de dados está hospedado na plataforma Microsoft Azure utilizando o serviço Azure Database for PostgreSQL.

---

# 🔌 Principais Funcionalidades

## Filmes

- Cadastrar filme
- Buscar filme por ID
- Listar filmes
- Atualizar filme
- Excluir filme

## Salas

- Cadastrar sala
- Buscar sala por ID
- Listar salas
- Atualizar sala
- Excluir sala

---

# 🔒 Segurança

A dependência Spring Security já está prevista no projeto e pode ser habilitada futuramente para:

- Autenticação JWT
- Controle de acesso por perfis
- Proteção dos endpoints REST

---

# 👨‍💻 Autor

## Cesar Augusto

### Tecnologias

- Java
- Spring Boot
- PostgreSQL
- Azure Cloud
- APIs REST

GitHub:

https://github.com/CesarAugustoNew
