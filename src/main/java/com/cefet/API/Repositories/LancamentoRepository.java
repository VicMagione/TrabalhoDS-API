package com.cefet.API.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cefet.API.entities.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findByContaId(Long contaId);

}