package com.algaworks.junit.utilidade;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testes com parâmetros de enums.
 * */
class MultiplicadorTest {

    /**
     * Deve se passar a class do enum, e se não for testar todos, passar os nomes.
     * */
    @ParameterizedTest
    @EnumSource(value = Multiplicador.class, names = {"DOBRO", "TRIPLO"})
    void aplicarMultiplicador(Multiplicador multiplicador) {
        assertNotNull(multiplicador.aplicarMultiplicador(10.0));
    }

    /**
     * Se não passar os nomes dos enuns, todos serão testados.
     * */
    @ParameterizedTest
    @EnumSource(value = Multiplicador.class)
    void aplicarMultiplicadorTodos(Multiplicador multiplicador) {
        assertNotNull(multiplicador.aplicarMultiplicador(10.0));
    }
}