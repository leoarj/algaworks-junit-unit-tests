package com.algaworks.junit.blog.utilidade;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ConversorSlugTest {

    /**
     * Exemplo de teste com mock estático.
     * Dependência mockito-inline é necessároa.
     * É um tipo de teste mais custo de processamento,
     * e deve ser usado quando necessário.
     * */
    @Test
    void deveConverterJuntoComCodigo() {
        try (MockedStatic<GeradorCodigo> mockedStatic = Mockito.mockStatic(GeradorCodigo.class)) {
            mockedStatic.when(GeradorCodigo::gerar).thenReturn("123456");

            String slug = ConversorSlug.converterJuntoComCodigo("olá mundo java");

            assertEquals("ola-mundo-java-123456", slug);
        }
    }

}