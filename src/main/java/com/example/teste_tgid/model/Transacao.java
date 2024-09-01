package com.example.teste_tgid.model;

import lombok.Data;

@Data
public class Transacao {
    private Long id;
    private Cliente cliente;
    private Empresa empresa;
    private double valor;
    private double taxaAplicada;
    private TipoTransacao tipoTransacao;

}
