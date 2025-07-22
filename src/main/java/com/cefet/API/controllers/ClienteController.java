package com.cefet.API.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.API.dto.ClienteDTO;
import com.cefet.API.entities.Cliente;
import com.cefet.API.services.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO> findByCPF(@PathVariable String cpf) {
        ClienteDTO clienteDTO = clienteService.findByCPF(cpf);
        return ResponseEntity.ok(clienteDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Long id) {
        ClienteDTO clienteDTO = clienteService.findById(id);
        return ResponseEntity.ok(clienteDTO);
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Cliente cliente) {
        try {
            Cliente novoUsuario = clienteService.criarCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<ClienteDTO> usuarios = clienteService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Long id, @RequestBody Cliente clienteDTO) {
        ClienteDTO pessoaAtualizado = clienteService.update(id, clienteDTO);
        return ResponseEntity.ok(pessoaAtualizado);
    }

    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO> update(@PathVariable String cpf, @RequestBody Cliente clienteDTO) {
        ClienteDTO pessoaAtualizado = clienteService.update(cpf, clienteDTO);
        return ResponseEntity.ok(pessoaAtualizado);
    }

    @PutMapping("/senha/{id}")
    public ResponseEntity<ClienteDTO> updateSenha(@PathVariable Long id, @RequestBody Cliente clienteDTO) {
        ClienteDTO pessoaAtualizado = clienteService.updateSenha(id, clienteDTO);
        return ResponseEntity.ok(pessoaAtualizado);
    }

    @PutMapping("/senha/cpf/{cpf}")
    public ResponseEntity<ClienteDTO> updateSenha(@PathVariable String cpf, @RequestBody Cliente clienteDTO) {
        ClienteDTO pessoaAtualizado = clienteService.updateSenha(cpf, clienteDTO);
        return ResponseEntity.ok(pessoaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        clienteService.deleteByCpf(cpf);
        return ResponseEntity.noContent().build();
    }
}
