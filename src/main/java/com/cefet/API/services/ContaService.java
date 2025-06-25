package com.cefet.API.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cefet.API.Repositories.ClienteRepository;
import com.cefet.API.Repositories.ContaRepository;
import com.cefet.API.dto.ContaDTO;
import com.cefet.API.entities.Conta;
import com.cefet.API.entities.Cliente;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ClienteRepository clienteRepository;


	// Buscar todos
	public List<ContaDTO> findAll() {
		List<Conta> listaConta = contaRepository.findAll();
		return listaConta.stream().map(ContaDTO::new).toList();
	}

	// Buscar por ID
	public ContaDTO findById(Long id) {
		Conta conta = contaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + id));
		return new ContaDTO(conta);
	}

	// Inserir Cliente
	public Conta insert(Conta dto) {
    Random gerador = new Random();
    Cliente cliente = clienteRepository.findByCpf(dto.getCliente().getCpf())
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com CPF: " + dto.getCliente().getCpf()));

    Conta conta = new Conta();
    String nomeNumero = cliente.getNome(); // Pega o nome do cliente encontrado
    String doisPrimeiros = nomeNumero.substring(0, 2);
    conta.setNumero(doisPrimeiros + "-" + (gerador.nextInt(900000)+100000));
    conta.setLimite(0.0);
    conta.setSaldo(0.0);
    conta.setClient(cliente);

    return contaRepository.save(conta);
}

	// Atualizar Tipo
	public ContaDTO update(Long id, ContaDTO dto) {
		Conta conta = contaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + id));

		// Se o novo número informado for diferente do atual, verifica se já existe
		// outra conta com esse número
		if (!dto.getNumero().equals(conta.getNumero()) && contaRepository.existsByNumero(dto.getNumero())) {
			throw new IllegalArgumentException("Já existe uma conta com o número: " + dto.getNumero());
		}

		Cliente cliente = clienteRepository.findByCpf(dto.getCliente().getCpf())
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrada com CPF: " + dto.getCliente().getCpf()));

		conta.setNumero(dto.getNumero());
		conta.setClient(cliente);

		Conta contaAtualizado = contaRepository.save(conta);
		return new ContaDTO(contaAtualizado);
	}

	// Remover por ID
	public void delete(Long id) {
		if (!contaRepository.existsById(id)) {
			throw new EntityNotFoundException("Conta não encontrada com ID: " + id);
		}
		contaRepository.deleteById(id);
	}

	// Buscar por idCliente
	public List<ContaDTO> findByPessoaId(Long idCliente) {
		List<Conta> listaConta = contaRepository.findByClienteId(idCliente);
		return listaConta.stream().map(ContaDTO::new).toList();
	}
}
