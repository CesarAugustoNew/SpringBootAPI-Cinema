package com.Senai.Filmes.Model;


import com.Senai.Filmes.Model.Enums.GeneroFilme;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Entity
@Data
@Table(name = "cFilmes")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O titulo é obrigatorio")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String urlPoster;

    @NotNull(message = "O campo genero é obrigatorio")
    @Enumerated(EnumType.STRING)
    private GeneroFilme genero;

    @NotNull(message = "O campo minutos é obrigatorio")
    @Min(value = 1, message = "A duração deve ser maior que 0")
    private Integer duracaoMinutos;

}
