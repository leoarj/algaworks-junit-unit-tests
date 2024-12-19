package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoPost;
import com.algaworks.junit.blog.exception.PostNaoEncontradoException;
import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Ganhos;
import com.algaworks.junit.blog.modelo.Notificacao;
import com.algaworks.junit.blog.modelo.Post;
import com.algaworks.junit.blog.utilidade.ConversorSlug;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CadastroPostTest {

    @Captor
    ArgumentCaptor<Notificacao> notificacaoArgumentCaptor;

    @Mock
    ArmazenamentoPost armazenamentoPost;

    @Mock
    CalculadoraGanhos calculadoraGanhos;

    @Mock
    GerenciadorNotificacao gerenciadorNotificacao;

    @InjectMocks
    CadastroPost cadastroPost;

    //DONE: Ações principais
    //DONE: Retornos principais
    //DONE: Sequências principais

    @Nested
    @DisplayName("Dado um post válido")
    class DadoUmPostValido {

        @Nested
        @DisplayName("Quando criar")
        class QuandoCriar {

            @Spy
            Post post = PostTestData.umPostNovo().build();

            static final long idValido = 1L;

            @BeforeEach
            void init() {
                Mockito.when(armazenamentoPost.salvar(Mockito.any(Post.class)))
                        .thenAnswer(invocationOnMock -> {
                            Post postPassado = invocationOnMock.getArgument(0, Post.class);
                            postPassado.setId(idValido);
                            return postPassado;
                        });
            }

            @Test
            @DisplayName("Então deve chamar método salvar do armazenamento")
            void entaoDeveChamarMetodoSalvarDoArmazenamento() {
                cadastroPost.criar(post);

                Mockito.verify(armazenamentoPost, Mockito.times(1)).salvar(post);
            }

            @Test
            @DisplayName("E deve retornar um ID de cadastro válido")
            void eDeveRetornarUmIdDeCadastroValido() {
                Post postSalvo = cadastroPost.criar(post);

                Mockito.verify(post, Mockito.times(1)).setId(Mockito.eq(idValido));
                assertEquals(idValido, postSalvo.getId());
            }

            @Test
            @DisplayName("E deve retornar post com slug")
            void eDeveRetornarPostComSlug () {
                Post postCriado = cadastroPost.criar(post);

                Mockito.verify(post, Mockito.times(1)).setSlug(Mockito.anyString());
                assertNotNull(postCriado.getSlug());
            }

            @Test
            @DisplayName("E deve retornar post com ganhos")
            void eDeveRetornarPostComGanhos() {
                /*
                * Objeto deve estar no contexto do mock, para que possa ser capturado na execução
                * e possa ser verificado corretamente.
                * Por isso o comportamento da CalculadoraDeGanhos foi modificado para retornar um objeto de Ganhos conhecido.
                * Caso isso não for feito, um erro como o abaixo é esperado:
                *
                * Argument(s) are different! Wanted:
                *    post.setGanhos(
                *        <any com.algaworks.junit.blog.modelo.Ganhos>
                *    );
                *
                * */

                Ganhos ganhos = new Ganhos(BigDecimal.TEN, 4, new BigDecimal("40"));
                Mockito.when(calculadoraGanhos.calcular(post)).thenReturn(ganhos);

                Post postCriado = cadastroPost.criar(post);

                Mockito.verify(post, Mockito.times(1)).setGanhos(ganhos);
                assertNotNull(postCriado.getGanhos());
            }

            @Test
            @DisplayName("E deve criar slug a partir do título do post")
            void eDeveCriarSlugAPartirDoTituloDoPost() {
                try (MockedStatic<ConversorSlug> conversorSlugMockedStatic = Mockito.mockStatic(ConversorSlug.class)) {
                    String slug = "testes-unitarios-com-mockito";
                    Mockito.when(ConversorSlug.converterJuntoComCodigo(post.getTitulo())).thenReturn(slug);

                    cadastroPost.criar(post);

                    assertAll(
                            () -> Mockito.verify(post, Mockito.times(1)).setSlug(slug),
                            () -> conversorSlugMockedStatic.verify(
                                    () -> ConversorSlug.converterJuntoComCodigo(post.getTitulo()), Mockito.times(1)),
                            () -> assertEquals(slug, post.getSlug())
                    );
                }
            }

            @Test
            @DisplayName("E deve criar slug antes de salvar")
            void eDeveCriarSlugAntesDeSalvar() {
                try (MockedStatic<ConversorSlug> conversorSlugMockedStatic = Mockito.mockStatic(ConversorSlug.class)) {
                    String slug = "testes-unitarios-com-mockito";
                    Mockito.when(ConversorSlug.converterJuntoComCodigo(post.getTitulo())).thenReturn(slug);

                    cadastroPost.criar(post);

                    InOrder inOrder = Mockito.inOrder(post, ConversorSlug.class, armazenamentoPost, gerenciadorNotificacao);
                    inOrder.verify(conversorSlugMockedStatic,
                            () -> ConversorSlug.converterJuntoComCodigo(post.getTitulo()), Mockito.times(1));
                    inOrder.verify(post, Mockito.times(1)).setSlug(slug);
                    inOrder.verify(armazenamentoPost, Mockito.times(1)).salvar(post);
                    inOrder.verify(gerenciadorNotificacao).enviar(Mockito.any(Notificacao.class));
                }
            }

            @Test
            @DisplayName("E deve calcular ganhos antes de salvar")
            void eDeveCalcularGanhosAntesDeSalvar() {
                Ganhos ganhos = new Ganhos(BigDecimal.TEN, 4, new BigDecimal("40"));
                Mockito.when(calculadoraGanhos.calcular(post)).thenReturn(ganhos);

                cadastroPost.criar(post);

                InOrder inOrder = Mockito.inOrder(post, calculadoraGanhos, armazenamentoPost);
                inOrder.verify(calculadoraGanhos, Mockito.times(1)).calcular(post);
                inOrder.verify(post, Mockito.times(1)).setGanhos(ganhos);
                Mockito.verify(armazenamentoPost, Mockito.times(1)).salvar(post);
            }

            @Test
            @DisplayName("E deve enviar notificação com título do post criado")
            void eDeveEnviarNotificacaoComTituloDoPostCriado() {
                Post postSalvo = cadastroPost.criar(post);
                String conteudoNotificacaoEsperado = "Novo post criado -> " + postSalvo.getTitulo();

                Mockito.verify(gerenciadorNotificacao).enviar(notificacaoArgumentCaptor.capture());

                Notificacao notificacao = notificacaoArgumentCaptor.getValue();

                assertEquals(conteudoNotificacaoEsperado, notificacao.getConteudo());
            }

            @Test
            @DisplayName("E deve enviar notificação após salvar")
            void eDeveEnviarNotificacaoAposSalvar() {
                cadastroPost.criar(post);

                InOrder inOrder = Mockito.inOrder(armazenamentoPost, gerenciadorNotificacao);
                inOrder.verify(armazenamentoPost, Mockito.times(1)).salvar(post);
                inOrder.verify(gerenciadorNotificacao, Mockito.times(1))
                        .enviar(Mockito.any(Notificacao.class));
            }

            @Nested
            @DisplayName("Quando lançar exception do armazenamento")
            class QuandoLancarExceptionDoArmazenamento {

                @Test
                @DisplayName("Então não deve enviar notificação")
                void entaoNaoDeveEnviarNotificacao() {
                    Mockito.when(armazenamentoPost.salvar(post)).thenThrow(new RuntimeException());

                    assertAll("Não deve enviar notificação quando lançar exception do armazenamento",
                            () -> assertThrows(RuntimeException.class, () -> cadastroPost.criar(post)),
                            () -> Mockito.verify(gerenciadorNotificacao, Mockito.never()).enviar(Mockito.any(Notificacao.class))
                    );
                }

            }

        }

        @Nested
        @DisplayName("Quando editar")
        class QuandoEditar {

            static final long idValido = 1L;

            @Spy
            Post post = PostTestData.umPostExistente().build();

            @BeforeEach
            void init() {
                Mockito.when(armazenamentoPost.salvar(Mockito.any(Post.class)))
                        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Post.class));

                Mockito.when(armazenamentoPost.encontrarPorId(idValido))
                        .thenReturn(Optional.ofNullable(post));
            }

            @Test
            @DisplayName("Então deve alterar post salvo")
            void entaoDeveAlterarPostSalvo() {
                post.setTitulo("Testes unitários com Mockito segunda edição");
                post.setConteudo("Testes unitários com Mockito segunda edição");

                Post postAtualizado = cadastroPost.editar(post);

                assertEquals(post.getTitulo(), postAtualizado.getTitulo());
                assertEquals(post.getConteudo(), postAtualizado.getConteudo());

                InOrder inOrder = Mockito.inOrder(post, armazenamentoPost);
                inOrder.verify(post, Mockito.times(1)).atualizarComDados(post);
                inOrder.verify(armazenamentoPost, Mockito.times(1)).salvar(post);
            }

            @Test
            @DisplayName("E deve retornar com mesmo ID")
            void eDeveRetornarComMesmoId() {
                Post postAtualizado = cadastroPost.editar(post);

                Mockito.verify(post, Mockito.never()).setId(Mockito.anyLong());
                assertEquals(idValido, postAtualizado.getId());
            }

            @Test
            @DisplayName("E se alterar título não deve alterar slug")
            void eSeAlterarTituloNaoDeveAlterarSlug() {
                post.setTitulo("Testes unitários com Mockito segunda edição");
                Post postAtualizado = cadastroPost.editar(post);

                Mockito.verify(post, Mockito.never()).setSlug(Mockito.anyString());
                assertEquals(post.getSlug(), postAtualizado.getSlug());
            }

            @Nested
            @DisplayName("Estando pago")
            class EstandoPago {

                @Test
                @DisplayName("Então não deve recalcular ganhos")
                void entaoNaoDeveRecalcularGanhos() {
                    post.marcarComoPago();
                    Ganhos ganhosOriginais = new Ganhos(post.getGanhos().getValorPagoPorPalavra(),
                            post.getGanhos().getQuantidadePalavras(),
                            post.getGanhos().getTotalGanho());

                    Post postAtualizado = cadastroPost.editar(post);

                    assertAll(
                            () -> Mockito.verify(post, Mockito.times(1)).isPago(),
                            () -> Mockito.verify(post, Mockito.never()).setGanhos(Mockito.any(Ganhos.class)),
                            () -> Mockito.verify(calculadoraGanhos, Mockito.never()).calcular(Mockito.any(Post.class)),
                            () -> assertEquals(ganhosOriginais, postAtualizado.getGanhos())
                    );
                }

            }

            @Nested
            @DisplayName("Estando não pago")
            class EstandoNaoPago {

                @Test
                @DisplayName("Então deve recalcular ganhos")
                void entaoDeveRecalcularGanhos() {
                    post.setPago(false);
                    post.setConteudo("Testes unitários com Mockito segunda edição"); // 6 palavras
                    Ganhos novosGanhosEsperados = new Ganhos(BigDecimal.TEN, 6, new BigDecimal("60"));

                    Mockito.when(calculadoraGanhos.calcular(Mockito.any(Post.class))).thenReturn(novosGanhosEsperados);

                    Post postAtualizado = cadastroPost.editar(post);

                    assertAll(
                            () -> Mockito.verify(post, Mockito.times(1)).isPago(),
                            () -> Mockito.verify(post, Mockito.times(1)).setGanhos(novosGanhosEsperados),
                            () -> Mockito.verify(calculadoraGanhos, Mockito.times(1)).calcular(Mockito.any(Post.class)),
                            () -> assertEquals(novosGanhosEsperados, postAtualizado.getGanhos())
                    );
                }

                @Test
                @DisplayName("E deve recalcular ganhos antes de salvar")
                void eDeveRecalcularGanhosAntesDeSalvar() {
                    post.setPago(false);
                    post.setConteudo("Testes unitários com Mockito segunda edição"); // 6 palavras
                    Ganhos novosGanhosEsperados = new Ganhos(BigDecimal.TEN, 6, new BigDecimal("60"));

                    Mockito.when(calculadoraGanhos.calcular(Mockito.any(Post.class))).thenReturn(novosGanhosEsperados);

                    cadastroPost.editar(post);

                    InOrder inOrder = Mockito.inOrder(post, calculadoraGanhos, armazenamentoPost);
                    inOrder.verify(post, Mockito.times(1)).isPago();
                    inOrder.verify(calculadoraGanhos, Mockito.times(1)).calcular(Mockito.any(Post.class));
                    inOrder.verify(post, Mockito.times(1)).setGanhos(novosGanhosEsperados);
                    inOrder.verify(armazenamentoPost, Mockito.times(1)).salvar(Mockito.any(Post.class));
                }

            }

        }

        @Nested
        @DisplayName("Quando remover")
        class QuandoRemover {

            final long idValido = 1L;

            @Spy
            Post post = PostTestData.umPostExistente()
                    .pago(false)
                    .publicado(false)
                    .build();

            @BeforeEach
            void init() {
                Mockito.when(armazenamentoPost.encontrarPorId(idValido))
                        .thenReturn(Optional.ofNullable(post));
            }

            @Test
            @DisplayName("Então deve chamar método remover do armazenamento")
            void entaoDeveChamarMetodoRemoverDoArmazenamento() {
                cadastroPost.remover(idValido);

                Mockito.verify(armazenamentoPost, Mockito.times(1)).remover(Mockito.eq(idValido));
            }

            @Test
            @DisplayName("E deve remover do armazenamento")
            void eDeveRemoverDoArmazenamento() {
                //DONE: Muda comportamento para retornar vazio ao procurar, após executar remoção?
                Mockito.doAnswer(invocationOnMock -> {
                    Mockito.when(armazenamentoPost.encontrarPorId(Mockito.eq(idValido)))
                            .thenReturn(Optional.empty());
                    return null;
                }).when(armazenamentoPost).remover(idValido);

                cadastroPost.remover(idValido);

                assertEquals(Optional.empty(), armazenamentoPost.encontrarPorId(idValido));
            }

            @Nested
            @DisplayName("Estando publicado")
            class EstandoPublicado {

                @Test
                @DisplayName("Então deve lançar exception")
                void entaoDeveLancarException() {
                    post.publicar();
                    assertThrows(RegraNegocioException.class, () -> cadastroPost.remover(idValido));
                }

                @Test
                @DisplayName("E não deve chamar método remover do armazenamento")
                void eNaoDeveChamarMetodoRemoverDoArmazenamento() {
                    post.publicar();
                    assertThrows(RegraNegocioException.class, () -> cadastroPost.remover(idValido));
                    Mockito.verify(armazenamentoPost, Mockito.never()).remover(idValido);
                }

            }

            @Nested
            @DisplayName("Estando pago")
            class EstandoPago {

                @Test
                @DisplayName("Então deve lançar exception")
                void entaoDeveLancarException() {
                    post.marcarComoPago();
                    assertThrows(RegraNegocioException.class, () -> cadastroPost.remover(idValido));
                }

                @Test
                @DisplayName("E não deve chamar método remover do armazenamento")
                void eNaoDeveChamarMetodoRemoverDoArmazenamento() {
                    post.marcarComoPago();
                    assertThrows(RegraNegocioException.class, () -> cadastroPost.remover(idValido));
                    Mockito.verify(armazenamentoPost, Mockito.never()).remover(idValido);
                }

            }

        }
    }

    @Nested
    @DisplayName("Dado um post null")
    class DadoUmPostNull {

        @Nested
        @DisplayName("Quando criar")
        class QuandoCriar {

            @Test
            @DisplayName("Então deve lançar exception")
            void entaoDeveLancarException() {
                assertThrows(NullPointerException.class, () -> cadastroPost.criar(null));
            }

            @Test
            @DisplayName("E não deve chamar método salvar do armazenamento")
            void eNaoDeveChamarMetodoSalvarDoArmazenamento() {
                assertThrows(NullPointerException.class, () -> cadastroPost.criar(null));
                Mockito.verify(armazenamentoPost, Mockito.never()).salvar(Mockito.any(Post.class));
            }

        }

        @Nested
        @DisplayName("Quando editar")
        class QuandoEditar {

            @Test
            @DisplayName("Então deve lançar exception")
            void entaoDeveLancarException() {
                assertThrows(NullPointerException.class, () -> cadastroPost.editar(null));
            }

            @Test
            @DisplayName("E não deve chamar método salvar do armazenamento")
            void eNaoDeveChamarMetodoSalvarDoArmazenamento() {
                assertThrows(NullPointerException.class, () -> cadastroPost.editar(null));
                Mockito.verify(armazenamentoPost, Mockito.never()).salvar(Mockito.any(Post.class));
            }

        }

    }

    @Nested
    @DisplayName("Dado um ID invalido")
    class DadoUmIdInvalido {

        static final long idInvalido = 99L;

        @Spy
        Post post = PostTestData.umPostComIdInexistente().build();

        @Nested
        @DisplayName("Quando editar")
        class QuandoEditar {

            @Test
            @DisplayName("Então deve lançar exception")
            void entaoDeveLancarException() {
                assertThrows(PostNaoEncontradoException.class, () -> cadastroPost.editar(post));
            }

            @Test
            @DisplayName("E não deve chamar método salvar do armazenamento")
            void eNaoDeveChamarMetodoSalvarDoArmazenamento() {
                assertThrows(PostNaoEncontradoException.class, () -> cadastroPost.editar(post));
                Mockito.verify(armazenamentoPost, Mockito.never()).salvar(post);
            }

        }

        @Nested
        @DisplayName("Quando remover")
        class QuandoRemover {

            @Test
            @DisplayName("Então deve lançar exception")
            void entaoDeveLancarException() {
                assertThrows(PostNaoEncontradoException.class, () -> cadastroPost.remover(idInvalido));
            }

            @Test
            @DisplayName("E não deve chamar método remover do armazenamento")
            void eNaoDeveChamarMetodoRemoverDoArmazenamento() {
                assertThrows(PostNaoEncontradoException.class, () -> cadastroPost.remover(idInvalido));
                Mockito.verify(armazenamentoPost, Mockito.never()).remover(idInvalido);
            }

        }

    }

}