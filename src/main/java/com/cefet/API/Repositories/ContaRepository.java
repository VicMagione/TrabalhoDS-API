package com.cefet.API.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cefet.API.entities.Conta;

public interface ContaRepository extends JpaRepository<Conta,Long>{

        boolean existsByNumero(String Numero);
        List<Conta> findByClienteId(Long clienteId);


}
