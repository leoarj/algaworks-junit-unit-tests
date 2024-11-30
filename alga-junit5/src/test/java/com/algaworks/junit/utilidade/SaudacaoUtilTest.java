package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes no utilitário de saudação") // exemplo para nome amigável na hora de executar os testes
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
    @DisplayName("Deve saudar com bom dia")
    public void deveSaudarComBomDiaQuandoHoraValida() {
        /*
        * Padrão Tripe A (Arrange, Act e Assert)
        * - Preparação das variáveis de teste
        * - Execução/definição das lógicas
        * - Verificação dos resultados dos testes
        * */

        // Arrange
        int horaValida = 9;

        // Act
        String saudacao = SaudacaoUtil.saudar(horaValida);

        // Assert
        assertEquals("Bom dia", saudacao, "Saudação incorreta!");
    }

    @Test
    @DisplayName("Deve saudar com bom dia às 5 horas")
    public void deveSaudarComBomDiaQuandoHoraValidaAPartir5h() {
        int horaValida = 5;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Bom dia", saudacao, "Saudação incorreta!");
    }

    @Test
    void deveSaudarComBoaTardeQuandoHoraValida() {
        int horaValida = 17;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Boa tarde", saudacao, "Saudação incorreta!");
    }

    @Test
    void deveSaudarComBoaNoiteQuandoHoraValida() {
        int horaValida = 21;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Boa noite", saudacao, "Saudação incorreta!");
    }

    @Test
    void deveSaudarComBoaNoiteQuandoHoraValidaAs4h() {
        int horaValida = 4;
        String saudacao = SaudacaoUtil.saudar(horaValida);
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
        // Arrange
        int horaValida = -10;
        // Act
        Executable chamadaInvalidaDeMetodo = () -> SaudacaoUtil.saudar(horaValida);
        //Assert
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, chamadaInvalidaDeMetodo);
        assertEquals("Hora inválida", e.getMessage());
    }

    /**
     * Testa um cenário onde não deve ser lançada uma exceção.
     *
     * org.opentest4j.AssertionFailedError: Unexpected exception thrown: java.lang.IllegalArgumentException: Hora inválida
     * */
    @Test
    public void naoDeveLancarException() {
        // Arrange
        int horaValida = 0;
        // Act
        Executable chamadaValidaDeMetodo = () -> SaudacaoUtil.saudar(horaValida);
        // Assert
        assertDoesNotThrow(chamadaValidaDeMetodo);
    }

}