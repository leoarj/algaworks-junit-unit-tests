package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Exemplo de uso de mocks.
 *
 * O que é Mock
 *  Implementação falsa/fictícia, isolamento de dependências
 *
 *  Possui vantagens a mais que o Stub
 *  - Não é necessário implementar uma interface ou estender uma classe (pode ser criada em tempo de compilação)
 *  - Facilidade de definir um comportamento fictício dinâmico em uma classe
 *  - Verificar comportamento da classe com mock
 * Mockito + JUnit
 *
 * */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CadastroEditorComMockTest {

    CadastroEditor cadastroEditor;
    Editor editor;

    @BeforeEach
    void init() {
        editor = new Editor(null, "Leandro", "leandro@email.com", BigDecimal.TEN, true);

        // Cria dinamicamente implementações fictícias e define comportamentos
        ArmazenamentoEditor armazenamentoEditor = Mockito.mock(ArmazenamentoEditor.class);
        Mockito.when(armazenamentoEditor.salvar(editor))
                .thenReturn(new Editor(1L, "Leandro", "leandro@email.com", BigDecimal.TEN, true));

        GerenciadorEnvioEmail gerenciadorEnvioEmail = Mockito.mock(GerenciadorEnvioEmail.class);

        cadastroEditor = new CadastroEditor(armazenamentoEditor, gerenciadorEnvioEmail);
    }

    @Test
    public void Dado_um_editor_valido_Quando_criar_Entao_deve_retornar_um_id_de_cadastro() {
        Editor editorSalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorSalvo.getId());
    }

}
