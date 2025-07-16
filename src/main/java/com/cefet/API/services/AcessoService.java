package com.cefet.API.services;

import org.springframework.stereotype.Service;

import com.cefet.API.Repositories.AcessoRepoisitory;
import com.cefet.API.Repositories.ClienteRepository;
import com.cefet.API.dto.AcessoDTO;
import com.cefet.API.entities.Acesso;
import com.cefet.API.entities.Cliente;

import jakarta.persistence.EntityNotFoundException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
@Service
public class AcessoService {

    @Autowired
    private AcessoRepoisitory acessoRepoisitory;

    @Autowired
	private ClienteRepository clienteRepository;

    public List<AcessoDTO> findAll() {
        List<Acesso> listaConta = acessoRepoisitory.findAll();
        return listaConta.stream().map(AcessoDTO::new).toList();
    }

    // Buscar por ID
    public AcessoDTO findById(Long id) {
        Acesso acesso = acessoRepoisitory.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Acesso não encontrada com ID: " + id));
        return new AcessoDTO(acesso);
    }

    public void delete(Long id) {
        if (!acessoRepoisitory.existsById(id)) {
            throw new EntityNotFoundException("Conta não encontrada com ID: " + id);
        }
        acessoRepoisitory.deleteById(id);
    }

    

    public AcessoDTO insert(AcessoDTO dto) {
        Cliente cliente = clienteRepository.findByCpf(dto.getCliente().getCpf())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cliente não encontrado com CPF: " + dto.getCliente().getCpf()));

        Acesso acesso = new Acesso();
        acesso.setCliente(cliente);
        acesso.setData(Date.valueOf(LocalDate.now()));

        acessoRepoisitory.save(acesso);

        return new AcessoDTO(acesso);
    }

    

}
