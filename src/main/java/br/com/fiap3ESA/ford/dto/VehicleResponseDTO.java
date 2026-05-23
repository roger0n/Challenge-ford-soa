package br.com.fiap3ESA.ford.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class VehicleResponseDTO {

    private String marca;

    private String modelo;

    private String versao;

    private Map<String, String> especificacoes;
}