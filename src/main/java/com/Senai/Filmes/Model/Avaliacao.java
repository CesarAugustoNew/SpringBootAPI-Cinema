package com.Senai.Filmes.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/*
  Avaliação (nota + comentário) que um usuário dá a um filme.

  A restrição de unicidade (filme_id + usuario_id) garante que cada
  usuário tenha no máximo UMA avaliação por filme. Se ele avaliar de
  novo, a avaliação anterior é atualizada (veja AvaliacaoService).
*/
@Data
@NoArgsConstructor
@Entity
@Table(
        name = "cAvaliacao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"filme_id", "usuario_id"})
)
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "filme_id")
    private Filme filme;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer nota;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @CreationTimestamp
    private LocalDateTime criadoEm;
}
