package com.Senai.Filmes.Controller;

import com.Senai.Filmes.DTO.Request.AvaliacaoRequest;
import com.Senai.Filmes.DTO.Response.AvaliacaoResponse;
import com.Senai.Filmes.Service.AvaliacaoService;
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
  GET  /api/filmes/{filmeId}/avaliacoes -> público (ver SecurityConfig:
                                            GET /api/filmes/** é permitAll)
  POST /api/filmes/{filmeId}/avaliacoes -> exige login (cai em
                                            anyRequest().authenticated())
*/
@Tag(name = "Avaliações", description = "Endpoint para avaliação de filmes")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/filmes/{filmeId}/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    @Operation(summary = "Listar avaliações", description = "Lista as avaliações de um filme (mais recentes primeiro)")
    public ResponseEntity<List<AvaliacaoResponse>> listar(@PathVariable UUID filmeId) {
        return new ResponseEntity<>(avaliacaoService.listarPorFilme(filmeId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Avaliar filme", description = "Cria (ou atualiza) a avaliação do usuário logado para o filme")
    public ResponseEntity<AvaliacaoResponse> avaliar(@PathVariable UUID filmeId,
                                                     @RequestBody AvaliacaoRequest request,
                                                     Authentication authentication) {
        AvaliacaoResponse response = avaliacaoService.salvar(filmeId, authentication.getName(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
