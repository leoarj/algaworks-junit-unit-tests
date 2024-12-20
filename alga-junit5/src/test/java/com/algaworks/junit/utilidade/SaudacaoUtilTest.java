package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // exemplo para nome amigável na hora de executar os testes
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
    //@DisplayName("Deve saudar com bom dia")
    public void Dado_um_horario_matutino_Quando_saudar_Entao_deve_retornar_bom_dia() {
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
    //@DisplayName("Deve saudar com bom dia às 5 horas")
    public void Dado_o_horario_matutino_5h_Quando_saudar_Entao_deve_retornar_bom_dia() {
        int horaValida = 5;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Bom dia", saudacao, "Saudação incorreta!");
    }

    @Test
    void Dado_um_horario_vespertino_Quando_saudar_Entao_deve_retornar_boa_tarde() {
        int horaValida = 17;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Boa tarde", saudacao, "Saudação incorreta!");
    }

    @Test
    void Dado_um_horario_noturno_Quando_saudar_Entao_deve_retornar_boa_noite() {
        int horaValida = 21;
        String saudacao = SaudacaoUtil.saudar(horaValida);
        assertEquals("Boa noite", saudacao, "Saudação incorreta!");
    }

    @Test
    void Dado_o_horario_noturno_4h_Quando_saudar_Entao_deve_retornar_boa_noite() {
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
    public void Dado_uma_hora_invalida_Quando_saudar_Entao_deve_lancar_exception() {
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
    public void Dado_uma_hora_valida_Quando_saudar_Entao_nao_deve_lancar_exception() {
        // Arrange
        int horaValida = 0;
        // Act
        Executable chamadaValidaDeMetodo = () -> SaudacaoUtil.saudar(horaValida);
        // Assert
        assertDoesNotThrow(chamadaValidaDeMetodo);
    }

    /**
     * Exemplo de teste paramêtrizado.
     * Para cada valor no array da anotação @ValueSource, um teste será executado.
     * */
    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8, 9, 10, 11})
    public void Dado_horario_matinal_Entao_deve_retornar_bom_dia(int hora) {
        String saudacao = SaudacaoUtil.saudar(hora);
        assertEquals("Bom dia", saudacao, "Saudação incorreta!");
    }

}