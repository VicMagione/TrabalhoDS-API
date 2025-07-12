package com.cefet.API.services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cefet.API.Repositories.ClienteRepository;
import com.cefet.API.Repositories.ContaRepository;
import com.cefet.API.dto.ContaDTO;
import com.cefet.API.dto.LancamentoDTO;
import com.cefet.API.entities.Conta;
import com.cefet.API.entities.Lancamento;
import com.cefet.API.entities.Operacao;
import com.cefet.API.entities.Tipo;
import com.cefet.API.entities.Cliente;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private LancamentoService lancamentoService;

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
				.orElseThrow(() -> new EntityNotFoundException(
						"Cliente não encontrado com CPF: " + dto.getCliente().getCpf()));

		Conta conta = new Conta();
		String nomeNumero = cliente.getNome(); // Pega o nome do cliente encontrado
		String doisPrimeiros = nomeNumero.substring(0, 2);
		conta.setNumero(doisPrimeiros + "-" + (gerador.nextInt(900000) + 100000));
		conta.setLimite(0.0);
		conta.setSaldo(0.0);
		conta.setClient(cliente);
		conta.setCpf(cliente.getCpf());

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
				.orElseThrow(() -> new EntityNotFoundException(
						"Cliente não encontrada com CPF: " + dto.getCliente().getCpf()));

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

	@Transactional // Esta anotação é crucial
	public void deleteByCpf(String cpf) {
		// Verifica se o cliente existe antes de deletar
		if (!clienteRepository.existsByCpf(cpf)) {
			throw new EntityNotFoundException("Cliente não encontrado com CPF: " + cpf);
		}

		clienteRepository.deleteByCpf(cpf);
	}

	public List<ContaDTO> findByClienteId(Long clienteId) {
		List<Conta> contas = contaRepository.findByClienteId(clienteId);
		return contas.stream()
				.map(ContaDTO::new)
				.toList();
	}

	public List<ContaDTO> findByClienteCpf(String cpf) {
		List<Conta> contas = contaRepository.findByClienteCpf(cpf);
		return contas.stream()
				.map(ContaDTO::new)
				.toList();
	}

	public ContaDTO atualizarChavePIX(Long id, String novaChavePIX) {
		if (contaRepository.existsByChavePIXAndIdNot(novaChavePIX, id)) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Esta chave PIX já está em uso por outra conta");
		}
		Conta conta = contaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + id));

		// Validações opcionais (ex: formato da chave, unicidade)
		if (novaChavePIX == null || novaChavePIX.isBlank()) {
			throw new IllegalArgumentException("Chave PIX inválida!");
		}

		conta.setChavePIX(novaChavePIX);
		Conta contaAtualizada = contaRepository.save(conta);
		return new ContaDTO(contaAtualizada);
	}

	public ContaDTO atualizarChavePIXCpf(String cpf, String novaChavePIX) {

		Conta conta = contaRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com CPF: " + cpf));

		// Validações opcionais (ex: formato da chave, unicidade)
		if (novaChavePIX == null || novaChavePIX.isBlank()) {
			throw new IllegalArgumentException("Chave PIX inválida!");
		}

		conta.setChavePIX(novaChavePIX);
		Conta contaAtualizada = contaRepository.save(conta);
		return new ContaDTO(contaAtualizada);
	}

	public Double calcularSaldoTotalPorClienteId(Long clienteId) {
		Double saldoTotal = contaRepository.sumSaldoByClienteId(clienteId);
		return saldoTotal != null ? saldoTotal : 0.0; // Retorna 0.0 se não houver contas
	}

	public Double calcularSaldoTotalPorClienteCpf(String cpf) {
		Double saldoTotal = contaRepository.sumSaldoByClienteCpf(cpf);
		return saldoTotal != null ? saldoTotal : 0.0; // Retorna 0.0 se não houver contas
	}

	// Buscar por idCliente
	public List<ContaDTO> findByPessoaId(Long idCliente) {
		List<Conta> listaConta = contaRepository.findByClienteId(idCliente);
		return listaConta.stream().map(ContaDTO::new).toList();
	}

	public ContaDTO atualizarLimite(Long id, Double novoLimite) {
		Conta conta = contaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + id));

		// Validações opcionais (ex: formato da chave, unicidade)
		if (novoLimite == null) {
			throw new IllegalArgumentException("Limite inválida!");
		}

		conta.setLimite(novoLimite);
		Conta contaAtualizada = contaRepository.save(conta);
		return new ContaDTO(contaAtualizada);
	}

	public boolean existeChavePix(String chave) {
		return contaRepository.existsByChavePIX(chave);
	}

	@Transactional
	public ContaDTO realizarSaque(Long idConta, Double valor) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + idConta));

		if (valor <= 0) {
			throw new IllegalArgumentException("Valor do saque deve ser positivo");
		}

		if (conta.getSaldo() + conta.getLimite() < valor) {
			throw new IllegalArgumentException("Saldo insuficiente para realizar o saque");
		}

		// Cria o lançamento de saque
		Lancamento lancamento = new Lancamento();
		lancamento.setValor(valor);
		lancamento.setConta(conta);
		lancamento.setOperacao(Operacao.SAQUE);
		lancamento.setTipo(Tipo.DEBITO); // ou outro tipo que você queira usar

		lancamentoService.insert(new LancamentoDTO(lancamento));

		return new ContaDTO(conta);
	}

	@Transactional
	public ContaDTO realizarDeposito(Long idConta, Double valor) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + idConta));

		if (valor <= 0) {
			throw new IllegalArgumentException("Valor do Deposito deve ser positivo");
		}

		Lancamento lancamento = new Lancamento();
		lancamento.setValor(valor);
		lancamento.setConta(conta);
		lancamento.setOperacao(Operacao.DEPOSITO);
		lancamento.setTipo(Tipo.CREDITO);

		if (valor>conta.getCliente().getSaldoTotal()) {
			Lancamento lancamento2 = new Lancamento();
			lancamento2.setValor(valor * 0.10);
			lancamento2.setConta(conta);
			lancamento2.setOperacao(Operacao.BONUS);
			lancamento2.setTipo(Tipo.CREDITO);
			lancamentoService.insert(new LancamentoDTO(lancamento2));

		}

		lancamentoService.insert(new LancamentoDTO(lancamento));

		return new ContaDTO(conta);
	}

	@Transactional
	public ContaDTO realizarTransferencia(Long idConta, Long idConta2, Double valor) {
		Conta conta = contaRepository.findById(idConta)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + idConta));
		Conta conta2 = contaRepository.findById(idConta2)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + idConta));

		if (valor <= 0) {
			throw new IllegalArgumentException("Valor da Transferencia deve ser positivo");
		}

		if (conta.getSaldo() + conta.getLimite() < valor) {
			throw new IllegalArgumentException("Saldo insuficiente para realizar a Transferencia");
		}
		Lancamento lancamento = new Lancamento();
		lancamento.setValor(valor);
		lancamento.setConta(conta);
		lancamento.setOperacao(Operacao.TRANSFERENCIA);
		lancamento.setTipo(Tipo.DEBITO);
		lancamentoService.insert(new LancamentoDTO(lancamento));

		Lancamento lancamento2 = new Lancamento();
		lancamento2.setValor(valor);
		lancamento2.setConta(conta2);
		lancamento2.setOperacao(Operacao.TRANSFERENCIA);
		lancamento2.setTipo(Tipo.CREDITO);
		lancamentoService.insert(new LancamentoDTO(lancamento2));

		if (lancamento2.getConta().getCliente() == lancamento.getConta().getCliente()) {
			Lancamento lancamento3 = new Lancamento();
			lancamento3.setValor(valor * 0.10);
			lancamento3.setConta(conta);
			lancamento3.setOperacao(Operacao.TAXA);
			lancamento3.setTipo(Tipo.DEBITO);
			lancamentoService.insert(new LancamentoDTO(lancamento3));

		}

		return new ContaDTO(conta);
	}

	@Transactional
	public ContaDTO realizarPix(String pix1, String pix2, Double valor) {
		Conta conta = contaRepository.findByChavePIX(pix1)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com Pix: " + pix1));
		Conta conta2 = contaRepository.findByChavePIX(pix2)
				.orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com Px: " + pix2));

		if (valor <= 0) {
			throw new IllegalArgumentException("Valor da Transferencia deve ser positivo");
		}

		if (conta.getSaldo() + conta.getLimite() < valor) {
			throw new IllegalArgumentException("Saldo insuficiente para realizar a Transferencia");
		}
		Lancamento lancamento = new Lancamento();
		lancamento.setValor(valor);
		lancamento.setConta(conta);
		lancamento.setOperacao(Operacao.PIX);
		lancamento.setTipo(Tipo.DEBITO);

		Lancamento lancamento2 = new Lancamento();
		lancamento2.setValor(valor);
		lancamento2.setConta(conta2);
		lancamento2.setOperacao(Operacao.PIX);
		lancamento2.setTipo(Tipo.CREDITO);

		lancamentoService.insert(new LancamentoDTO(lancamento));
		lancamentoService.insert(new LancamentoDTO(lancamento2));

		return new ContaDTO(conta);
	}

}
