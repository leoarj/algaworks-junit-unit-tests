package com.algaworks.junit.utilidade;

import java.math.BigDecimal;
import java.util.Objects;

public class ContaBancaria {

    private BigDecimal saldo = BigDecimal.ZERO;

    public ContaBancaria(BigDecimal saldo) {
        //DONE 1 - validar saldo: não pode ser nulo, caso seja, deve lançar uma IllegalArgumentException
        if (Objects.isNull(saldo)) {
            throw new IllegalArgumentException("Saldo não pode ser nulo");
        }
        //DONE 2 - pode ser zero ou negativo
        this.saldo = this.saldo.add(saldo);
    }

    public void saque(BigDecimal valor) {
        //DONE 1 - validar valor: não pode ser nulo, zero ou menor que zero, caso seja, deve lançar uma IllegalArgumentException
        validarValor(valor);

        //DONE 2 - Deve subtrair o valor do saldo
        //DONE 3 - Se o saldo for insuficiente deve lançar uma RuntimeException
        if (saldo.compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        saldo = saldo.subtract(valor);
    }

    public void deposito(BigDecimal valor) {
        //DONE 1 - validar valor: não pode ser nulo, zero ou menor que zero, caso seja, deve lançar uma IllegalArgumentException
        validarValor(valor);
        //TODO 2 - Deve adicionar o valor ao saldo
        saldo = saldo.add(valor);
    }

    public BigDecimal saldo() {
        //DONE 1 - retornar saldo
        return this.saldo;
    }

    private void validarValor(BigDecimal valor) {
        if (Objects.isNull(valor)) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }

        switch (valor.compareTo(BigDecimal.ZERO)) {
            case -1 -> throw new IllegalArgumentException("Valor não pode ser negativo");
            case 0 -> throw new IllegalArgumentException("Valor não pode ser zero");
        }
    }
}
