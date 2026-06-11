package com.Senai.Filmes.Controller;


import com.Senai.Filmes.DTO.Request.FilmeRequest;
import com.Senai.Filmes.DTO.Response.FilmeResponse;
import com.Senai.Filmes.Service.FilmeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/filmes")
public class FilmeController {

    @Autowired
    private FilmeService filmeService;

    @GetMapping
    @Operation(summary = "Listar todos os filmes", description = "Rota para listar todos os filmes cadastrados")
    public ResponseEntity<List<FilmeResponse>> listarTodos() {
        List<FilmeResponse> filmes = filmeService.listarTodos();
        if (filmes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity<>(filmes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar filmes por ID", description = "Retorna os detalhes de um único filme")
    public ResponseEntity<FilmeResponse> buscarPorFilmeId(@PathVariable UUID id) {
        return new ResponseEntity<>(filmeService.buscarPorFilmeId(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar Filme", description = "Cadastrar um novo filme")
    public ResponseEntity<FilmeResponse> criarFilme(@RequestBody FilmeRequest filmerequest) {
        return new ResponseEntity<>(filmeService.cadastrarFilme(filmerequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar filme", description = "Atualiza os dados de um filme")
    public ResponseEntity<FilmeResponse> atualizar(@PathVariable UUID id, @RequestBody FilmeRequest filmeRequest) {
        return new ResponseEntity<>(filmeService.atualizarFilme(id, filmeRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar filme", description = "Remove um filme do sistema")
    public  ResponseEntity<FilmeResponse> deletar(@PathVariable UUID id){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
