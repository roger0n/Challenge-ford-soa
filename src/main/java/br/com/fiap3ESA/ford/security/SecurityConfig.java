package br.com.fiap3ESA.ford.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // Endpoints públicos
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Consulta de veículos - USER ou ADMIN
                        .requestMatchers(HttpMethod.GET, "/vehicles/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Consulta de especificações e comparação
                        .requestMatchers(HttpMethod.POST, "/vehicles/specifications")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/vehicles/compare")
                        .hasAnyRole("USER", "ADMIN")

                        // Alteração e exclusão - somente ADMIN
                        .requestMatchers(HttpMethod.PUT, "/vehicles/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/vehicles/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/vehicles")
                        .hasRole("ADMIN")

                        // Qualquer outro endpoint precisa de autenticação
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("""
                    {
                      "error": "Unauthorized",
                      "message": "Autenticação necessária",
                      "status": 401
                    }
                    """);
                        })

                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("""
                    {
                      "error": "Forbidden",
                      "message": "Você não possui permissão para acessar este recurso",
                      "status": 403
                    }
                    """);
                        })
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}