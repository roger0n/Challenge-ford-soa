package br.com.fiap3ESA.ford.service;

import br.com.fiap3ESA.ford.dto.RegisterRequestDTO;
import br.com.fiap3ESA.ford.model.Role;
import br.com.fiap3ESA.ford.model.User;
import br.com.fiap3ESA.ford.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap3ESA.ford.dto.LoginRequestDTO;
import br.com.fiap3ESA.ford.dto.LoginResponseDTO;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authService = new AuthService(
                userRepository,
                passwordEncoder,
                tokenService
        );
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setNome("Augusto");
        request.setEmail("admin@ford.com");
        request.setPassword("123456");


        when(userRepository.existsByEmail("admin@ford.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("senha-criptografada");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        User resultado = authService.register(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Augusto", resultado.getNome());
        assertEquals("admin@ford.com", resultado.getEmail());
        assertEquals("senha-criptografada", resultado.getPassword());
        assertEquals(Role.USER, resultado.getRole());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailJaExistente() {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setNome("Augusto");
        request.setEmail("admin@ford.com");
        request.setPassword("123456");


        when(userRepository.existsByEmail("admin@ford.com"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request)
        );

        assertEquals("E-mail já cadastrado", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deveRealizarLoginComSucesso() {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@ford.com");
        request.setPassword("123456");

        User user = User.builder()
                .id(1L)
                .nome("Augusto")
                .email("admin@ford.com")
                .password("senha-criptografada")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findByEmail("admin@ford.com"))
                .thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches("123456", "senha-criptografada"))
                .thenReturn(true);

        when(tokenService.generateToken(user))
                .thenReturn("token-jwt-teste");

        LoginResponseDTO resultado = authService.login(request);

        assertNotNull(resultado);
        assertEquals("token-jwt-teste", resultado.getToken());

        verify(tokenService).generateToken(user);
    }

    @Test
    void naoDeveRealizarLoginComSenhaIncorreta() {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@ford.com");
        request.setPassword("senha-errada");

        User user = User.builder()
                .id(1L)
                .nome("Augusto")
                .email("admin@ford.com")
                .password("senha-criptografada")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findByEmail("admin@ford.com"))
                .thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches(
                "senha-errada",
                "senha-criptografada"
        )).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(request)
        );

        assertEquals(
                "E-mail ou senha inválidos",
                exception.getMessage()
        );

        verify(tokenService, never()).generateToken(any(User.class));
    }
}