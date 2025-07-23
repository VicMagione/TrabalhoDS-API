package com.cefet.API.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.API.dto.AcessoDTO;

import com.cefet.API.services.AcessoService;

@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://trabalhodsvicmagi.netlify.app"
})
@RestController
@RequestMapping("/acessos")
public class AcessoController {
    @Autowired
    private AcessoService acessoService;

    @PostMapping
	public ResponseEntity<AcessoDTO> create(@RequestBody AcessoDTO acessoDTO) {
		AcessoDTO acessoNovo = acessoService.insert(acessoDTO);
		return ResponseEntity.status(201).body(acessoNovo);
	}

    @GetMapping
    public ResponseEntity<List<AcessoDTO>> findAll() {
        List<AcessoDTO> usuarios = acessoService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        acessoService.delete(id);
        return ResponseEntity.noContent().build();
    }

   

}
