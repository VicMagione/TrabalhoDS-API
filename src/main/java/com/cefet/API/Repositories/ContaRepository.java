package com.cefet.API.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cefet.API.entities.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

        boolean existsByNumero(String Numero);

        List<Conta> findByClienteId(Long clienteId);

        @Query("SELECT SUM(c.saldo) FROM Conta c WHERE c.cliente.id = :clienteId")
        Double sumSaldoByClienteId(@Param("clienteId") Long clienteId);

}
