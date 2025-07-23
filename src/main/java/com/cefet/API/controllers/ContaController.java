package com.cefet.API.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.API.dto.ContaDTO;
import com.cefet.API.entities.Conta;
import com.cefet.API.services.ContaService;

@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://trabalhodsvicmagi.netlify.app"
})@RestController
@RequestMapping("/contas")
public class ContaController {

	@Autowired
	private ContaService contaService;

	@GetMapping("/{id}")
	public ResponseEntity<ContaDTO> findById(@PathVariable Long id) {
		ContaDTO contaDTO = contaService.findById(id);
		return ResponseEntity.ok(contaDTO);
	}

	@GetMapping
	public ResponseEntity<List<ContaDTO>> findAll() {
		List<ContaDTO> contaDTOs = contaService.findAll();
		return ResponseEntity.ok(contaDTOs);
	}

	@GetMapping("/cliente/{clienteId}") // Exemplo: /contas/cliente/1
	public ResponseEntity<List<ContaDTO>> getContasPorCliente(@PathVariable Long clienteId) {
		List<ContaDTO> contas = contaService.findByClienteId(clienteId);
		return ResponseEntity.ok(contas);
	}

	@GetMapping("/cliente/cpf/{cpf}") // Exemplo: /contas/cliente/cpf/142
	public ResponseEntity<List<ContaDTO>> getContasPorClienteCPF(@PathVariable String cpf) {
		List<ContaDTO> contas = contaService.findByClienteCpf(cpf);
		return ResponseEntity.ok(contas);
	}

	@GetMapping("/cliente/{clienteId}/saldo-total")
	public ResponseEntity<Double> getSaldoTotalPorCliente(@PathVariable Long clienteId) {
		Double saldoTotal = contaService.calcularSaldoTotalPorClienteId(clienteId);
		return ResponseEntity.ok(saldoTotal);
	}

	@GetMapping("/cliente/cpf/{cpf}/saldo-total")
	public ResponseEntity<Double> getSaldoTotalPorCliente(@PathVariable String cpf) {
		Double saldoTotal = contaService.calcularSaldoTotalPorClienteCpf(cpf);
		return ResponseEntity.ok(saldoTotal);
	}

	@PostMapping
	public ResponseEntity<Conta> create(@RequestBody Conta conta) {
		Conta contaNovo = contaService.insert(conta);
		return ResponseEntity.status(201).body(contaNovo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ContaDTO> update(@PathVariable Long id, @RequestBody ContaDTO contaDTO) {
		ContaDTO contaAtualizado = contaService.update(id, contaDTO);
		return ResponseEntity.ok(contaAtualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		contaService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/cpf/{cpf}")
	public ResponseEntity<Void> delete(@PathVariable String cpf) {
		contaService.deleteByCpf(cpf);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/chave-pix") // Endpoint dedicado para atualizar apenas a Chave PIX
	public ResponseEntity<ContaDTO> atualizarChavePIX(
			@PathVariable Long id,
			@RequestBody Map<String, String> request // Recebe um JSON com a chave
	) {
		String novaChavePIX = request.get("chavePIX");
		ContaDTO contaAtualizada = contaService.atualizarChavePIX(id, novaChavePIX);
		return ResponseEntity.ok(contaAtualizada);
	}

	@PatchMapping("/cpf/{cpf}/chave-pix") // Endpoint dedicado para atualizar apenas a Chave PIX
	public ResponseEntity<ContaDTO> atualizarChavePIXCpf(
			@PathVariable String cpf,
			@RequestBody Map<String, String> request // Recebe um JSON com a chave
	) {
		String novaChavePIX = request.get("chavePIX");
		ContaDTO contaAtualizada = contaService.atualizarChavePIXCpf(cpf, novaChavePIX);
		return ResponseEntity.ok(contaAtualizada);
	}

	@PatchMapping("{id}/limite") // Endpoint dedicado para atualizar apenas a Chave PIX
	public ResponseEntity<ContaDTO> atualizarLimite(
			@PathVariable Long id,
			@RequestBody Map<String, Double> request // Recebe um JSON com a chave
	) {
		Double novoLimite = request.get("limite");
		ContaDTO contaAtualizada = contaService.atualizarLimite(id, novoLimite);
		return ResponseEntity.ok(contaAtualizada);
	}

	@GetMapping("/verificar-chave-pix/{chave}")
	public ResponseEntity<Boolean> verificarChavePixExistente(
			@RequestParam String chave) {
		boolean existe = contaService.existeChavePix(chave);
		return ResponseEntity.ok(existe);
	}

	// Adicione este método ao ContaController.java
	@PatchMapping("/{id}/saque")
	public ResponseEntity<ContaDTO> realizarSaque(
			@PathVariable Long id,
			@RequestBody Map<String, Double> request) {
		Double valor = request.get("valor");
		ContaDTO contaAtualizada = contaService.realizarSaque(id, valor);
		return ResponseEntity.ok(contaAtualizada);
	}

	@PatchMapping("/{id}/deposito")
	public ResponseEntity<ContaDTO> realizarDeposito(
			@PathVariable Long id,
			@RequestBody Map<String, Double> request) {
		Double valor = request.get("valor");
		ContaDTO contaAtualizada = contaService.realizarDeposito(id, valor);
		return ResponseEntity.ok(contaAtualizada);
	}

	@PatchMapping("/{id}/{id2}/transferencia")
	public ResponseEntity<ContaDTO> realizarTransferencia(
			@PathVariable Long id,
			@PathVariable Long id2,
			@RequestBody Map<String, Double> request) {
		Double valor = request.get("valor");
		ContaDTO contaAtualizada = contaService.realizarTransferencia(id,id2, valor);
		return ResponseEntity.ok(contaAtualizada);
	}

	@PatchMapping("/{pix1}/{pix2}/pix")
	public ResponseEntity<ContaDTO> realizarPix(
			@PathVariable String pix1,
			@PathVariable String pix2,
			@RequestBody Map<String, Double> request) {
		Double valor = request.get("valor");
		ContaDTO contaAtualizada = contaService.realizarPix(pix1,pix2, valor);
		return ResponseEntity.ok(contaAtualizada);
	}
}
