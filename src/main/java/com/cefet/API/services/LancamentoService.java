package com.cefet.API.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cefet.API.Repositories.ContaRepository;
import com.cefet.API.Repositories.LancamentoRepository;
import com.cefet.API.dto.LancamentoDTO;
import com.cefet.API.entities.Conta;
import com.cefet.API.entities.Lancamento;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LancamentoService {
    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
	private ContaRepository contaRepository;

    // Buscar todas os lancamentos
    public List<LancamentoDTO> findAll() {
        List<Lancamento> listaLancamentos = lancamentoRepository.findAll();
        return listaLancamentos.stream().map(LancamentoDTO::new).toList();
    }

    // Buscar lancamento por ID
    public LancamentoDTO findById(Long id) {
        Lancamento lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lancamento não encontrada com ID: " + id));
        return new LancamentoDTO(lancamento);
    }

    // Inserir Lancamento
    public LancamentoDTO insert(LancamentoDTO lancamentoDTO) {
    Conta conta = contaRepository.findById(lancamentoDTO.getIdConta())
        .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + lancamentoDTO.getIdConta()));

    Lancamento lancamento = new Lancamento();
    lancamento.setValor(lancamentoDTO.getValor());
    lancamento.setConta(conta);

    // Atualiza o saldo da conta
    conta.setSaldo(conta.getSaldo() + lancamento.getValor());

    contaRepository.save(conta); // Salva a conta com saldo atualizado
    lancamentoRepository.save(lancamento); // Salva o lançamento

    return new LancamentoDTO(lancamento);
}

    // Atualizar Lancamento
    public LancamentoDTO update(Long id,LancamentoDTO lancamentoDTO) {
        Lancamento lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lancamento não encontrado com ID: " + id));
        // Garante que o lancamento não seja alterado
        if (!lancamento.getValor().equals(lancamentoDTO.getValor())) {
            throw new IllegalArgumentException("Não é permitido alterar o Valor.");
        }
        lancamento.setConta(lancamentoDTO.getConta());
        Lancamento lancamentoAtualizado = lancamentoRepository.save(lancamento);
        return new LancamentoDTO(lancamentoAtualizado);
    }

    // Remover por ID
    public void delete(Long id) {
        if (!lancamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Lancamento não encontrado com ID: " + id);
        }
        lancamentoRepository.deleteById(id);
    }


}
