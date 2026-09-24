package br.com.fiap3ESA.ford.controller;

import br.com.fiap3ESA.ford.dto.VehicleComparisonRequestDTO;
import br.com.fiap3ESA.ford.dto.VehicleComparisonResponseDTO;
import br.com.fiap3ESA.ford.dto.VehicleRequestDTO;
import br.com.fiap3ESA.ford.dto.VehicleResponseDTO;
import br.com.fiap3ESA.ford.model.Vehicle;
import br.com.fiap3ESA.ford.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@Tag(
        name = "Veículos",
        description = "Consulta, comparação e gerenciamento de veículos"
)
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @Operation(
            summary = "Consultar especificações",
            description = "Retorna as especificações solicitadas de um veículo."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Especificações retornadas com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da requisição inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo não encontrado"
            )
    })
    @PostMapping("/specifications")
    public ResponseEntity<VehicleResponseDTO> getSpecifications(
            @RequestBody @Valid VehicleRequestDTO request
    ) {
        return ResponseEntity.ok(
                service.getSpecifications(request)
        );
    }

    @Operation(
            summary = "Listar veículos",
            description = "Retorna todos os veículos cadastrados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Veículos retornados com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            )
    })
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(
                service.getAllVehicles()
        );
    }

    @Operation(
            summary = "Atualizar veículo",
            description = "Atualiza um veículo existente. Requer perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Veículo atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso permitido apenas para ADMIN"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo não encontrado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id,
            @RequestBody Vehicle updatedVehicle
    ) {
        return ResponseEntity.ok(
                service.updateVehicle(id, updatedVehicle)
        );
    }

    @Operation(
            summary = "Excluir veículo",
            description = "Exclui um veículo pelo ID. Requer perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Veículo excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso permitido apenas para ADMIN"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Veículo não encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id
    ) {
        service.deleteVehicle(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Comparar veículos",
            description = "Compara os atributos técnicos solicitados entre veículos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Comparação realizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            )
    })
    @PostMapping("/compare")
    public ResponseEntity<List<VehicleComparisonResponseDTO>> compareVehicles(
            @RequestBody VehicleComparisonRequestDTO request
    ) {
        return ResponseEntity.ok(
                service.compareVehicles(request)
        );
    }

    @Operation(
            summary = "Cadastrar veículo",
            description = "Cadastra um novo veículo. Requer perfil ADMIN."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Veículo cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso permitido apenas para ADMIN"
            )
    })
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(
            @RequestBody Vehicle vehicle
    ) {
        Vehicle createdVehicle = service.createVehicle(vehicle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdVehicle);
    }
}