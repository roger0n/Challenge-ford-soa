package br.com.fiap3ESA.ford.service;

import org.springframework.stereotype.Service;

@Service
public class SpecificationNormalizationService {

    public String normalize(String value) {

        if (value == null || value.isBlank()) {
            return "NÃO DISPONÍVEL";
        }

        return value;
    }
}