package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ContaBancariaTest {

    /*========== Testes criação conta bancária - INÍCIO ==========*/
    /*
    * Anotação @Nested para organizar os testes em subclasses.
    * */
    @Nested
    class Saldo {
        @Test
        public void deveCriarContaBancariaQuandoSaldoValido() {
            BigDecimal saldoValido = new BigDecimal("1000.00");

            ContaBancaria contaBancaria = assertDoesNotThrow(() -> new ContaBancaria(saldoValido));
            assertEquals(saldoValido, contaBancaria.saldo(),
                    "Saldo da conta bancária criada é diferente do saldo fornecido");
        }

        @Test
        public void deveLancarExceptionAoCriarContaQuandoSaldoNulo() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new ContaBancaria(null));
            assertEquals("Saldo não pode ser nulo", illegalArgumentException.getMessage());
        }
    }
    /*========== Testes criação conta bancária - FIM ==========*/

    /*========== Testes saque - INÍCIO ==========*/
    @Nested
    class Saque {
        @Test
        public void deveSacarQuandoValorMenor() {
            BigDecimal saldoInicial = new BigDecimal("1000.00");
            BigDecimal valorSacar = new BigDecimal("350.75");
            BigDecimal saldoEsperado = saldoInicial.subtract(valorSacar);
            ContaBancaria contaBancaria = new ContaBancaria(saldoInicial);

            assertDoesNotThrow(() -> contaBancaria.saque(valorSacar));
            assertEquals(saldoEsperado, contaBancaria.saldo(), "Saldo da conta após saque é diferente do esperado");
        }

        @Test
        public void deveSacarQuandoValorIgual() {
            BigDecimal valorSacar = new BigDecimal("1000.00");
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000.00"));
            BigDecimal saldoEsperadoZerado = new BigDecimal("0.00"); // por causa da escala .00

            assertDoesNotThrow(() -> contaBancaria.saque(valorSacar));
            assertEquals(saldoEsperadoZerado, contaBancaria.saldo(), "Saldo da conta após saque é diferente do esperado");
        }

        @Test
        public void deveLancarExceptionAoSacarQuandoValorNulo() {
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));

            IllegalArgumentException IllegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> contaBancaria.saque(null));
            assertEquals("Valor não pode ser nulo", IllegalArgumentException.getMessage());
        }

        @Test
        public void deveLancarExceptionAoSacarQuandoValorZero() {
            BigDecimal valorInvalido = BigDecimal.ZERO;
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));

            IllegalArgumentException IllegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> contaBancaria.saque(valorInvalido));
            assertEquals("Valor não pode ser zero", IllegalArgumentException.getMessage());
        }

        @Test
        public void deveLancarExceptionAoSacarQuandoValorNegativo() {
            BigDecimal valorInvalido = new BigDecimal("-500.00");
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000"));

            IllegalArgumentException IllegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> contaBancaria.saque(valorInvalido));
            assertEquals("Valor não pode ser negativo", IllegalArgumentException.getMessage());
        }

        @Test
        public void deveLancarExceptionAoSacarQuandoSaldoInsuficiente() {
            BigDecimal saldoInicial = new BigDecimal("1000.00");
            BigDecimal valorSacarMaior = new BigDecimal("1500.48");
            ContaBancaria contaBancaria = new ContaBancaria(saldoInicial);

            RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> contaBancaria.saque(valorSacarMaior));
            assertEquals("Saldo insuficiente", runtimeException.getMessage());
        }
    }
    /*========== Testes saque - FIM ==========*/

    /*========== Testes depósito - INÍCIO ==========*/
    @Nested
    class Deposito {
        @Test
        public void deveDepositarQuandoValorValido() {
            BigDecimal saldoInicial = new BigDecimal("1000.00");
            BigDecimal valorDepositar = new BigDecimal("350.55");
            BigDecimal saldoEsperado = saldoInicial.add(valorDepositar);
            ContaBancaria contaBancaria = new ContaBancaria(saldoInicial);

            assertDoesNotThrow(() -> contaBancaria.deposito(valorDepositar));
            assertEquals(saldoEsperado, contaBancaria.saldo(), "Saldo da conta após depósito é diferente do esperado");
        }

        @Test
        public void deveLancarExceptionAoDepositarQuandoValorNulo() {
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000.00"));

            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> contaBancaria.deposito(null));
            assertEquals("Valor não pode ser nulo", illegalArgumentException.getMessage());
        }

        @Test
        public void deveLancarExceptionAoDepositarQuandoValorZero() {
            BigDecimal valorDepositar = BigDecimal.ZERO;
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000.00"));

            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> contaBancaria.deposito(valorDepositar));
            assertEquals("Valor não pode ser zero", illegalArgumentException.getMessage());
        }

        @Test
        public void deveLancarExceptionAoDepositarQuandoValorNegativo() {
            BigDecimal valorDepositar = new BigDecimal("-100.00");
            ContaBancaria contaBancaria = new ContaBancaria(new BigDecimal("1000.00"));

            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> contaBancaria.deposito(valorDepositar));
            assertEquals("Valor não pode ser negativo", illegalArgumentException.getMessage());
        }
    }
    /*========== Testes depósito - FIM ==========*/
}