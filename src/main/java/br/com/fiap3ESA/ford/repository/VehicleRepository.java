package br.com.fiap3ESA.ford.repository;

import br.com.fiap3ESA.ford.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByMarcaAndModeloAndVersao(
            String marca,
            String modelo,
            String versao
    );
}