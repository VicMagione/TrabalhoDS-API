package com.cefet.API.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_lancamento")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_conta", nullable = false)
    private Conta conta;

    @ManyToOne
    @JoinColumn(name = "id_conta2")
    private Conta conta2;

    @Column(nullable = false)
    private Double valor;

    @Column
    private Double bonus_taxa;

    @Enumerated(EnumType.STRING)
    @Column(name= "Tipo",nullable = false)
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name= "Operacao",nullable = false)
    private Operacao operacao;

    public Lancamento() {
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public Lancamento(Long id, Conta conta, Double valor) {
        this.id = id;
        this.conta = conta;
        this.valor = valor;
    }

   
    public Lancamento(Long id, Conta conta, Double valor, Double bonus_taxa, Tipo tipo, Operacao operacao) {
        this.id = id;
        this.conta = conta;
        this.valor = valor;
        this.bonus_taxa = bonus_taxa;
        this.tipo = tipo;
        this.operacao = operacao;
    }

    public Lancamento(Long id, Conta conta, Double valor, Tipo tipo, Operacao operacao) {
        this.id = id;
        this.conta = conta;
        this.valor = valor;
        this.tipo = tipo;
        this.operacao = operacao;
    }

    public Lancamento(Long id, Conta conta,Conta conta2, Double valor, Tipo tipo, Operacao operacao) {
        this.id = id;
        this.conta = conta;
        this.conta2 = conta2;
        this.valor = valor;
        this.tipo = tipo;
        this.operacao = operacao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((conta == null) ? 0 : conta.hashCode());
        long temp;
        temp = Double.doubleToLongBits(valor);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lancamento other = (Lancamento) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (conta == null) {
            if (other.conta != null)
                return false;
        } else if (!conta.equals(other.conta))
            return false;
        if (Double.doubleToLongBits(valor) != Double.doubleToLongBits(other.valor))
            return false;
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void setDescricao(Object descricao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDescricao'");
    }

    public Double getBonus_taxa() {
        return bonus_taxa;
    }

    public void setBonus_taxa(Double bonus_taxa) {
        this.bonus_taxa = bonus_taxa;
    }

    public Conta getConta2() {
        return conta2;
    }

    public void setConta2(Conta conta2) {
        this.conta2 = conta2;
    }

}
