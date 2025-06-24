package com.cefet.prova_20203004575.dto;

import com.cefet.prova_20203004575.entities.Conta;
import com.cefet.prova_20203004575.entities.Lancamento;

public class LancamentoDTO {
    private Long id;
    private Double valor;
    private Conta conta;
    private Long idConta;

    
    public LancamentoDTO() {
    }


    public LancamentoDTO(Lancamento lancamento) {
        this.id = lancamento.getId();
        this.valor = lancamento.getValor();
        this.conta = lancamento.getConta();
        this.idConta = lancamento.getConta().getId();
    }


    public Long getId() {
        return id;
    }
    public Long getIdConta() {
        return idConta;
    }


    public Double getValor() {
        return valor;
    }


    public Conta getConta() {
        return conta;
    }



}
