package com.Senai.Filmes.Controller;

import com.Senai.Filmes.DTO.Request.SalaRequest;
import com.Senai.Filmes.DTO.Response.SalaResponse;
import com.Senai.Filmes.Service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Tag(name = "Salas", description = "Endpoint para gerenciamento de salas do cinema")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/salas")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @GetMapping
    @Operation(summary = "Listar todas as salas", description = "Rota para listar todas as salas cadastradas")
    public ResponseEntity<List<SalaResponse>> listarTodos() {
        List<SalaResponse> salas = salaService.listarTodos();
        if (salas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(salas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por ID", description = "Retorna os detalhes de uma única sala")
    public ResponseEntity<SalaResponse> buscarPorSalaId(@PathVariable UUID id) {
        return new ResponseEntity<>(salaService.buscarPorSalaId(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar Sala", description = "Cadastrar uma nova sala com seus assentos")
    public ResponseEntity<SalaResponse> criarSala(@RequestBody SalaRequest salaRequest) {
        return new ResponseEntity<>(salaService.cadastrarSala(salaRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sala", description = "Atualiza os dados de uma sala")
    public ResponseEntity<SalaResponse> atualizar(@PathVariable UUID id, @RequestBody SalaRequest salaRequest) {
        return new ResponseEntity<>(salaService.atualizarSala(id, salaRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar sala", description = "Remove uma sala do sistema")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        salaService.deletar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}