package com.example.teste_tgid.service;

import com.example.teste_tgid.model.TransacaoRequest;

public interface CallBackService {
    void enviarCallBack(String url , TransacaoRequest request);
}
