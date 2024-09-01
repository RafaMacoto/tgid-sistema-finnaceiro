package com.example.teste_tgid.service;

import com.example.teste_tgid.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransacaoService {

    private final Map<Long, Cliente> clientes = new HashMap<>();
    private final Map<Long, Empresa> empresas = new HashMap<>();
    private NotificacaoService notificacaoService;

    @Autowired
    private HttpCallBackService callbackService;

    public void realizarTransacao(TransacaoRequest request) throws Exception {
        Cliente cliente = clientes.get(request.getClienteId());
        Empresa empresa = empresas.get(request.getEmpresaId());

        if (cliente == null || empresa == null) {
            throw new Exception("Cliente ou Empresa não encontrados.");
        }

        double valorTransacao = request.getValor();
        double taxa = empresa.getTaxa();
        double valorFinal = calcularValorFinal(valorTransacao, taxa);

        if (valorFinal <= 0) {
            throw new Exception("Valor após aplicação da taxa é insuficiente.");
        }

        if (request.getTipo() == TipoTransacao.DEPOSITO) {
            processarDeposito(cliente, empresa, valorTransacao, valorFinal);
        } else if (request.getTipo() == TipoTransacao.SAQUE) {
            processarSaque(cliente, empresa, valorTransacao, valorFinal);
        } else {
            throw new Exception("Tipo de transação inválido.");
        }

        registrarTransacao(cliente, empresa, valorTransacao, taxa, request.getTipo());
        notificacaoService.enviarNotificacao(cliente.getEmail() , "Confirmação de transação" , "Sua transação de " + valorTransacao + " foi realizada com sucesso!");
        String callbackUrl = "https://webhook.site/96f0b299-e11a-44b3-b35f-6a0111c5f2d8";
        callbackService.enviarCallBack(callbackUrl, request);
    }

    private double calcularValorFinal(double valor, double taxa) {
        return valor - (valor * taxa / 100);
    }

    private void processarDeposito(Cliente cliente, Empresa empresa, double valorTransacao, double valorFinal) {
        cliente.setSaldo(cliente.getSaldo() - valorTransacao);
        empresa.setSaldo(empresa.getSaldo() + valorFinal);
    }

    private void processarSaque(Cliente cliente, Empresa empresa, double valorTransacao, double valorFinal) throws Exception {
        if (empresa.getSaldo() < valorTransacao) {
            throw new Exception("Saldo da empresa insuficiente para realizar o saque.");
        }

        cliente.setSaldo(cliente.getSaldo() + valorFinal);
        empresa.setSaldo(empresa.getSaldo() - valorTransacao);
    }

    private void registrarTransacao(Cliente cliente, Empresa empresa, double valor, double taxa, TipoTransacao tipo) {
        Transacao transacao = new Transacao();
        transacao.setCliente(cliente);
        transacao.setEmpresa(empresa);
        transacao.setValor(valor);
        transacao.setTipoTransacao(tipo);
        transacao.setTaxaAplicada(taxa);

    }

    public void adicionarCliente(Long id, Cliente cliente) {
        clientes.put(id, cliente);
    }

    public void adicionarEmpresa(Long id, Empresa empresa) {
        empresas.put(id, empresa);
    }
}
