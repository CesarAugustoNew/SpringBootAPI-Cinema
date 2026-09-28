package com.Senai.Filmes.Repository;

import com.Senai.Filmes.Model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {

    // Avaliações de um filme, da mais recente para a mais antiga
    List<Avaliacao> findByFilmeIdOrderByCriadoEmDesc(UUID filmeId);

    // Avaliação que um usuário específico já fez de um filme (se houver)
    Optional<Avaliacao> findByFilmeIdAndUsuarioId(UUID filmeId, UUID usuarioId);

    // Usado ao deletar um filme, para não deixar avaliações órfãs
    void deleteByFilmeId(UUID filmeId);
}
