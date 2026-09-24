package br.com.fiap3ESA.ford.service;

import br.com.fiap3ESA.ford.dto.VehicleRequestDTO;
import br.com.fiap3ESA.ford.dto.VehicleResponseDTO;
import br.com.fiap3ESA.ford.exception.VehicleNotFoundException;
import br.com.fiap3ESA.ford.model.Vehicle;
import br.com.fiap3ESA.ford.repository.VehicleRepository;
import br.com.fiap3ESA.ford.service.CompetitorIntegrationService;
import br.com.fiap3ESA.ford.dto.VehicleComparisonItemDTO;
import br.com.fiap3ESA.ford.dto.VehicleComparisonRequestDTO;
import br.com.fiap3ESA.ford.dto.VehicleComparisonResponseDTO;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Service
public class VehicleService {

    private final CompetitorIntegrationService integrationService;
    private final SpecificationNormalizationService normalizationService;

    public VehicleService(
            CompetitorIntegrationService integrationService,
            SpecificationNormalizationService normalizationService
    ) {
        this.integrationService = integrationService;
        this.normalizationService = normalizationService;
    }

    public VehicleResponseDTO getSpecifications(VehicleRequestDTO request) {

        Vehicle vehicle = integrationService
                .fetchVehicleData(
                        request.getMarca(),
                        request.getModelo(),
                        request.getVersao()
                );

        if (vehicle == null) {
            throw new VehicleNotFoundException(
                    "Veículo não encontrado"
            );
        }

        Map<String, String> specs = new HashMap<>();

        for (String atributo : request.getAtributos()) {

            switch (atributo.toLowerCase()) {

                case "motor":
                    specs.put(
                            "motor",
                            normalizationService.normalize(vehicle.getMotor())
                    );
                    break;

                case "potencia":
                    specs.put(
                            "potencia",
                            normalizationService.normalize(vehicle.getPotencia())
                    );
                    break;

                case "torque":
                    specs.put(
                            "torque",
                            normalizationService.normalize(vehicle.getTorque())
                    );
                    break;

                case "cambio":
                    specs.put(
                            "cambio",
                            normalizationService.normalize(vehicle.getCambio())
                    );
                    break;

                case "tracao":
                    specs.put(
                            "tracao",
                            normalizationService.normalize(vehicle.getTracao())
                    );
                    break;

                case "combustivel":
                    specs.put(
                            "combustivel",
                            normalizationService.normalize(vehicle.getCombustivel())
                    );
                    break;

                case "capacidadecarga":
                    specs.put(
                            "capacidadeCarga",
                            normalizationService.normalize(vehicle.getCapacidadeCarga())
                    );
                    break;

                default:
                    specs.put(
                            atributo,
                            "ATRIBUTO NÃO ENCONTRADO"
                    );
            }
        }

        return VehicleResponseDTO.builder()
                .marca(vehicle.getMarca())
                .modelo(vehicle.getModelo())
                .versao(vehicle.getVersao())
                .especificacoes(specs)
                .build();
    }

    public List<Vehicle> getAllVehicles() {

        return integrationService.findAll();
    }

    public Vehicle updateVehicle(
            Long id,
            Vehicle updatedVehicle
    ) {

        Vehicle vehicle = integrationService.findById(id);

        if (vehicle == null) {
            throw new VehicleNotFoundException(
                    "Veículo não encontrado"
            );
        }

        vehicle.setMarca(updatedVehicle.getMarca());
        vehicle.setModelo(updatedVehicle.getModelo());
        vehicle.setVersao(updatedVehicle.getVersao());
        vehicle.setMotor(updatedVehicle.getMotor());
        vehicle.setPotencia(updatedVehicle.getPotencia());
        vehicle.setTorque(updatedVehicle.getTorque());
        vehicle.setCambio(updatedVehicle.getCambio());
        vehicle.setTracao(updatedVehicle.getTracao());
        vehicle.setCombustivel(updatedVehicle.getCombustivel());
        vehicle.setCapacidadeCarga(updatedVehicle.getCapacidadeCarga());

        return integrationService.save(vehicle);
    }

    public void deleteVehicle(Long id) {

        Vehicle vehicle = integrationService.findById(id);

        if (vehicle == null) {
            throw new VehicleNotFoundException(
                    "Veículo não encontrado"
            );
        }

        integrationService.delete(vehicle);
    }

    public List<VehicleComparisonResponseDTO> compareVehicles(
            VehicleComparisonRequestDTO request
    ) {

        List<VehicleComparisonResponseDTO> comparison = new ArrayList<>();

        for (VehicleComparisonItemDTO item : request.getVeiculos()) {

            Vehicle vehicle = integrationService
                    .fetchVehicleData(
                            item.getMarca(),
                            item.getModelo(),
                            item.getVersao()
                    );

            if (vehicle == null) {
                continue;
            }

            Map<String, String> specs = new HashMap<>();

            for (String atributo : request.getAtributos()) {

                switch (atributo.toLowerCase()) {

                    case "motor":
                        specs.put(
                                "motor",
                                normalizationService.normalize(vehicle.getMotor())
                        );
                        break;

                    case "potencia":
                        specs.put(
                                "potencia",
                                normalizationService.normalize(vehicle.getPotencia())
                        );
                        break;

                    case "torque":
                        specs.put(
                                "torque",
                                normalizationService.normalize(vehicle.getTorque())
                        );
                        break;

                    case "cambio":
                        specs.put(
                                "cambio",
                                normalizationService.normalize(vehicle.getCambio())
                        );
                        break;

                    case "tracao":
                        specs.put(
                                "tracao",
                                normalizationService.normalize(vehicle.getTracao())
                        );
                        break;
                }
            }

            comparison.add(
                    VehicleComparisonResponseDTO.builder()
                            .veiculo(
                                    vehicle.getMarca() + " "
                                            + vehicle.getModelo() + " "
                                            + vehicle.getVersao()
                            )
                            .especificacoes(specs)
                            .build()
            );
        }

        return comparison;
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        vehicle.setId(null);
        return integrationService.save(vehicle);
    }
}