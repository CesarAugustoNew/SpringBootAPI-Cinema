package com.Senai.Filmes.Config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/*
  Transforma as exceções lançadas pelos services em respostas HTTP com
  o corpo { "mensagem": "..." } — exatamente o campo que o front-end lê
  em src/services/api.js para mostrar o erro ao usuário.

  Sem isso, qualquer erro (ex.: "A nota deve ser de 1 a 5") virava um
  500 genérico sem mensagem.
*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> naoEncontrado(EntityNotFoundException e) {
        return resposta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> requisicaoInvalida(IllegalArgumentException e) {
        return resposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /*
      Login com e-mail ou senha errados (BadCredentialsException,
      UsernameNotFoundException etc. — todas herdam de AuthenticationException).

      Sem este handler, a exceção escapava do controller, o Spring redirecionava
      para /error (rota protegida) e o cliente recebia 403 com corpo vazio.
      Agora devolvemos 401 com uma mensagem amigável.
    */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> credenciaisInvalidas(AuthenticationException e) {
        return resposta(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflito(IllegalStateException e) {
        return resposta(HttpStatus.CONFLICT, e.getMessage());
    }

    private ResponseEntity<Map<String, String>> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("mensagem", mensagem == null ? "Erro" : mensagem));
    }
}
