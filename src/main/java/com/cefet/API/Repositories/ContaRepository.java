package com.cefet.API.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cefet.API.entities.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

        boolean existsByNumero(String Numero);

        List<Conta> findByClienteId(Long clienteId);

        List<Conta> findByClienteCpf(String cpf);

        @Query("SELECT SUM(c.saldo) FROM Conta c WHERE c.cliente.id = :clienteId")
        Double sumSaldoByClienteId(@Param("clienteId") Long clienteId);

        @Query("SELECT SUM(c.saldo) FROM Conta c WHERE c.cliente.cpf = :cpf")
        Double sumSaldoByClienteCpf(@Param("cpf") String cpf);

        Optional<Conta> findByCpf(String cpf);

        Optional<Conta> findByChavePIX(String chavePIX);

        boolean existsByCpf(String cpf);

        void deleteByCpf(String cpf);

        boolean existsByChavePIX(String chavePix);

        boolean existsByChavePIXAndIdNot(String novaChavePIX, Long id);

}
