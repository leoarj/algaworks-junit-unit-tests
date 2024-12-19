package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.modelo.Editor;

import java.math.BigDecimal;

/**
 * Aplica o design patter "Object Mother", para centralizar código referente à criação de dependências nos testes.
 * */
public class EditorTestData {

    private EditorTestData() {

    }

    public static Editor.Builder umEditorNovo() {
        return Editor.builder()
                .nome("Leandro")
                .email("leandro@email.com")
                .valorPagoPorPalavra(BigDecimal.TEN)
                .premium(true);

    }

    public static Editor.Builder umEditorExistente() {
        return umEditorNovo().id(1L);
    }

    public static Editor.Builder umEditorComIdInexistente() {
        return umEditorNovo().id(99L);
    }


}
