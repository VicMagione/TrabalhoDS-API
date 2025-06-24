package com.cefet.prova_20203004575.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.prova_20203004575.dto.ContaDTO;
import com.cefet.prova_20203004575.entities.Conta;
import com.cefet.prova_20203004575.services.ContaService;

@RestController
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
}

