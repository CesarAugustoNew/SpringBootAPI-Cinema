package com.Senai.Filmes.Repository;

import com.Senai.Filmes.Model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ISessaoRepository extends JpaRepository<Sessao, UUID> {

    @Query(" SELECT s FROM Sessao s WHERE s.inicio >= :inicioDia AND s.inicio < :fimDia")
    List<Sessao> findByData(@Param("inicioDia") LocalDateTime inicioDia,
                            @Param("fimDia") LocalDateTime fimDia);

    @Query(" SELECT s FROM Sessao s WHERE s.filme.id = :filmeId ORDER BY s.inicio ASC")
    List<Sessao> findByFilmeId(@Param("filmeId") UUID filmeId);
}