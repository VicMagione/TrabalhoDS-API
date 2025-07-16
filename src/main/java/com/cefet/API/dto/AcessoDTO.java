package com.cefet.API.dto;

import java.sql.Date;

import com.cefet.API.entities.Acesso;



public class AcessoDTO {
    
    private Long id;

    private ClienteDTO cliente;

    private Date data;


    public AcessoDTO(){

    }

    public AcessoDTO(Acesso acesso) {
        this.id = acesso.getId();
        this.cliente = new ClienteDTO(acesso.getCliente());
        this.data = acesso.getData();
    }

    public Long getId() {
        return id;
    }

    

    public Date getData() {
        return data;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }
}
