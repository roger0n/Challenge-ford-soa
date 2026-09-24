package br.com.fiap3ESA.ford.config;

import br.com.fiap3ESA.ford.model.Role;
import br.com.fiap3ESA.ford.model.User;
import br.com.fiap3ESA.ford.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String email = "admin@ford.com";

            if (!userRepository.existsByEmail(email)) {

                User admin = User.builder()
                        .nome("Administrador")
                        .email(email)
                        .password(passwordEncoder.encode("123456"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(admin);
            }
        };
    }
}