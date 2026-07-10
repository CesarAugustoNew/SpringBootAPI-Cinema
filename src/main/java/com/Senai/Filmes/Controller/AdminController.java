package com.Senai.Filmes.Controller;

import com.Senai.Filmes.DTO.Response.RelatorioResponse;
import com.Senai.Filmes.DTO.Response.ReservaResponse;
import com.Senai.Filmes.Service.AdminService;
import com.Senai.Filmes.Service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/*
  Todo o painel admin exige cargo ADMIN — @PreAuthorize a nível de
  classe se aplica a todos os métodos abaixo.
*/
@Tag(name = "Admin", description = "Endpoints administrativos: reservas gerais, relatórios e promoção de usuários")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/reservas")
    @Operation(summary = "Listar todas as reservas", description = "Lista as reservas de todos os usuários do sistema")
    public ResponseEntity<List<ReservaResponse>> listarReservas() {
        return new ResponseEntity<>(reservaService.listarTodas(), HttpStatus.OK);
    }

    @GetMapping("/relatorios")
    @Operation(summary = "Relatório geral", description = "Gera o relatório de reservas e receita do sistema")
    public ResponseEntity<RelatorioResponse> gerarRelatorio() {
        return new ResponseEntity<>(adminService.gerarRelatorio(), HttpStatus.OK);
    }

    @PatchMapping("/usuarios/{id}/promover")
    @Operation(summary = "Promover usuário", description = "Promove um usuário comum para o cargo ADMIN")
    public ResponseEntity<Void> promoverUsuario(@PathVariable UUID id) {
        adminService.promoverUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
