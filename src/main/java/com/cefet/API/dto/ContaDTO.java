package com.cefet.API.dto;

import com.cefet.API.entities.Conta;

public class ContaDTO {

    private Long id;
    private String numero;
    private Double saldo;
    private Double limite;
    private String chavePIX;
    private ClienteDTO cliente;
    


    public Double getLimite() {
        return limite;
    }
    public ContaDTO() {
    }
    public Double getSaldo(){
        return saldo;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    
    public ContaDTO(Conta conta) {
        this.id = conta.getId();
        this.numero = conta.getNumero();
        this.saldo = conta.getSaldo();
        this.limite = conta.getLimite();
        this.cliente = new ClienteDTO(conta.getCliente());
        this.chavePIX = conta.getChavePIX();
    }
    public String getChavePIX() {
        return chavePIX;
    }
    

}

