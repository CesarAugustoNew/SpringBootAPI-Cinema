package com.Senai.Filmes.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
  Expõe a pasta de uploads (onde os pôsteres dos filmes são salvos)
  como arquivos estáticos, acessíveis em:

      http://localhost:8080/uploads/posters/<arquivo>

  Sem essa configuração, o navegador recebe 404 ao tentar carregar
  a imagem salva por FilmeService.atualizarImagem().
*/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String localizacao = "file:" + uploadDir + "/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(localizacao);
    }
}
