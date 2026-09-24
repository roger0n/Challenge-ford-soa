package br.com.fiap3ESA.ford.controller;

import br.com.fiap3ESA.ford.dto.LoginRequestDTO;
import br.com.fiap3ESA.ford.dto.LoginResponseDTO;
import br.com.fiap3ESA.ford.dto.RegisterRequestDTO;
import br.com.fiap3ESA.ford.model.User;
import br.com.fiap3ESA.ford.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Autenticação",
        description = "Endpoints para cadastro e autenticação de usuários"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Cadastrar usuário",
            description = "Cadastra um novo usuário com perfil USER."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou e-mail já cadastrado"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegisterRequestDTO request
    ) {
        User user = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @Operation(
            summary = "Realizar login",
            description = "Autentica o usuário e retorna um token JWT."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "E-mail ou senha inválidos"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}