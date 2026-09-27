<div align="center">

# CineSenai — API

Back-end de um sistema de cinema (catálogo de filmes, salas, sessões, reservas de assento e painel administrativo), construído em **Java** com **Spring Boot**.

![Java](https://img.shields.io/badge/Java-24-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![Database](https://img.shields.io/badge/Database-PostgreSQL-336791?style=flat-square&logo=postgresql&logoColor=white)
![Deploy](https://img.shields.io/badge/Deploy-Render-46E3B7?style=flat-square&logo=render&logoColor=white)
![Docker](https://img.shields.io/badge/Container-Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Docs-Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black)

[**🔗 API publicada**](#) · [**📘 Documentação (Swagger)**](#) · [**🖥️ Repositório do front-end**](#)

</div>

---

## Sobre este projeto

Esta é a API REST do CineSenai: guarda e organiza filmes, salas, sessões de exibição e as reservas de assento de cada usuário, além de um painel só para administradores. Ela não tem tela nenhuma — só recebe pedidos (do front-end React, ou de qualquer outro programa) e responde em formato de dados (JSON).

O front-end que consome esta API é um projeto separado, feito em React ([link do repositório](#)).

## Funcionalidades

- Cadastro de usuário e login com senha, com dois papéis: **usuário comum** e **administrador**
- Catálogo de filmes (título, descrição, gênero, duração, pôster)
- Cadastro de salas, com geração automática dos assentos por fileira/quantidade
- Agendamento de sessões de exibição por filme e sala
- Reserva de assentos por sessão, com verificação de assentos já ocupados
- Cancelamento de reserva (o próprio usuário cancela as suas; administrador cancela qualquer uma)
- Painel administrativo: listagem geral de reservas, relatório de receita/vendas por filme, promoção de usuário a administrador

## Tecnologias usadas

| Item | Tecnologia |
|---|---|
| Linguagem | Java 24 |
| Framework | Spring Boot |
| Acesso ao banco | Spring Data JPA (Hibernate) |
| Autenticação | Spring Security + JWT |
| Banco de dados | PostgreSQL |
| Documentação | Swagger / OpenAPI |
| Empacotamento | Docker |
| Publicação | Render |

## Como a API é organizada

Segue o modelo em camadas mais comum para esse tipo de projeto:

- **Controller** — recebe a requisição HTTP (ex.: "reservar um assento") e devolve a resposta.
- **Service** — contém as regras de negócio (ex.: "não deixar reservar um assento que já está ocupado").
- **Repository** — conversa com o banco de dados.
- **DTO** (Request/Response) — formato dos dados que entram e saem da API, sem expor as entidades internas diretamente.

## Principais endpoints

| Recurso | Método | Rota | Acesso |
|---|---|---|---|
| Autenticação | POST | `/api/auth/cadastro`, `/api/auth/login` | Público |
| Filmes | GET | `/api/filmes`, `/api/filmes/{id}` | Público |
| Filmes | POST/PUT/DELETE | `/api/filmes/...` | Administrador |
| Filmes | POST/DELETE | `/api/filmes/{id}/imagem` | Administrador |
| Salas | GET | `/api/salas`, `/api/salas/{id}` | Público |
| Salas | POST/DELETE | `/api/salas/...` | Administrador |
| Sessões | GET | `/api/sessoes`, `/api/sessoes/{id}`, `/api/sessoes/{id}/assentos` | Público |
| Sessões | POST/DELETE | `/api/sessoes/...` | Administrador |
| Reservas | POST | `/api/reservas` | Autenticado |
| Reservas | GET | `/api/reservas/minhas` | Autenticado |
| Reservas | DELETE | `/api/reservas/{id}` | Dono da reserva ou Administrador |
| Admin | GET | `/api/admin/reservas`, `/api/admin/relatorios` | Administrador |
| Admin | PATCH | `/api/admin/usuarios/{id}/promover` | Administrador |

## Segurança

O login funciona com **JWT** (JSON Web Token): ao entrar com e-mail e senha, o usuário recebe um token que deve ser enviado nas próximas requisições, provando quem ele é sem precisar mandar a senha de novo. As rotas de administrador (cadastro de filmes, salas, sessões, relatórios) exigem que o token pertença a um usuário com papel **ADMIN**.

## Pôsteres dos filmes

Os pôsteres enviados pelo administrador são convertidos para **base64** e guardados direto no banco de dados, junto com as demais informações do filme — não ficam salvos como arquivo separado no servidor. Isso evita que a imagem se perca quando o serviço reinicia (algo comum em provedores de hospedagem com disco não permanente).

## Deploy

Publicada no **Render**, com banco de dados **PostgreSQL** também hospedado lá. A aplicação é empacotada com **Docker**, o que permite publicá-la de forma consistente em praticamente qualquer provedor de nuvem.
