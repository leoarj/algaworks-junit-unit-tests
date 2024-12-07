package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {

    /**
     * Testa cenário de asserções agrupadas, que é onde caso uma exceção
     * da cadeia de testes falhar, mesmo assim a execução continuará até o final.
     *
     * É útil para testes onde a rastreabilidade de uma sequência de passos é importante.
     * */
    @Test
    void assercaoAgrupada() {
        Pessoa pessoa = new Pessoa("Leandro", "Araújo");

        // Testa todas as asserções sem quebrar o fluxo, mostrando as falhas (caso houver) apenas no final.
        assertAll("Asserções de pessoa",
                () -> assertEquals("Leandro", pessoa.getNome()),
                () -> assertEquals("Araújo", pessoa.getSobrenome()));
    }

}