package com.example.teste_tgid.service;

import com.example.teste_tgid.model.Cliente;
import com.example.teste_tgid.model.Empresa;
import com.example.teste_tgid.model.TipoTransacao;
import com.example.teste_tgid.model.TransacaoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransacaoServiceTest {

    private TransacaoService transacaoService;
    private Cliente cliente;
    private Empresa empresa;

    @BeforeEach
    public void inicializar() {
        transacaoService = new TransacaoService();

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setCpf("12345678900");
        cliente.setSaldo(1000.0);

        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa XYZ");
        empresa.setCnpj("12345678000195");
        empresa.setSaldo(5000.0);
        empresa.setTaxa(10.0);

        transacaoService.adicionarCliente(cliente.getId(), cliente);
        transacaoService.adicionarEmpresa(empresa.getId(), empresa);
    }

    @Test
    public void testar_se_deposito_500_retorna_saldo_500_e_saldo_5450_com_taxa() throws Exception {
        TransacaoRequest request = new TransacaoRequest();
        request.setClienteId(cliente.getId());
        request.setEmpresaId(empresa.getId());
        request.setValor(500.0);
        request.setTipo(TipoTransacao.DEPOSITO);

        transacaoService.realizarTransacao(request);

        Assertions.assertEquals(500.0, cliente.getSaldo(), "Saldo do cliente não atualizado!");
        Assertions.assertEquals(5450.0, empresa.getSaldo(), "Saldo da empresa não atualizado!");
    }

    @Test
    public void testar_saque_cliente_empresa() throws Exception {
        TransacaoRequest request = new TransacaoRequest();
        request.setClienteId(cliente.getId());
        request.setEmpresaId(empresa.getId());
        request.setValor(100.0);
        request.setTipo(TipoTransacao.SAQUE);

        transacaoService.realizarTransacao(request);

        Assertions.assertEquals(1090.0, cliente.getSaldo(), "Saldo do cliente não atualizado corretamente");
        Assertions.assertEquals(4900.0, empresa.getSaldo(), "Saldo da empresa não atualizado corretamente");
    }

    @Test
    public void testar_saque_insuficiente_retorna_erro() {
        TransacaoRequest request = new TransacaoRequest();
        request.setClienteId(cliente.getId());
        request.setEmpresaId(empresa.getId());
        request.setValor(5001);
        request.setTipo(TipoTransacao.SAQUE);

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            transacaoService.realizarTransacao(request);
        });

        Assertions.assertEquals("Saldo da empresa insuficiente para realizar o saque.", exception.getMessage());
    }
}
