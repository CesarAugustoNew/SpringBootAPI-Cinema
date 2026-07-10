package com.Senai.Filmes.Service;

import com.Senai.Filmes.DTO.Request.ReservaRequest;
import com.Senai.Filmes.DTO.Response.AssentoResponse;
import com.Senai.Filmes.DTO.Response.FilmeResponse;
import com.Senai.Filmes.DTO.Response.ReservaResponse;
import com.Senai.Filmes.DTO.Response.SalaResponse;
import com.Senai.Filmes.DTO.Response.SessaoResponse;
import com.Senai.Filmes.Model.Assento;
import com.Senai.Filmes.Model.Enums.StatusReserva;
import com.Senai.Filmes.Model.Filme;
import com.Senai.Filmes.Model.Reserva;
import com.Senai.Filmes.Model.ReservaAssento;
import com.Senai.Filmes.Model.Sala;
import com.Senai.Filmes.Model.Sessao;
import com.Senai.Filmes.Model.Usuario;
import com.Senai.Filmes.Repository.IAssentoRepository;
import com.Senai.Filmes.Repository.IReservaAssentoRepository;
import com.Senai.Filmes.Repository.IReservaRepository;
import com.Senai.Filmes.Repository.ISessaoRepository;
import com.Senai.Filmes.Repository.IUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private ISessaoRepository sessaoRepository;

    @Autowired
    private IAssentoRepository assentoRepository;

    @Autowired
    private IReservaAssentoRepository reservaAssentoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    /*
      Cria uma reserva para o usuário logado (identificado pelo e-mail
      extraído do token JWT). Antes de confirmar, verifica assento a
      assento se algum já está ocupado por outra reserva ATIVA na
      mesma sessão — evita "overbooking" da mesma poltrona.
    */
    public ReservaResponse criar(String emailUsuario, ReservaRequest request) {
        if (request.assentoIds() == null || request.assentoIds().isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos um assento");
        }

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Sessao sessao = sessaoRepository.findById(request.sessaoId())
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setSessao(sessao);
        reserva.setStatus(StatusReserva.ATIVA);

        List<ReservaAssento> reservaAssentos = request.assentoIds().stream().map(assentoId -> {
            Assento assento = assentoRepository.findById(assentoId)
                    .orElseThrow(() -> new EntityNotFoundException("Assento não encontrado"));

            boolean ocupado = reservaAssentoRepository.isAssentoOcupado(
                    assentoId, request.sessaoId(), StatusReserva.ATIVA);
            if (ocupado) {
                throw new IllegalStateException(
                        "O assento " + assento.getFileira() + assento.getNumero() + " já está reservado");
            }

            ReservaAssento reservaAssento = new ReservaAssento();
            reservaAssento.setReserva(reserva);
            reservaAssento.setAssento(assento);
            return reservaAssento;
        }).toList();

        reserva.setAssentos(reservaAssentos);

        return toResponse(reservaRepository.save(reserva));
    }

    public List<ReservaResponse> listarMinhas(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return reservaRepository.findByUsuarioId(usuario.getId()).stream().map(this::toResponse).toList();
    }

    public List<ReservaResponse> listarTodas() {
        return reservaRepository.findAll().stream().map(this::toResponse).toList();
    }

    /*
      Cancela uma reserva. Um usuário comum só pode cancelar a própria
      reserva; um admin pode cancelar qualquer uma.
    */
    public void cancelar(UUID id, String emailUsuario, boolean isAdmin) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));

        if (!isAdmin && !reserva.getUsuario().getEmail().equalsIgnoreCase(emailUsuario)) {
            throw new AccessDeniedException("Você não pode cancelar uma reserva de outro usuário");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    private ReservaResponse toResponse(Reserva reserva) {
        Sessao sessao = reserva.getSessao();
        Filme filme = sessao.getFilme();
        Sala sala = sessao.getSala();

        FilmeResponse filmeResponse = new FilmeResponse(
                filme.getId(), filme.getTitulo(), filme.getDescricao(),
                filme.getUrlPoster(), filme.getGenero(), filme.getDuracaoMinutos()
        );

        SalaResponse salaResponse = new SalaResponse(sala.getId(), sala.getNome(), sala.getTotalAssentos());

        SessaoResponse sessaoResponse = new SessaoResponse(
                sessao.getId(), filmeResponse, salaResponse,
                sessao.getInicio(), sessao.getFim(), sessao.getPreco()
        );

        List<AssentoResponse> assentos = reserva.getAssentos().stream()
                .map(ra -> new AssentoResponse(
                        ra.getAssento().getId(),
                        ra.getAssento().getFileira(),
                        ra.getAssento().getNumero(),
                        false
                ))
                .toList();

        return new ReservaResponse(reserva.getId(), sessaoResponse, assentos, reserva.getStatus(), reserva.getCriadoEm());
    }
}
