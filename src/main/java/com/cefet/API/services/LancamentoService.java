package com.cefet.API.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cefet.API.Repositories.ClienteRepository;
import com.cefet.API.Repositories.ContaRepository;
import com.cefet.API.Repositories.LancamentoRepository;
import com.cefet.API.dto.LancamentoDTO;
import com.cefet.API.dto.LancamentoPixDTO;
import com.cefet.API.dto.LancamentoTransferenciaDTO;
import com.cefet.API.entities.Cliente;
import com.cefet.API.entities.Conta;
import com.cefet.API.entities.Lancamento;
import com.cefet.API.entities.Operacao;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LancamentoService {
    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Conta não encontrada com ID: " + lancamentoDTO.getIdConta()));

        Cliente cliente = conta.getCliente();
        Lancamento lancamento = new Lancamento();
        lancamento.setValor(lancamentoDTO.getValor());
        lancamento.setConta(conta);
        lancamento.setOperacao(lancamentoDTO.getOperacao());
        lancamento.setTipo(lancamentoDTO.getTipo());

        Operacao op1 = Operacao.DEPOSITO;
        Operacao op2 = Operacao.SAQUE;
        if (lancamento.getOperacao() == op1) {
            if (lancamento.getValor() >= lancamento.getConta().getCliente().getSaldoTotal()) {
                lancamento.setBonus_taxa(lancamento.getValor() * 0.1);
                conta.setSaldo(conta.getSaldo() + lancamento.getValor() + lancamento.getValor() * 0.1);
                cliente.setSaldoTotal(cliente.getSaldoTotal() + lancamento.getValor() + lancamento.getValor() * 0.1);
            } else {
                conta.setSaldo(conta.getSaldo() + lancamento.getValor());
                cliente.setSaldoTotal(cliente.getSaldoTotal() + lancamento.getValor());
            }
        }

        if (lancamento.getOperacao() == op2) {
            conta.setSaldo(conta.getSaldo() - lancamento.getValor());
            cliente.setSaldoTotal(cliente.getSaldoTotal() - lancamento.getValor());
        }

        clienteRepository.save(cliente);
        contaRepository.save(conta);
        lancamentoRepository.save(lancamento);

        return new LancamentoDTO(lancamento);
    }

    public LancamentoDTO insertTransferencia(LancamentoTransferenciaDTO lancamentoDTO) {
        Conta conta = contaRepository.findById(lancamentoDTO.getIdContaOrigin())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Conta Origem não encontrada com ID: " + lancamentoDTO.getIdContaOrigin()));

        Conta conta2 = contaRepository.findById(lancamentoDTO.getIdContaDestino())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Conta Destino não encontrada com ID: " + lancamentoDTO.getIdContaDestino()));

        Cliente cliente = conta.getCliente();
        Cliente cliente2 = conta2.getCliente();

        Lancamento lancamento = new Lancamento();
        lancamento.setValor(lancamentoDTO.getValor());
        lancamento.setConta(conta);
        lancamento.setConta2(conta2);
        lancamento.setOperacao(lancamentoDTO.getOperacao());
        lancamento.setTipo(lancamentoDTO.getTipo());

        if (cliente.getId() == cliente2.getId()) {
            lancamento.setBonus_taxa(lancamento.getValor() * 0.1);

            conta.setSaldo(conta.getSaldo() - lancamento.getValor()-(lancamento.getValor() * 0.1));
            cliente.setSaldoTotal(cliente.getSaldoTotal() - lancamento.getValor()-(lancamento.getValor() * 0.1));

            conta2.setSaldo(conta2.getSaldo() + lancamento.getValor());
            cliente2.setSaldoTotal(cliente2.getSaldoTotal() + lancamento.getValor());

        } else {
            conta.setSaldo(conta.getSaldo() - lancamento.getValor());
            cliente.setSaldoTotal(cliente.getSaldoTotal() - lancamento.getValor());

            conta2.setSaldo(conta2.getSaldo() + lancamento.getValor());
            cliente2.setSaldoTotal(cliente2.getSaldoTotal() + lancamento.getValor());
        }

        clienteRepository.save(cliente);
        contaRepository.save(conta);
        clienteRepository.save(cliente2);
        contaRepository.save(conta2);
        lancamentoRepository.save(lancamento);

        return new LancamentoDTO(lancamento);
    }

    public LancamentoDTO insertPIX(LancamentoPixDTO lancamentoDTO) {
        Conta conta = contaRepository.findByChavePIX(lancamentoDTO.getPixOrigin())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Conta Origem não encontrada com ID: " + lancamentoDTO.getPixOrigin()));

        Conta conta2 = contaRepository.findByChavePIX(lancamentoDTO.getPixDestino())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Conta Destino não encontrada com ID: " + lancamentoDTO.getPixDestino()));

        Cliente cliente = conta.getCliente();
        Cliente cliente2 = conta2.getCliente();

        Lancamento lancamento = new Lancamento();
        lancamento.setValor(lancamentoDTO.getValor());
        lancamento.setConta(conta);
        lancamento.setConta2(conta2);
        lancamento.setOperacao(lancamentoDTO.getOperacao());
        lancamento.setTipo(lancamentoDTO.getTipo());

        conta.setSaldo(conta.getSaldo() - lancamento.getValor());
        cliente.setSaldoTotal(cliente.getSaldoTotal() - lancamento.getValor());

        conta2.setSaldo(conta2.getSaldo() + lancamento.getValor());
        cliente2.setSaldoTotal(cliente2.getSaldoTotal() + lancamento.getValor());

        clienteRepository.save(cliente);
        contaRepository.save(conta);
        clienteRepository.save(cliente2);
        contaRepository.save(conta2);
        lancamentoRepository.save(lancamento);

        return new LancamentoDTO(lancamento);
    }

    // Atualizar Lancamento
    public LancamentoDTO update(Long id, Lancamento lancamentoDTO) {
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
