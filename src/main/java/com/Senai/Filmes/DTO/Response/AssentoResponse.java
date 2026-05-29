package com.Senai.Filmes.DTO.Response;

import java.util.UUID;

public record AssentoResponse(
        UUID id,
        String fileira,
        Integer numero,
        Boolean disponivel
) {}
