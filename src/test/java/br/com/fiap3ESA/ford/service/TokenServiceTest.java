package br.com.fiap3ESA.ford.service;

import br.com.fiap3ESA.ford.model.Role;
import br.com.fiap3ESA.ford.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

import com.auth0.jwt.JWT;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();

        ReflectionTestUtils.setField(
                tokenService,
                "secret",
                "chave-secreta-de-teste"
        );
    }

    @Test
    void deveGerarEValidarToken() {

        User user = User.builder()
                .id(1L)
                .nome("Augusto")
                .email("admin@ford.com")
                .password("senha-criptografada")
                .role(Role.ADMIN)
                .build();

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());

        String email = tokenService.validateToken(token);

        assertEquals("admin@ford.com", email);
    }

    @Test
    void deveRejeitarTokenInvalido() {

        String tokenInvalido = "token.jwt.invalido";

        String resultado = tokenService.validateToken(tokenInvalido);

        assertNull(resultado);
    }

    @Test
    void deveConterRoleNoToken() {

        User user = User.builder()
                .id(1L)
                .nome("Augusto")
                .email("admin@ford.com")
                .password("senha-criptografada")
                .role(Role.ADMIN)
                .build();

        String token = tokenService.generateToken(user);

        String role = JWT.decode(token)
                .getClaim("role")
                .asString();

        assertEquals("ADMIN", role);
    }
}