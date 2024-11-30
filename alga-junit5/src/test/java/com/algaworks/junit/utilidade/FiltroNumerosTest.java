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
    public void deve_retornar_numeros_pares_() {
        List<Integer>  numeros = Arrays.asList(1, 2, 3, 4);
        List<Integer> numerosParesEsperados = Arrays.asList(2, 4);
        List<Integer> resultadoFiltro = FiltroNumeros.numerosPares(numeros);
        Assertions.assertIterableEquals(numerosParesEsperados, resultadoFiltro);
    }

}