package com.cefet.prova_20203004575.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cefet.prova_20203004575.Repositories.ClienteRepository;
import com.cefet.prova_20203004575.dto.ClienteDTO;
import com.cefet.prova_20203004575.entities.Cliente;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {

    @Autowired
    private final ClienteRepository clienteRepository;

    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cliente criarCliente(Cliente cliente) {
        if (clienteRepository.existsByLogin(cliente.getLogin())) {
            throw new IllegalArgumentException("Login já existe.");
        }

        cliente.setNome(cliente.getNome());
        cliente.setCpf(cliente.getCpf());
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        return clienteRepository.save(cliente);
    }

    // Buscar todos
    public List<ClienteDTO> findAll() {
        List<Cliente> listaCliente = clienteRepository.findAll();
        return listaCliente.stream().map(ClienteDTO::new).toList();
    }

    // Buscar por ID
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrada com ID: " + id));
        return new ClienteDTO(cliente);
    }

    // Inserir Cliente
    public ClienteDTO insert(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        Cliente pessoaSalvo = clienteRepository.save(cliente);
        return new ClienteDTO(pessoaSalvo);
    }

    // Atualizar Tipo
    public ClienteDTO update(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrada com ID: " + id));
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return new ClienteDTO(clienteAtualizado);
    }

    // Remover por ID
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrada com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
