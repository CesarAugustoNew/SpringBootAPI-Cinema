package com.Senai.Filmes.Repository;

import com.Senai.Filmes.Model.Enums.StatusReserva;
import com.Senai.Filmes.Model.ReservaAssento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IReservaAssentoRepository extends JpaRepository<ReservaAssento, UUID> {

    @Query("SELECT CASE WHEN COUNT(ra) > 0 THEN true ELSE false END" +
            " FROM ReservaAssento ra" +
            " WHERE ra.assento.id = :assentoId AND ra.reserva.sessao.id = :sessaoId AND ra.reserva.status = :status")
    boolean isAssentoOcupado(@Param("assentoId") UUID assentoId,
                             @Param("sessaoId") UUID sessaoId,
                             @Param("status") StatusReserva statusReserva);

    @Query("SELECT ra.assento.id FROM ReservaAssento ra " +
            "WHERE ra.reserva.id = :sessaoId AND ra.reserva.status = :status")
    List<UUID> findAssentosOcupadosBySessaoId(@Param("sessaoId") UUID sessaoId,
                                              @Param("status") StatusReserva statusReserva);
}