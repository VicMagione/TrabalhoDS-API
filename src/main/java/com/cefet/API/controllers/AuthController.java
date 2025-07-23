package com.cefet.API.controllers;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.API.Repositories.AcessoRepoisitory;
import com.cefet.API.Repositories.ClienteRepository;
import com.cefet.API.dto.JwtAuthenticationDTO;
import com.cefet.API.dto.LoginDTO;
import com.cefet.API.entities.Acesso;
import com.cefet.API.entities.Cliente;
import com.cefet.API.security.JwtTokenProvider;

import jakarta.persistence.EntityNotFoundException;

@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://trabalhodsvicmagi.netlify.app"
})@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private AcessoRepoisitory acessoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getCpf(),
                        loginDTO.getSenha()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);

    Cliente cliente = clienteRepository.findByCpf(loginDTO.getCpf())
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com CPF: " + loginDTO.getCpf()));

    Acesso acesso = new Acesso();
    acesso.setCliente(cliente);
    acesso.setData(Date.valueOf(LocalDate.now())); 
    acessoRepository.save(acesso);

    return ResponseEntity.ok(new JwtAuthenticationDTO(jwt));
    }
}
