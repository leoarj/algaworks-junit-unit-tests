package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaudacaoUtilTest {

    /**
     * Exemplo de teste unitário.
     *
     * Anotação @Test marca o método como um teste.
     *
     * Com o pacote de {@link org.junit.jupiter.api.Assertions}
     * podemos verificar os resultados e fazer o self-validating do teste.
     */
    @Test
    public void saudar() {
        String saudacao = SaudacaoUtil.saudar(9);
        // Testa e espera que o valor da variável saudacao seja igual a "Bom dia"
        assertEquals("Bom dia", saudacao, "Saudação incorreta!");
    }

}