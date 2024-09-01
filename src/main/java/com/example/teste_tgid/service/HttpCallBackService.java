package com.example.teste_tgid.service;

import com.example.teste_tgid.model.TransacaoRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpCallBackService implements CallBackService{

    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public void enviarCallBack(String url, TransacaoRequest request){
        try {

            String payload = String.format("Transação de %f realizada para empresa %s. Tipo: %s",
                    request.getValor(), request.getEmpresaId(), request.getTipo());

            ResponseEntity<String> response = restTemplate.postForEntity(url, payload, String.class);


            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Callback enviado com sucesso: " + response.getBody());
            } else {
                System.out.println("Falha ao enviar callback: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Erro ao enviar callback: " + e.getMessage());
        }
    }
}

