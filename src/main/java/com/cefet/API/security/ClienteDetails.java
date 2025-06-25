package com.cefet.API.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cefet.API.entities.Cliente;

public class ClienteDetails implements UserDetails {

    private final Cliente cliente;

    public ClienteDetails(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + cliente.getNivelAcesso().name()));
    }

    @Override
    public String getPassword() {
        return cliente.getSenha();
    }

    @Override
    public String getUsername() {
        return cliente.getCpf();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
