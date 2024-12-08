package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

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

    /*
    * Anotação @Spy torna o objeto observável para verificar chamadas de método.
    * Obs.: Executado a cada teste
    *
    * Editor sendo inicializado aqui, para não substituir o skyp do Mockito,
    * caso estivesse inicializando pelo @BeforeEach/init().
    * */
    @Spy
    Editor editor = new Editor(null, "Leandro", "leandro@email.com", BigDecimal.TEN, true);

    /*
    * Anotação @Captor, pode ser usada para inicializar o ArgumentCaptor, para a class definida.
    * Obs.: Executado a cada teste
    * */
    @Captor
    ArgumentCaptor<Mensagem> mensagemArgumentCaptor;

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

    @Nested
    class CadastroComEditorValido {

        @BeforeEach
        void init() {
            // Define comportamentos dos mocks
//        Mockito.when(armazenamentoEditor.salvar(editor))
//                .thenReturn(new Editor(1L, "Leandro", "leandro@email.com", BigDecimal.TEN, true));

            // Com thenAnswer() é possível personalizar o parâmetro passado na invocação.
            // Com Mockito.any() é possível deixar o parâmetro dinâmico para o tipo definido, em vez de passar um objeto fixo
            // Obs.: Essa definição nunca vai ser usada caso um editor for nulo, por isso está em uma inner-class.
            Mockito.when(armazenamentoEditor.salvar(Mockito.any(Editor.class)))
                    .thenAnswer(invocation -> {
                        Editor editorPassado = invocation.getArgument(0, Editor.class);
                        editorPassado.setId(1L);
                        return editorPassado;
                    });

        }

        @Test
        void Dado_um_editor_valido_Quando_criar_Entao_deve_retornar_um_id_de_cadastro() {
            Editor editorSalvo = cadastroEditor.criar(editor);
            assertEquals(1L, editorSalvo.getId());
        }

        /**
         * Teste com Mockito.verify(), verifica se chamada do método foi chamada n vezes,
         * e se o argumento do método passado foi o mesmo repassado para o método da classe que está tendo
         * o comportamento verificado.
         * */
        @Test
        void Dado_um_editor_valido_Quando_criar_Entao_deve_chamar_metodo_salvar_do_armazenamento() {
            cadastroEditor.criar(editor);
            Mockito.verify(armazenamentoEditor, Mockito.times(1))
                    .salvar(Mockito.eq(editor));
        }

        /**
         * Testa o lançamento de exceptions, customizando um mock para que caso determinado método seja chamado,
         * uma Runtime exception seja lançada.
         *
         * Em seguida, verifica as asserções para que:
         * - A exceção realmente seja lançada.
         * - Verifica se o método de um mock deve ser chamado nesta determinada situação.
         *
         * thenThrow() = Lança uma exceção caso determinado comportamento seja chamado.
         * never() = Método do mock nunca deve ser chamado, conforme o fluxo definido.
         * any() = Qualquer argumento passado.
         *
         * */
        @Test
        void Dado_um_editor_valido_Quando_criar_e_lancar_exception_ao_salvar_Entao_nao_deve_enviar_email() {
            Mockito.when(armazenamentoEditor.salvar(editor)).thenThrow(new RuntimeException());
            assertAll("Não deve enviar e-mail, quando lançar exception do armazenamento",
                    () -> assertThrows(RuntimeException.class, () -> cadastroEditor.criar(editor)),
                    () -> Mockito.verify(gerenciadorEnvioEmail, Mockito.never()).enviarEmail(Mockito.any())
            );
        }

        /**
         * Teste para ArgumentCaptor, para capturar argumentos em uma chamada de método de um mock.
         * No caso, capturando o argumento que foi passado e recuperando o valor para realizar asserções necessárias.
         *
         * Para esta situação, o e-mail do destinatário da mensagem deve ser o mesmo que do editor.
         * */
        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_enviar_email_com_destino_ao_editor() {
            // Criando ArgumentCaptor
//        ArgumentCaptor<Mensagem> mensagemArgumentCaptor = ArgumentCaptor.forClass(Mensagem.class);

            // Internamento vai chamar o envio de e-mail
            Editor editorSalvo = cadastroEditor.criar(editor);

            // Configura a captura, para quando o método do mock for chamado
            Mockito.verify(gerenciadorEnvioEmail).enviarEmail(mensagemArgumentCaptor.capture());

            // Recupera o valor argumento passado no método do mock
            Mensagem mensagem = mensagemArgumentCaptor.getValue();

            assertEquals(editorSalvo.getEmail(), mensagem.getDestinatario());
        }

        /**
         * Teste com spy().
         * spy() = Permite observar objetos e verificações de chamadas de seus métodos.
         * O objeto "espionado" deve ser passado como argumento nos métodos que se deseja testar.
         * */
        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_verificar_o_email() {
            // Criando objeto spy
//        Editor editorSpy = Mockito.spy(editor);
            cadastroEditor.criar(editor);
            Mockito.verify(editor, Mockito.atLeast(1)).getEmail();
        }

        /**
         * Exemplo de teste com alteração do retorno conforme chamadas consecutivas.
         * */
        @Test
        void Dado_um_editor_com_email_existente_Quando_cadastrar_Entao_deve_lancar_exception() {
            /*
             * Configura para quando chamar na primeira vez, retorne um Optional vazio,
             * na segunda vez um objeto (já cadastrado).
             *
             * Dessa forma, na segunda chamada do método que depende da verificação do email,
             * é esperado que retorne uma exception.
             * */
            Mockito.when(armazenamentoEditor.encontrarPorEmail("leandro@email.com"))
                    .thenReturn(Optional.empty()) // definição para primeira chamada
                    .thenReturn(Optional.of(editor)); // definição para segunda chamada

            Editor editorComEmailExistente = new Editor(null, "Leandro", "leandro@email.com", BigDecimal.TEN, true);

            cadastroEditor.criar(editor); // execução de primeira chamada

            // execução de segunda chamada
            assertThrows(RegraNegocioException.class, () -> cadastroEditor.criar(editorComEmailExistente));
        }

        /**
         * Exemplo de verificação da ordem de execução de métodos para objetos mock.
         * Útil para assegurar que uma sequência correta de operações seja chamada na regra de negócio.
         * */
        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_enviar_email_apos_salvar() {
            cadastroEditor.criar(editor);

            // armazenamentoEditor e gerenciadorEnvioEmail já são mocks
            // aqui está verificando a sequência de execção deles
            InOrder inOrder = Mockito.inOrder(armazenamentoEditor, gerenciadorEnvioEmail);
            inOrder.verify(armazenamentoEditor, Mockito.times(1)).salvar(editor);
            inOrder.verify(gerenciadorEnvioEmail, Mockito.times(1)).enviarEmail(Mockito.any(Mensagem.class));
        }

    }

    @Nested
    class CadastroComEditorNull {

        /**
         * Como no teste de editor nulo, as definições de invocação não serão usadas,
         * o Mockito lança uma exception de mocks não utilizados.
         * Para resolver isso, pode-se criar inner-classes para separar os testes que dependem
         * das definições dos que não dependem.
         * */
        @Test
        void Dado_um_editor_null_Quando_cadastrar_Entao_deve_lancar_exception() {
            assertThrows(NullPointerException.class, () -> cadastroEditor.criar(editor));
            Mockito.verify(armazenamentoEditor, Mockito.never()).salvar(Mockito.any());
            Mockito.verify(gerenciadorEnvioEmail, Mockito.never()).enviarEmail(Mockito.any());
        }

    }

}
