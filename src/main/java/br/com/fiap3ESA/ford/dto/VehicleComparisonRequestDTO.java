package br.com.fiap3ESA.ford.dto;

import lombok.Data;

import java.util.List;

@Data
public class VehicleComparisonRequestDTO {

    private List<VehicleComparisonItemDTO> veiculos;

    private List<String> atributos;
}