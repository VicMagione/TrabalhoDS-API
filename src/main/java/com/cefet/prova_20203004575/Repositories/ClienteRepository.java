package com.cefet.prova_20203004575.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cefet.prova_20203004575.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente,Long>{

    boolean existsByCpf(String cpf);
    Optional<Cliente> findByCpf(String cpf);

}
