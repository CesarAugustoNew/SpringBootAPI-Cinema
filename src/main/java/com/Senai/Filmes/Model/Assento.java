package com.Senai.Filmes.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "cAssento")

public class Assento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @NotBlank(message = "A assento é obrigatório")
    private String fileira;

    @NotNull(message = "O numero do assento é obrigatório")
    private Integer numero;

}
