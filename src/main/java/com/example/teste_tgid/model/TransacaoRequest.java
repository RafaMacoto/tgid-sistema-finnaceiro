package com.example.teste_tgid.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoRequest {
    private Long clienteId;
    private Long empresaId;
    private double valor;
    private TipoTransacao tipo;


}
