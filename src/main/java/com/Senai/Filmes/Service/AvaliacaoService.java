package com.Senai.Filmes.Service;

import com.Senai.Filmes.DTO.Request.AvaliacaoRequest;
import com.Senai.Filmes.DTO.Response.AvaliacaoResponse;
import com.Senai.Filmes.Model.Avaliacao;
import com.Senai.Filmes.Model.Filme;
import com.Senai.Filmes.Model.Usuario;
import com.Senai.Filmes.Repository.IAvaliacaoRepository;
import com.Senai.Filmes.Repository.IFilmeRepository;
import com.Senai.Filmes.Repository.IUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AvaliacaoService {

    private static final int TAMANHO_MAX_COMENTARIO = 1000;

    @Autowired
    private IAvaliacaoRepository avaliacaoRepository;

    @Autowired
    private IFilmeRepository filmeRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    public List<AvaliacaoResponse> listarPorFilme(UUID filmeId) {
        if (!filmeRepository.existsById(filmeId)) {
            throw new EntityNotFoundException("Filme não encontrado");
        }
        return avaliacaoRepository.findByFilmeIdOrderByCriadoEmDesc(filmeId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /*
      Salva a avaliação do usuário logado (identificado pelo e-mail do
      token JWT). Se ele já avaliou este filme, a avaliação existente é
      atualizada em vez de criar uma duplicada.
    */
    public AvaliacaoResponse salvar(UUID filmeId, String emailUsuario, AvaliacaoRequest request) {
        if (request == null || request.nota() == null || request.nota() < 1 || request.nota() > 5) {
            throw new IllegalArgumentException("A nota deve ser um número de 1 a 5");
        }

        String comentario = request.comentario() == null ? null : request.comentario().trim();
        if (comentario != null && comentario.isEmpty()) {
            comentario = null;
        }
        if (comentario != null && comentario.length() > TAMANHO_MAX_COMENTARIO) {
            throw new IllegalArgumentException(
                    "O comentário deve ter no máximo " + TAMANHO_MAX_COMENTARIO + " caracteres");
        }

        Filme filme = filmeRepository.findById(filmeId)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Avaliacao avaliacao = avaliacaoRepository
                .findByFilmeIdAndUsuarioId(filmeId, usuario.getId())
                .orElseGet(() -> {
                    Avaliacao nova = new Avaliacao();
                    nova.setFilme(filme);
                    nova.setUsuario(usuario);
                    return nova;
                });

        avaliacao.setNota(request.nota());
        avaliacao.setComentario(comentario);

        return toResponse(avaliacaoRepository.save(avaliacao));
    }

    private AvaliacaoResponse toResponse(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getUsuario().getNome(),
                avaliacao.getCriadoEm()
        );
    }
}
