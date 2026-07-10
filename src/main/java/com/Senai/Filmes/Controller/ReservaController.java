package com.Senai.Filmes.Controller;

import com.Senai.Filmes.DTO.Request.ReservaRequest;
import com.Senai.Filmes.DTO.Response.ReservaResponse;
import com.Senai.Filmes.Service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/*
  Todos os endpoints aqui exigem usuário autenticado (ver
  SecurityConfig: qualquer rota não listada como permitAll cai em
  anyRequest().authenticated()). O usuário é identificado pelo e-mail
  presente no token JWT (Authentication#getName()), então cada um só
  enxerga/mexe nas próprias reservas — exceto o admin, que pode
  cancelar qualquer uma.
*/
@Tag(name = "Reservas", description = "Endpoint para reserva de ingressos")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    @Operation(summary = "Criar reserva", description = "Reserva assentos para o usuário logado")
    public ResponseEntity<ReservaResponse> criar(@RequestBody ReservaRequest request, Authentication authentication) {
        ReservaResponse response = reservaService.criar(authentication.getName(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/minhas")
    @Operation(summary = "Minhas reservas", description = "Lista as reservas do usuário logado")
    public ResponseEntity<List<ReservaResponse>> listarMinhas(Authentication authentication) {
        return new ResponseEntity<>(reservaService.listarMinhas(authentication.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva", description = "Cancela uma reserva (a própria, ou qualquer uma se for admin)")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        reservaService.cancelar(id, authentication.getName(), isAdmin);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
