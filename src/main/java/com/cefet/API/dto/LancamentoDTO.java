package com.cefet.API.dto;

import com.cefet.API.entities.Conta;
import com.cefet.API.entities.Lancamento;
import com.cefet.API.entities.Operacao;
import com.cefet.API.entities.Tipo;

public class LancamentoDTO {
    private Long id;
    private Double valor;
    private Double bonus;
    private Conta conta;
    private Long idConta;
    private Operacao operacao;
    private Tipo tipo;

    
    public LancamentoDTO() {
    }


    public Operacao getOperacao() {
        return operacao;
    }


    public Tipo getTipo() {
        return tipo;
    }


    public LancamentoDTO(Lancamento lancamento) {
        this.id = lancamento.getId();
        this.valor = lancamento.getValor();
        this.bonus = lancamento.getBonus();
        this.conta = lancamento.getConta();
        this.idConta = lancamento.getConta().getId();
        this.tipo = lancamento.getTipo();
        this.operacao = lancamento.getOperacao();
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


    public Double getBonus() {
        return bonus;
    }



}
