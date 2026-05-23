package br.com.fiap3ESA.ford.controller;

import br.com.fiap3ESA.ford.dto.VehicleRequestDTO;
import br.com.fiap3ESA.ford.dto.VehicleResponseDTO;
import br.com.fiap3ESA.ford.service.VehicleService;
import br.com.fiap3ESA.ford.model.Vehicle;
import br.com.fiap3ESA.ford.dto.VehicleComparisonRequestDTO;
import br.com.fiap3ESA.ford.dto.VehicleComparisonResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping("/specifications")
    public ResponseEntity<VehicleResponseDTO> getSpecifications(
            @RequestBody @Valid VehicleRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.getSpecifications(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {

        return ResponseEntity.ok(
                service.getAllVehicles()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id,
            @RequestBody Vehicle updatedVehicle
    ) {

        return ResponseEntity.ok(
                service.updateVehicle(id, updatedVehicle)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id
    ) {

        service.deleteVehicle(id);

        return ResponseEntity.noContent().build();
    }
    @PostMapping("/compare")
    public ResponseEntity<List<VehicleComparisonResponseDTO>> compareVehicles(
            @RequestBody VehicleComparisonRequestDTO request
    ) {

        return ResponseEntity.ok(
                service.compareVehicles(request)
        );
    }
}