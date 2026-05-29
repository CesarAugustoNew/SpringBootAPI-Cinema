package com.Senai.Filmes.DTO.Request;

public record SalaRequest(
        String nome,
        String totalAssemtos,
        Integer fileiras,
        Integer assentosFileira
) {}
