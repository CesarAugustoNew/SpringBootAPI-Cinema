package com.Senai.Filmes.Model;


import com.Senai.Filmes.Model.Enums.StatusReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cReserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sessao_id")
    private Sessao sessao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaAssento> assentos = new ArrayList<>();


}
