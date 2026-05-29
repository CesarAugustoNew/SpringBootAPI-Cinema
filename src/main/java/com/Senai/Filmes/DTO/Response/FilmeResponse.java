package com.Senai.Filmes.DTO.Response;

import com.Senai.Filmes.Model.Enums.GeneroFilme;

import java.util.UUID;

public record FilmeResponse(
        UUID id,
        String titulo,
        String descricao,
        String urlPoster,
        GeneroFilme genero,
        Integer duracaoMinutos
) {}
