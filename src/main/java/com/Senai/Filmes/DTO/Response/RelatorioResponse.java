package com.Senai.Filmes.DTO.Response;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioResponse(
        long totalReservas,
        BigDecimal totalReceita,
        List<FilmeTotais> filmes
) {
    public record FilmeTotais(String nomeFilme, long totalReservas) {}
}
