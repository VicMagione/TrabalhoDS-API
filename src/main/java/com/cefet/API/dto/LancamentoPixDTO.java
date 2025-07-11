package com.cefet.API.dto;

import com.cefet.API.entities.Lancamento;
import com.cefet.API.entities.Operacao;
import com.cefet.API.entities.Tipo;

public class LancamentoPixDTO {
    private Long id;
    private Double valor;
    private String pixOrigin;
    private String pixDestino;
    private Operacao operacao;
    private Tipo tipo;

    
    public LancamentoPixDTO() {
    }


    public Operacao getOperacao() {
        return operacao;
    }


    public Tipo getTipo() {
        return tipo;
    }


    public LancamentoPixDTO(Lancamento lancamento) {
        this.id = lancamento.getId();
        this.valor = lancamento.getValor();
        this.pixOrigin = lancamento.getConta().getChavePIX();
        this.pixDestino = lancamento.getConta2().getChavePIX();
        this.tipo = lancamento.getTipo();
        this.operacao = lancamento.getOperacao();
    }


    public Long getId() {
        return id;
    }
    public String getPixOrigin() {
        return pixOrigin;
    }


    public Double getValor() {
        return valor;
    }

    public String getPixDestino() {
        return pixDestino;
    }



}
