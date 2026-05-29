package com.Senai.Filmes.DTO.Response;

public record AuthResponse(
        String token,
        String nome,
        String cargo
) {}
