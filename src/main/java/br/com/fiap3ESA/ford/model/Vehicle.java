package br.com.fiap3ESA.ford.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;

    private String modelo;

    private String versao;

    private String motor;

    private String potencia;

    private String torque;

    private String cambio;

    private String tracao;

    private String combustivel;

    private String capacidadeCarga;
}