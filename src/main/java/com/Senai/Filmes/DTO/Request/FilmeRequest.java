package com.Senai.Filmes.DTO.Request;

import com.Senai.Filmes.Model.Enums.GeneroFilme;

import java.util.UUID;

public record FilmeRequest(
        UUID id,
        String titulo,
        String descricao,
        String urlPoster,
        GeneroFilme genero,
        Integer duracaoMinutos
) {}

