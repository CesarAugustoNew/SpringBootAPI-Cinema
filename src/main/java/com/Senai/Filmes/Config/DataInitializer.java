package com.Senai.Filmes.Config;

import com.Senai.Filmes.Model.Enums.Cargo;
import com.Senai.Filmes.Model.Usuario;
import com.Senai.Filmes.Repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@cinemasenai.com}")
    private String adminEmail;

    @Value("${admin.senha:Admin@134}")
    private String adminSenha;

    @Override
    public void run(String... args) {

        if (usuarioRepository.existeByEmail(adminEmail)) {
            return;
        }

        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setNome("Cesar Augusto");
        usuarioAdmin.setEmail(adminEmail);
        usuarioAdmin.setSenha(passwordEncoder.encode(adminSenha));
        usuarioAdmin.setCargo(Cargo.ADMIN);

        usuarioRepository.save(usuarioAdmin);

        System.out.println(
                ">>>>>>>>>> USUARIO ADMIN CRIADO: "
                        + adminEmail
                        + " <<<<<<<<<<"
        );
    }
}