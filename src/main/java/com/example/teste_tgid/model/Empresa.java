package com.example.teste_tgid.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Empresa {
    private Long id;

    @NotBlank(message = "Nome não pode ser vazio")
    private String nome;

    @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos")
    private String cnpj;

    private double saldo;

    private double taxa;

}
