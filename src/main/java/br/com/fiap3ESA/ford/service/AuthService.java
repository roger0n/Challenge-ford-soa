package br.com.fiap3ESA.ford.service;

import br.com.fiap3ESA.ford.dto.LoginRequestDTO;
import br.com.fiap3ESA.ford.dto.LoginResponseDTO;
import br.com.fiap3ESA.ford.dto.RegisterRequestDTO;
import br.com.fiap3ESA.ford.model.User;
import br.com.fiap3ESA.ford.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.fiap3ESA.ford.model.Role;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public User register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        User user = User.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException("E-mail ou senha inválidos");
        }

        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token);
    }
}