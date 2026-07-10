package com.Senai.Filmes.Controller;

import com.Senai.Filmes.DTO.Request.SessaoRequest;
import com.Senai.Filmes.DTO.Response.AssentoResponse;
import com.Senai.Filmes.DTO.Response.SessaoResponse;
import com.Senai.Filmes.Service.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Sessões", description = "Endpoint para gerenciamento das sessões de exibição")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    /*
      Um único endpoint atende dois usos do front-end:

        GET /api/sessoes?data=2026-07-10   -> sessões daquele dia
        GET /api/sessoes?filmeId=<uuid>     -> sessões daquele filme

      Se filmeId for enviado, ele tem prioridade. Caso contrário,
      filtra pela data informada (ou por hoje, se nenhuma vier).
    */
    @GetMapping
    @Operation(summary = "Listar sessões", description = "Lista sessões filtrando por data ou por filme")
    public ResponseEntity<List<SessaoResponse>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) UUID filmeId) {

        List<SessaoResponse> sessoes = filmeId != null
                ? sessaoService.listarPorFilme(filmeId)
                : sessaoService.listarPorData(data != null ? data : LocalDate.now());

        if (sessoes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sessoes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sessão por ID", description = "Retorna os detalhes de uma única sessão")
    public ResponseEntity<SessaoResponse> buscarPorId(@PathVariable UUID id) {
        return new ResponseEntity<>(sessaoService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/assentos")
    @Operation(summary = "Listar assentos da sessão", description = "Lista os assentos da sala com sua disponibilidade para a sessão")
    public ResponseEntity<List<AssentoResponse>> listarAssentos(@PathVariable UUID id) {
        return new ResponseEntity<>(sessaoService.listarAssentos(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar sessão", description = "Agenda uma nova sessão de exibição")
    public ResponseEntity<SessaoResponse> criar(@RequestBody SessaoRequest request) {
        return new ResponseEntity<>(sessaoService.criar(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar sessão", description = "Remove uma sessão do sistema")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        sessaoService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
