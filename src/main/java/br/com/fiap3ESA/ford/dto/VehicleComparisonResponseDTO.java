package br.com.fiap3ESA.ford.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class VehicleComparisonResponseDTO {

    private String veiculo;

    private Map<String, String> especificacoes;
}