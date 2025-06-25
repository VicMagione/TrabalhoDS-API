package com.cefet.API.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cefet.API.Repositories.ClienteRepository;
import com.cefet.API.entities.Cliente;
import com.cefet.API.security.ClienteDetails;

@Service
public class ClienteDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public ClienteDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Cliente usuario = clienteRepository.findByCpf(cpf)
            .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado: " + cpf));
        return new ClienteDetails(usuario);
    }
}
