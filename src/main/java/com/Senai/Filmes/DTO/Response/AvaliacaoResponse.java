package com.Senai.Filmes.DTO.Response;

import java.time.LocalDateTime;
import java.util.UUID;

// Os nomes dos campos (usuarioNome, criadoEm...) são exatamente os que o
// front-end espera em DetalheFilme.jsx.
public record AvaliacaoResponse(
        UUID id,
        Integer nota,
        String comentario,
        String usuarioNome,
        LocalDateTime criadoEm
) {
}
