package com.Senai.Filmes.Service;


import com.Senai.Filmes.DTO.Request.FilmeRequest;
import com.Senai.Filmes.DTO.Response.FilmeResponse;
import com.Senai.Filmes.Model.Filme;
import com.Senai.Filmes.Repository.IFilmeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FilmeService {

    @Autowired
    private IFilmeRepository filmeRepository;

    /*
      Pasta onde os pôsteres enviados pelo admin são gravados no disco
      do servidor. Pode ser configurada em application.properties
      via "app.upload.dir" (por padrão usa uma pasta "uploads" na raiz
      do projeto). Os arquivos ficam em <uploadDir>/posters/ e são
      expostos publicamente em /uploads/posters/** (ver WebConfig).
    */
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

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
      Salva o arquivo de imagem enviado pelo admin em disco e atualiza
      o campo urlPoster do filme para apontar para a rota pública
      /uploads/posters/<nome-gerado>.

      Um nome de arquivo aleatório (UUID) é usado para evitar
      conflitos entre filmes diferentes e problemas com caracteres
      especiais no nome original do arquivo.
    */
    public FilmeResponse atualizarImagem(UUID id, MultipartFile arquivo) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma imagem foi enviada");
        }

        try {
            Path pastaPosters = Paths.get(uploadDir, "posters");
            Files.createDirectories(pastaPosters);

            String nomeArquivo = UUID.randomUUID() + extensaoDoArquivo(arquivo.getOriginalFilename());
            Path destino = pastaPosters.resolve(nomeArquivo);
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            removerArquivoPosterAtual(filme);

            filme.setUrlPoster("/uploads/posters/" + nomeArquivo);
            return toResponse(filmeRepository.save(filme));
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao salvar a imagem do filme", e);
        }
    }

    /*
      Remove o pôster de um filme: apaga o arquivo salvo em disco
      (se existir) e limpa o campo urlPoster.
    */
    public FilmeResponse removerImagem(UUID id) {
        Filme filme = filmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme não encontrado"));

        removerArquivoPosterAtual(filme);
        filme.setUrlPoster(null);
        return toResponse(filmeRepository.save(filme));
    }

    private void removerArquivoPosterAtual(Filme filme) {
        String urlAtual = filme.getUrlPoster();
        if (urlAtual != null && urlAtual.startsWith("/uploads/")) {
            try {
                Path caminhoAntigo = Paths.get(uploadDir, urlAtual.substring("/uploads/".length()));
                Files.deleteIfExists(caminhoAntigo);
            } catch (IOException ignored) {
                // Se não conseguir apagar o arquivo antigo, seguimos em frente —
                // não é motivo para impedir a troca/remoção do pôster.
            }
        }
    }

    private String extensaoDoArquivo(String nomeOriginal) {
        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            return nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        }
        return "";
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












