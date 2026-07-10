package com.Senai.Filmes.Service;

import com.Senai.Filmes.DTO.Request.SessaoRequest;
import com.Senai.Filmes.DTO.Response.AssentoResponse;
import com.Senai.Filmes.DTO.Response.FilmeResponse;
import com.Senai.Filmes.DTO.Response.SalaResponse;
import com.Senai.Filmes.DTO.Response.SessaoResponse;
import com.Senai.Filmes.Model.Assento;
import com.Senai.Filmes.Model.Enums.StatusReserva;
import com.Senai.Filmes.Model.Filme;
import com.Senai.Filmes.Model.Sala;
import com.Senai.Filmes.Model.Sessao;
import com.Senai.Filmes.Repository.IAssentoRepository;
import com.Senai.Filmes.Repository.IFilmeRepository;
import com.Senai.Filmes.Repository.IReservaAssentoRepository;
import com.Senai.Filmes.Repository.ISalaRepository;
import com.Senai.Filmes.Repository.ISessaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SessaoService {

    @Autowired
    private ISessaoRepository sessaoRepository;

    @Autowired
    private IFilmeRepository filmeRepository;

    @Autowired
    private ISalaRepository salaRepository;

    @Autowired
    private IAssentoRepository assentoRepository;

    @Autowired
    private IReservaAssentoRepository reservaAssentoRepository;

    public List<SessaoResponse> listarPorData(LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = inicioDia.plusDays(1);
        return sessaoRepository.findByData(inicioDia, fimDia).stream().map(this::toResponse).toList();
    }

    public List<SessaoResponse> listarPorFilme(UUID filmeId) {
        return sessaoRepository.findByFilmeId(filmeId).stream().map(this::toResponse).toList();
    }

    public SessaoResponse buscarPorId(UUID id) {
        Sessao sessao = sessaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));
        return toResponse(sessao);
    }

    /*
      Lista todos os assentos da sala da sessão, marcando quais já
      estão ocupados por outra reserva ATIVA para essa mesma sessão.
    */
    public List<AssentoResponse> listarAssentos(UUID sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        List<Assento> assentos = assentoRepository.findBySalaId(sessao.getSala().getId());
        Set<UUID> ocupados = Set.copyOf(
                reservaAssentoRepository.findAssentosOcupadosBySessaoId(sessaoId, StatusReserva.ATIVA)
        );

        return assentos.stream()
                .map(a -> new AssentoResponse(a.getId(), a.getFileira(), a.getNumero(), !ocupados.contains(a.getId())))
                .toList();
    }

    public SessaoResponse criar(SessaoRequest request) {
        Filme filme = filmeRepository.findById(request.filmeId())
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));
        Sala sala = salaRepository.findById(request.salaId())
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada"));

        Sessao sessao = new Sessao();
        sessao.setFilme(filme);
        sessao.setSala(sala);
        sessao.setInicio(request.inicio());
        sessao.setFim(request.fim());
        sessao.setPreco(request.preco());

        return toResponse(sessaoRepository.save(sessao));
    }

    public void deletar(UUID id) {
        Sessao sessao = sessaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));
        sessaoRepository.delete(sessao);
    }

    private SessaoResponse toResponse(Sessao sessao) {
        Filme filme = sessao.getFilme();
        FilmeResponse filmeResponse = new FilmeResponse(
                filme.getId(),
                filme.getTitulo(),
                filme.getDescricao(),
                filme.getUrlPoster(),
                filme.getGenero(),
                filme.getDuracaoMinutos()
        );

        Sala sala = sessao.getSala();
        SalaResponse salaResponse = new SalaResponse(
                sala.getId(),
                sala.getNome(),
                sala.getTotalAssentos()
        );

        return new SessaoResponse(
                sessao.getId(),
                filmeResponse,
                salaResponse,
                sessao.getInicio(),
                sessao.getFim(),
                sessao.getPreco()
        );
    }
}
