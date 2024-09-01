package com.example.teste_tgid.service;

import com.example.teste_tgid.model.Empresa;

public class NotificacaoService {
    public void enviarNotificacao(String email , String assunto, String mensagem) {
        System.out.println("Enviando e-mail para: " + email);
        System.out.println("Assunto: " + assunto);
        System.out.println("Mensagem: " + mensagem);
    }
}
