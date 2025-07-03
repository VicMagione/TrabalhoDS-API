package com.cefet.API.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cefet.API.Repositories.ClienteRepository;
import com.cefet.API.dto.ClienteDTO;
import com.cefet.API.entities.Cliente;

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
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF já existe.");
        }

        cliente.setNome(cliente.getNome());
        cliente.setCpf(cliente.getCpf());
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setEmail(cliente.getEmail());
        cliente.setTelefone(cliente.getTelefone());
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

    // Buscar por ID
    public ClienteDTO findByCPF(String cpf) {
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrada com CPF: " + cpf));
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

    // Atualizar 
    public ClienteDTO update(Long id, Cliente dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrada com ID: " + id));
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return new ClienteDTO(clienteAtualizado);
    }
    public ClienteDTO update(String cpf, Cliente dto) {
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrada com cpf: " + cpf));
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
         cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
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

    @Transactional // Esta anotação é crucial
    public void deleteByCpf(String cpf) {
        // Verifica se o cliente existe antes de deletar
        if (!clienteRepository.existsByCpf(cpf)) {
            throw new EntityNotFoundException("Cliente não encontrado com CPF: " + cpf);
        }
        
        clienteRepository.deleteByCpf(cpf);
    }

    
}
