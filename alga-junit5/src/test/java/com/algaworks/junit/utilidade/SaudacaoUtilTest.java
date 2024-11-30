package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    public void deveSaudarComBomDiaQuandoHoraValida() {
        String saudacao = SaudacaoUtil.saudar(9);
        // Testa e espera que o valor da variável saudacao seja igual a "Bom dia"
        assertEquals("Bom dia", saudacao, "Saudação incorreta!");
    }

    @Test
    void deveSaudarComBoaTardeQuandoHoraValida() {
        String saudacao = SaudacaoUtil.saudar(17);
        assertEquals("Boa tarde", saudacao, "Saudação incorreta!");
    }

    @Test
    void deveSaudarComBoaNoiteQuandoHoraValida() {
        String saudacao = SaudacaoUtil.saudar(21);
        assertEquals("Boa noite", saudacao, "Saudação incorreta!");
    }

    /**
     * Testa uma condição onde uma exceção deve ser lançada,
     * e a mensagem da mesma deve corresponder a uma esperada.
     *
     * org.opentest4j.AssertionFailedError: Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.
     */
    @Test
    public void deveLancarException() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> SaudacaoUtil.saudar(-10));
        assertEquals("Hora inválida", illegalArgumentException.getMessage());
    }

    /**
     * Testa um cenário onde não deve ser lançada uma exceção.
     *
     * org.opentest4j.AssertionFailedError: Unexpected exception thrown: java.lang.IllegalArgumentException: Hora inválida
     * */
    @Test
    public void naoDeveLancarException() {
        assertDoesNotThrow(() -> SaudacaoUtil.saudar(0));
    }

}