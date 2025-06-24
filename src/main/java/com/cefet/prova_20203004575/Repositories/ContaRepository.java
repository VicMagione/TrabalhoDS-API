package com.cefet.prova_20203004575.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cefet.prova_20203004575.entities.Conta;

public interface ContaRepository extends JpaRepository<Conta,Long>{

        boolean existsByNumero(String Numero);
        List<Conta> findByClienteId(Long clienteId);


}
