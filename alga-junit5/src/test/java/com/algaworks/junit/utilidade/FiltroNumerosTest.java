package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

// Anotação para personalizar geraçãop de nome de exibição dos testes.
// No caso vai substituir underscores definidos no nome
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FiltroNumerosTest {

    /**
     * Testa cenário com listas, onde uma sequência informada,
     * depois de filtrada deve ser igual a um resultado esperado,
     * lembrando que Assertions.assertIterableEquals também verifica as posições dos elementos.
     * */
    @Test
    // Renomeado para testar @DisplayNameGeneration

    /*
    * Nomenclatura padrão BDD
    * Given, When, Then
    * Dado, Quando, Então
    * */

    public void Dado_uma_lista_de_numeros_Quando_filtrar_por_pares_Entao_deve_retornar_apenas_numeros_pares() {
        List<Integer>  numeros = Arrays.asList(1, 2, 3, 4);
        List<Integer> numerosParesEsperados = Arrays.asList(2, 4);
        List<Integer> resultadoFiltro = FiltroNumeros.numerosPares(numeros);
        Assertions.assertIterableEquals(numerosParesEsperados, resultadoFiltro);
    }

    @Test
    public void Dado_uma_lista_de_numeros_Quando_filtrar_por_impares_Entao_deve_retornar_apenas_numeros_impares() {
        List<Integer>  numeros = Arrays.asList(1, 2, 3, 4);
        List<Integer> numerosParesEsperados = Arrays.asList(1, 3);
        List<Integer> resultadoFiltro = FiltroNumeros.numerosImpares(numeros);
        Assertions.assertIterableEquals(numerosParesEsperados, resultadoFiltro);
    }

}