package com.cefet.prova_20203004575.dto;

import com.cefet.prova_20203004575.entities.Cliente;

public class ClienteDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;

    public String getEmail() {
        return email;
    }
    public String getTelefone() {
        return telefone;
    }
    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
    }
    public ClienteDTO() {
    }
    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getCpf() {
        return cpf;
    }
}
