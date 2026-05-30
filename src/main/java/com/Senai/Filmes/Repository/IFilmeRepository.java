package com.Senai.Filmes.Repository;

import com.Senai.Filmes.Model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IFilmeRepository extends JpaRepository<Filme, UUID> {

}
