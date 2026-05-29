package com.Senai.Filmes.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "cSessoes")
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "filme_id")
    @NotNull
    private Filme filme;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    @NotNull
    private Sala sala;

    @NotNull(message = "O horário do inicio é obrigatório")
    private LocalDateTime inicio;

    @NotNull(message = "O horário do filme é obrigatório")
    private LocalDateTime fim;

    @NotNull(message = "O preço é obrigatório")
    private BigDecimal preco;
}








