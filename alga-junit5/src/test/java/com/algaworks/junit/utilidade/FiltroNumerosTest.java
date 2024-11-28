package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FiltroNumerosTest {

    /**
     * Testa cenário com listas, onde uma sequência informada,
     * depois de filtrada deve ser igual a um resultado esperado,
     * lembrando que Assertions.assertIterableEquals também verifica as posições dos elementos.
     * */
    @Test
    public void deveRetornarNumerosPares() {
        List<Integer>  numeros = Arrays.asList(1, 2, 3, 4);
        List<Integer> numerosParesEsperados = Arrays.asList(2, 4);
        List<Integer> resultadoFiltro = FiltroNumeros.numerosPares(numeros);
        Assertions.assertIterableEquals(numerosParesEsperados, resultadoFiltro);
    }

}