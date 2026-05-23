package br.com.fiap3ESA.ford.service;

import br.com.fiap3ESA.ford.model.Vehicle;
import br.com.fiap3ESA.ford.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompetitorIntegrationService {

    private final VehicleRepository repository;

    public CompetitorIntegrationService(
            VehicleRepository repository
    ) {
        this.repository = repository;
    }

    public Vehicle fetchVehicleData(
            String marca,
            String modelo,
            String versao
    ) {

        return repository
                .findByMarcaAndModeloAndVersao(
                        marca,
                        modelo,
                        versao
                )
                .orElse(null);
    }

    public List<Vehicle> findAll() {

        return repository.findAll();
    }

    public Vehicle findById(Long id) {

        return repository.findById(id)
                .orElse(null);
    }

    public Vehicle save(Vehicle vehicle) {

        return repository.save(vehicle);
    }

    public void delete(Vehicle vehicle) {

        repository.delete(vehicle);
    }
}