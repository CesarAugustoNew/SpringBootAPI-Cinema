# 🎬 CineSenai — API (SpringBootAPI-Cinema)

API RESTful em **Java + Spring Boot** para um sistema de cinema: catálogo de filmes, salas, sessões, reservas de assentos e um painel administrativo. Serve de back-end para o front-end **CineSenai-Final** (React + Vite).

---

## 🛠 Tecnologias

- Java 21
- Spring Boot 4 / Spring Security 7
- Spring Web, Spring Data JPA (Hibernate)
- JWT (autenticação stateless)
- MySQL
- Maven
- Lombok
- Swagger / OpenAPI (springdoc)

---

## ▶️ Como rodar

### Pré-requisitos
- JDK 21+
- Maven (ou usar o `mvnw` incluso no projeto)
- MySQL rodando localmente na porta `3306`

### 1. Criar o banco de dados
```sql
CREATE DATABASE cinema;
```
As tabelas são criadas automaticamente na primeira execução (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Configurar credenciais do banco
Em `src/main/resources/application.properties`, ajuste usuário/senha do MySQL se necessário:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cinema?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=Cesar123
```

### 3. Subir a aplicação
```bash
./mvnw spring-boot:run
```
A API sobe em `http://localhost:8080`.

### 4. Usuário admin inicial
Na primeira execução, um usuário administrador é criado automaticamente:
- **E-mail:** `admin@cinemasenai.com`
- **Senha:** `Admin@134`

(configuráveis via `admin.email` e `admin.senha` em `application.properties`, se quiser sobrescrever)

---

## 📖 Documentação interativa (Swagger)

Com a aplicação rodando, acesse:
```
http://localhost:8080/swagger-ui.html
```

---

## 🚀 Funcionalidades

- Cadastro/login de usuários com JWT
- Controle de acesso por cargo (`USUARIO` / `ADMIN`) via Spring Security
- CRUD de filmes, incluindo upload/remoção de pôster (salvo localmente em `uploads/posters` e servido em `/uploads/**`)
- CRUD de salas (geração automática dos assentos por fileira/quantidade)
- Agendamento e remoção de sessões de exibição
- Reserva de assentos por sessão, com verificação de assentos já ocupados
- Cancelamento de reservas (usuário cancela as próprias; admin cancela qualquer uma)
- Painel admin: listagem geral de reservas, relatório de receita/vendas por filme, promoção de usuário a admin

---

## 📂 Estrutura do projeto

```
src/main/java/com/Senai/Filmes
├── Controller      # endpoints REST
├── Service         # regras de negócio
├── Repository      # acesso a dados (Spring Data JPA)
├── Model           # entidades JPA
│   └── Enums
├── DTO
│   ├── Request
│   └── Response
├── Security        # JWT, filtros e configuração do Spring Security
└── Config          # inicialização de dados, upload de arquivos, etc.
```

---

## 🔌 Principais endpoints

| Recurso   | Método | Rota                                  | Acesso         |
|-----------|--------|----------------------------------------|----------------|
| Auth      | POST   | `/api/auth/cadastro`                   | Público        |
| Auth      | POST   | `/api/auth/login`                      | Público        |
| Filmes    | GET    | `/api/filmes`, `/api/filmes/{id}`      | Público        |
| Filmes    | POST/PUT/DELETE | `/api/filmes...`               | ADMIN          |
| Filmes    | POST/DELETE | `/api/filmes/{id}/imagem`          | ADMIN          |
| Salas     | GET    | `/api/salas`, `/api/salas/{id}`        | Público        |
| Salas     | POST/DELETE | `/api/salas...`                    | ADMIN          |
| Sessões   | GET    | `/api/sessoes?data=` ou `?filmeId=`    | Público        |
| Sessões   | GET    | `/api/sessoes/{id}`, `/{id}/assentos`  | Público        |
| Sessões   | POST/DELETE | `/api/sessoes...`                  | ADMIN          |
| Reservas  | POST   | `/api/reservas`                        | Autenticado    |
| Reservas  | GET    | `/api/reservas/minhas`                 | Autenticado    |
| Reservas  | DELETE | `/api/reservas/{id}`                   | Dono ou ADMIN  |
| Admin     | GET    | `/api/admin/reservas`, `/relatorios`   | ADMIN          |
| Admin     | PATCH  | `/api/admin/usuarios/{id}/promover`    | ADMIN          |

---

