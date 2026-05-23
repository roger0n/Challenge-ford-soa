package br.com.fiap3ESA.ford.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class VehicleRequestDTO {

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    @NotBlank
    private String versao;

    private List<String> atributos;
}
