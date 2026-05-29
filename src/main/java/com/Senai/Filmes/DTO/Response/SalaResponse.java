package com.Senai.Filmes.DTO.Response;

import java.util.UUID;

public record SalaResponse(
        UUID id,
        String nome,
        Integer totalAssentos
) {
}
