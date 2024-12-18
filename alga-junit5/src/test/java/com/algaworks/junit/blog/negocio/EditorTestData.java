package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.modelo.Editor;

import java.math.BigDecimal;

/**
 * Aplica o design patter "Object Mother", para centralizar código referente à criação de dependências nos testes.
 * */
public class EditorTestData {

    private EditorTestData() {

    }

    public static Editor umEditorNovo() {
        return new Editor(null, "Leandro", "leandro@email.com", BigDecimal.TEN, true);
    }

    public static Editor umEditorExistente() {
        return new Editor(1L, "Leandro", "leandro@email.com", BigDecimal.TEN, true);
    }

    public static Editor umEditorComIdInexistente() {
        return new Editor(99L, "Leandro", "leandro@email.com", BigDecimal.TEN, true);
    }


}
