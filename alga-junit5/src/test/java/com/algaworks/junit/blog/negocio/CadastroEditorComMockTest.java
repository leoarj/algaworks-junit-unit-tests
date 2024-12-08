package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
/*
* Anotação @ExtendWith e classe MockitoExtension adicionam capacidades na classe de testes
* para inicialização de mocks e injeção dos mesmos.
* */
@ExtendWith(MockitoExtension.class)
public class CadastroEditorComMockTest {

    Editor editor;

    /*
    * Anotação @Mock permite que o Mockito crie um mock do tipo anotado.
    * Obs.: Executado a cada teste
    * */
    @Mock
    ArmazenamentoEditor armazenamentoEditor;

    @Mock
    GerenciadorEnvioEmail gerenciadorEnvioEmail;

    /*
    * Anotação @InjetcMocks, injeta os mocks no objeto que precisa das dependências.
    * Obs.: Executado a cada teste.
    *
    * Dessa forma podemos deixar o @BeforeEach mais para definição de comportamentos.
    * */
    @InjectMocks
    CadastroEditor cadastroEditor;

    @BeforeEach
    void init() {
        editor = new Editor(null, "Leandro", "leandro@email.com", BigDecimal.TEN, true);

        // Define comportamentos dos mocks
//        Mockito.when(armazenamentoEditor.salvar(editor))
//                .thenReturn(new Editor(1L, "Leandro", "leandro@email.com", BigDecimal.TEN, true));

        // Com thenAnswer() é possível personalizar o parâmetro passado na invocação.
        // Com Mockito.any() é possível deixar o parâmetro dinâmico para o tipo definido, em vez de passar um objeto fixo
        Mockito.when(armazenamentoEditor.salvar(Mockito.any(Editor.class)))
                .thenAnswer(invocation -> {
                   Editor editorPassado = invocation.getArgument(0, Editor.class);
                   editorPassado.setId(1L);
                   return editorPassado;
                });

    }

    @Test
    public void Dado_um_editor_valido_Quando_criar_Entao_deve_retornar_um_id_de_cadastro() {
        Editor editorSalvo = cadastroEditor.criar(editor);
        assertEquals(1L, editorSalvo.getId());
    }

}
