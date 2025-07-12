package com.cefet.API.dto;

import com.cefet.API.entities.Lancamento;
import com.cefet.API.entities.Operacao;
import com.cefet.API.entities.Tipo;

public class LancamentoTransferenciaDTO {
    private Long id;
    private Double valor;
    private Double taxa;
    private Long idContaOrigin;
    private Long idContaDestino;
    private Operacao operacao;
    private Tipo tipo;

    
    public LancamentoTransferenciaDTO() {
    }


    public Operacao getOperacao() {
        return operacao;
    }


    public Tipo getTipo() {
        return tipo;
    }


    public LancamentoTransferenciaDTO(Lancamento lancamento) {
        this.id = lancamento.getId();
        this.valor = lancamento.getValor();
        this.idContaOrigin = lancamento.getConta().getId();
        this.idContaDestino = lancamento.getConta2().getId();
        this.tipo = lancamento.getTipo();
        this.operacao = lancamento.getOperacao();
    }


    public Long getId() {
        return id;
    }
    public Long getIdContaOrigin() {
        return idContaOrigin;
    }


    public Double getValor() {
        return valor;
    }

    public Double getTaxa() {
        return taxa;
    }


    public Long getIdContaDestino() {
        return idContaDestino;
    }



}
