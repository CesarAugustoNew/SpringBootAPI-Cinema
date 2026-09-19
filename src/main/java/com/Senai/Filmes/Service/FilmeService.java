package com.Senai.Filmes.Service;


import com.Senai.Filmes.DTO.Request.FilmeRequest;
import com.Senai.Filmes.DTO.Response.FilmeResponse;
import com.Senai.Filmes.Model.Filme;
import com.Senai.Filmes.Repository.IFilmeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class FilmeService {

    @Autowired
    private IFilmeRepository filmeRepository;

    //crud
    public List<FilmeResponse> listarTodos() {
        return filmeRepository.findAll().stream().map(this::toResponse).toList();
    }


    public FilmeResponse buscarPorFilmeId(UUID id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));

        return toResponse(filme);
    }

    public  FilmeResponse cadastrarFilme(FilmeRequest request){
        Filme filme = new Filme();
        filme.setTitulo(request.titulo());
        filme.setDescricao(request.descricao());
        filme.setUrlPoster(request.urlPoster());
        filme.setGenero(request.genero());
        filme.setDuracaoMinutos(request.duracaoMinutos());

        return toResponse(filmeRepository.save(filme));
    }

    public FilmeResponse atualizarFilme(UUID id, FilmeRequest filmeRequest) {
        Filme filme = filmeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nenhum filme encontrado"));

        filme.setTitulo(filmeRequest.titulo());
        filme.setDescricao(filmeRequest.descricao());
        filme.setUrlPoster(filmeRequest.urlPoster());
        filme.setGenero(filmeRequest.genero());
        filme.setDuracaoMinutos(filmeRequest.duracaoMinutos());
        return toResponse(filmeRepository.save(filme));
    }

    public void deletar (UUID id) {
        Filme filme = filmeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nenhum filme encontrado"));
        filmeRepository.delete(filme);
    }

    /*
      Converte a imagem enviada pelo admin para uma "data URL" em
      base64 (ex.: "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQ...") e
      guarda esse texto direto no campo urlPoster, no banco.

      Antes o arquivo era gravado em disco no servidor — mas no Render
      (plano free) esse disco não é permanente: some sempre que o
      serviço reinicia, inclusive só por ter ficado inativo um tempo.
      Guardando a imagem dentro do próprio banco (junto com o resto
      dos dados do filme), ela nunca se perde.

      Como o valor guardado já é uma data URL, o front-end usa ele
      direto num <img src="..."> sem precisar de nenhuma mudança —
      funciona exatamente como funcionava com uma URL de arquivo.
    */
    public FilmeResponse atualizarImagem(UUID id, MultipartFile arquivo) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma imagem foi enviada");
        }

        try {
            String tipoConteudo = arquivo.getContentType() != null ? arquivo.getContentType() : "image/jpeg";
            String base64 = Base64.getEncoder().encodeToString(arquivo.getBytes());
            String dataUrl = "data:" + tipoConteudo + ";base64," + base64;

            filme.setUrlPoster(dataUrl);
            return toResponse(filmeRepository.save(filme));
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao processar a imagem do filme", e);
        }
    }

    /*
      Remove o pôster de um filme: como a imagem mora só no campo
      urlPoster (não existe mais arquivo em disco), basta limpar esse
      campo.
    */
    public FilmeResponse removerImagem(UUID id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));

        filme.setUrlPoster(null);
        return toResponse(filmeRepository.save(filme));
    }

    private FilmeResponse toResponse(Filme filme) {
        return new FilmeResponse(
                filme.getId(),
                filme.getTitulo(),
                filme.getDescricao(),
                filme.getUrlPoster(),
                filme.getGenero(),
                filme.getDuracaoMinutos()
        );
    }

}












