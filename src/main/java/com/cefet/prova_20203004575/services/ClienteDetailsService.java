package com.cefet.prova_20203004575.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cefet.prova_20203004575.Repositories.ClienteRepository;
import com.cefet.prova_20203004575.entities.Cliente;
import com.cefet.prova_20203004575.security.ClienteDetails;

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
