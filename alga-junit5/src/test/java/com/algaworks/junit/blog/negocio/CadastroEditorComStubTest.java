package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CadastroEditorComStubTest {

    CadastroEditor cadastroEditor;
    ArmazenamentoFixoEmMemoria armazenamentoEditor;
    Editor editor;

    @BeforeEach
    void beforeEach() {
        editor = new Editor(null, "Leandro", "leandro@email.com", BigDecimal.TEN, true);

        armazenamentoEditor = new ArmazenamentoFixoEmMemoria();

        cadastroEditor = new CadastroEditor(
                armazenamentoEditor,
                new GerenciadorEnvioEmail() {
                    @Override
                    void enviarEmail(Mensagem mensagem) {
                        System.out.printf("Enviando mensagem para: %s", mensagem.getDestinatario());
                    }
                }
        );
    }

    @Test
    public void Dado_um_editor_valido_Quando_criar_Entao_deve_retornar_um_id_de_cadastro() {
        Editor editorSalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorSalvo.getId());
        assertTrue(armazenamentoEditor.chamouSalvar);
    }

    @Test
    public void Dado_um_editor_null_Quando_criar_Entao_deve_lancar_exception() {
        assertThrows(NullPointerException.class, () -> cadastroEditor.criar(null));
        assertFalse(armazenamentoEditor.chamouSalvar);
    }

    // Obs.: No stub já tem um editor com esse e-mail
    @Test
    public void Dado_um_editor_com_email_existente_Quando_criar_Entao_deve_lancar_exception() {
        editor.setEmail("leandro.existe@email.com");
        assertThrows(RegraNegocioException.class, () -> cadastroEditor.criar(editor));
    }

    @Test
    public void Dado_um_editor_com_email_existente_Quando_criar_Entao_nao_deve_salvar() {
        editor.setEmail("leandro.existe@email.com");
        try {
            cadastroEditor.criar(editor);
        } catch (RegraNegocioException e) {
            assertFalse(armazenamentoEditor.chamouSalvar);
        }
    }

}