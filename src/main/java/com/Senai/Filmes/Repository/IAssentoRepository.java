package com.Senai.Filmes.Repository;

import com.Senai.Filmes.Model.Assento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IAssentoRepository extends JpaRepository<Assento, UUID> {
    List<Assento> findAssentoById(UUID salaId);
}
