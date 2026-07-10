package com.Senai.Filmes.Service;

import com.Senai.Filmes.DTO.Response.RelatorioResponse;
import com.Senai.Filmes.Model.Enums.Cargo;
import com.Senai.Filmes.Model.Enums.StatusReserva;
import com.Senai.Filmes.Model.Reserva;
import com.Senai.Filmes.Model.Usuario;
import com.Senai.Filmes.Repository.IReservaRepository;
import com.Senai.Filmes.Repository.IUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    /*
      Considera apenas reservas ATIVAS (canceladas não contam nem para
      receita nem para o total de ingressos vendidos).
    */
    public RelatorioResponse gerarRelatorio() {
        List<Reserva> reservasAtivas = reservaRepository.findAll().stream()
                .filter(r -> r.getStatus() == StatusReserva.ATIVA)
                .toList();

        long totalReservas = reservasAtivas.size();

        BigDecimal totalReceita = reservasAtivas.stream()
                .map(r -> r.getSessao().getPreco().multiply(BigDecimal.valueOf(r.getAssentos().size())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> reservasPorFilme = new LinkedHashMap<>();
        for (Reserva reserva : reservasAtivas) {
            String nomeFilme = reserva.getSessao().getFilme().getTitulo();
            reservasPorFilme.merge(nomeFilme, 1L, Long::sum);
        }

        List<RelatorioResponse.FilmeTotais> filmeTotais = reservasPorFilme.entrySet().stream()
                .map(e -> new RelatorioResponse.FilmeTotais(e.getKey(), e.getValue()))
                .toList();

        return new RelatorioResponse(totalReservas, totalReceita, filmeTotais);
    }

    public void promoverUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        usuario.setCargo(Cargo.ADMIN);
        usuarioRepository.save(usuario);
    }
}
