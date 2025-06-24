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

import com.cefet.prova_20203004575.dto.LancamentoDTO;
import com.cefet.prova_20203004575.services.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
    @Autowired
    private LancamentoService lancamentoService;

    @GetMapping("/{id}")
    public ResponseEntity<LancamentoDTO> findById(@PathVariable Long id) {
        LancamentoDTO lancamentoDTO = lancamentoService.findById(id);
        return ResponseEntity.ok(lancamentoDTO);
    }

    @GetMapping
    public ResponseEntity<List<LancamentoDTO>> findAll() {
        List<LancamentoDTO> lancamentos = lancamentoService.findAll();
        return ResponseEntity.ok(lancamentos);
    }

    @PostMapping
    public ResponseEntity<LancamentoDTO> create(@RequestBody LancamentoDTO lancamentoDTO) {
        LancamentoDTO lancamentoNovo = lancamentoService.insert(lancamentoDTO);
        return ResponseEntity.status(201).body(lancamentoNovo);
    }

     @PutMapping("/{id}")
    public ResponseEntity<LancamentoDTO> update(@PathVariable Long id, @RequestBody LancamentoDTO pessoaDTO) {
        LancamentoDTO lancamentoAtualizado = lancamentoService.update(id, pessoaDTO);
        return ResponseEntity.ok(lancamentoAtualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lancamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
