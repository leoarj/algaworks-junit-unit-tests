package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.modelo.Editor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Classe stub (implementação falsa/dublê).
 *
 * O objetivo de stubs é simular uma implementação de um componente externo,
 * de modo que o teste unitário não tenha que se preocupar com a dependência,
 * caso contrário seria um teste de integração.
 *
 * No stub, podemos simular dados e comportamentos que seriam fornecidos externamente.
 *
 * */
public class ArmazenamentoFixoEmMemoria implements ArmazenamentoEditor {

    // flag para controle nos testes
    public boolean chamouSalvar;

    @Override
    public Editor salvar(Editor editor) {
        this.chamouSalvar = true;
        if (editor.getId() == null) {
            editor.setId(1L);
        }
        return editor;
    }

    @Override
    public Optional<Editor> encontrarPorId(Long editor) {
        return Optional.empty();
    }

    @Override
    public Optional<Editor> encontrarPorEmail(String email) {
        if (email.equals("leandro.existe@email.com")) {
            return Optional.of(new Editor(2L, "Leandro", "leandro.existe@email.com", BigDecimal.TEN, true));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Editor> encontrarPorEmailComIdDiferenteDe(String email, Long id) {
        return Optional.empty();
    }

    @Override
    public void remover(Long editorId) {

    }

    @Override
    public List<Editor> encontrarTodos() {
        return null;
    }
}
