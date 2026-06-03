package com.Senai.Filmes.Repository;

import com.Senai.Filmes.Model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISalaRepository extends JpaRepository<Sala, UUID> {

}