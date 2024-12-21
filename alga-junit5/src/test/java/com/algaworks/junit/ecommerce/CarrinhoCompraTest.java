package com.algaworks.junit.ecommerce;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Carrinho de compras")
class CarrinhoCompraTest {

    private Cliente cliente;
    private Produto produtoCelular;
    private Produto produtoFoneOuvido;
    private Produto produtoTablet;
    private List<ItemCarrinhoCompra> listaInicialItensCarrinho;
    private CarrinhoCompra carrinho;

    /**
     * Decidi fazer:
     * - Cenário específico de carrinho vazio
     * - Cenário específico só para carrinho inicializado com itens
     * - Cenário geral
     * */

    @Nested
    @DisplayName("Dado um carrinho de compras")
    class DadoUmCarrinhoDeCompra {

        //DONE: Inicializando dados comuns toda vez a cada teste
        @BeforeEach
        void beforeEach() {
            cliente = new Cliente(1L, "Leandro");

            produtoCelular = new Produto(1L, "Celular", "Celular", new BigDecimal("3000.00"));
            produtoFoneOuvido = new Produto(2L, "Fone de ouvido", "Fone de ouvido", new BigDecimal("100.00"));
            produtoTablet = new Produto(1L, "Tablet", "Tablet", new BigDecimal("1900.00"));

            carrinho = null; // para forçar inicializar o carrinho a cada grupo de testes?
        }

        /*
         * Cenário específico de carrinho inicializado vazio.
         * */
        @Nested
        @DisplayName("Vazio")
        class Vazio {

            @BeforeEach
            void beforeEach() {
                carrinho = new CarrinhoCompra(cliente);
            }

            @Nested
            @DisplayName("Quando retornar a lista de itens")
            class QuandoRetornarAListaDeItens {

                @Test
                @DisplayName("Então deve estar vazia")
                void deveEstarVazia() {
                    assertTrue(carrinho.getItens().isEmpty());
                }

            }

            @Nested
            @DisplayName("Quando calcular valor total")
            class QuandoCalcularValorTotal {

                @Test
                @DisplayName("Então deve ser zero")
                void valorTotalDeveSerZero() {
                    assertEquals(new BigDecimal("0.00"), carrinho.getValorTotal());
                }
            }

            @Nested
            @DisplayName("Quando calcular quantidade total")
            class QuandoCalcularQuantidadeTotal {

                @Test
                @DisplayName("Então deve ser zero")
                void quantidadeTotalDeveSerZero() {
                    assertEquals(0, carrinho.getQuantidadeTotalDeProdutos());
                }
            }

            /**
             * Teste utilizando AssertJ para asserções múltiplas em listas.
             * */
            @Test
            @DisplayName("Deve conter apenas itens adicionados")
            void deveConterApenasItensAdicionados() {
                carrinho.adicionarProduto(produtoCelular, 1);
                carrinho.adicionarProduto(produtoFoneOuvido, 1);

                Assertions.assertThat(carrinho.getItens())
                        .flatMap(ItemCarrinhoCompra::getProduto)
                        .contains(produtoCelular, produtoFoneOuvido)
                        .doesNotContain(produtoTablet);
            }
        }

        /*
         * Cenário específico de carrinho inicializado com itens.
         * */
        @Nested
        @DisplayName("Dado um carrinho de compras com itens")
        class ComItens {

            @BeforeEach
            void beforeEach() {
                listaInicialItensCarrinho = List.of(
                        new ItemCarrinhoCompra(produtoCelular, 1),
                        new ItemCarrinhoCompra(produtoFoneOuvido, 2),
                        new ItemCarrinhoCompra(produtoTablet, 1));

                carrinho = new CarrinhoCompra(cliente, listaInicialItensCarrinho);
            }

            @Nested
            @DisplayName("Quando retornar a lista de itens")
            class QuandoRetornarAListaDeItens {

                @Test
                @DisplayName("Então deve ter os mesmos produtos e quantidades que a lista inicial")
                void deveTerOsMesmosProdutosEQuantidadesQueAListaInicial() {
                    assertIterableEquals(listaInicialItensCarrinho, carrinho.getItens());
                }
            }

            @Nested
            @DisplayName("Quando esvaziar carrinho")
            class QuandoEsvaziarCarrinho {

                @BeforeEach
                void beforeEach() {
                    carrinho.esvaziar();
                }

                @Test
                @DisplayName("Então deve ficar vazia")
                void deveFicarVazia() {
                    assertTrue(carrinho.getItens().isEmpty());
                }

                @Test
                @DisplayName("E deve zerar valor total de produtos no carrinho")
                void deveZerarValorTotalNoCarrinho() {
                    assertEquals(new BigDecimal("0.00"), carrinho.getValorTotal());
                }

                @Test
                @DisplayName("E deve zerar quantidade total de produtos no carrinho")
                void deveZerarQuantidadeTotalNoCarrinho() {
                    assertEquals(0, carrinho.getQuantidadeTotalDeProdutos());
                }

            }
        }

        /*
        * Cenário geral.
        * */
        @Nested
        @DisplayName("Quando acessar a lista de itens")
        class QuandoAcessarAListaDeItens {

            @BeforeEach
            void beforeEach() {
                carrinho = new CarrinhoCompra(cliente);
                carrinho.adicionarProduto(produtoCelular, 1);
                carrinho.adicionarProduto(produtoFoneOuvido, 1);
            }

            @Test
            @DisplayName("Então não deve ser nula")
            void naoDeveSerNula() {
                assertNotNull(carrinho.getItens());
            }

            //DONE: Aqui, decidi mudar um pouco a ideia, quanto a "deve sempre retornar uma nova lista"
            // para "não deve alterar diretamente a lista de itens do carrinho"
            @Test
            @DisplayName("E não deve alterar indevidamente a lista de itens")
            void naoDeveAlterarIndevidamenteAListaDeItens() {
                int tamanhoInicialDaListaDeItens = carrinho.getItens().size();
                int quantidadeParaAdicionar = tamanhoInicialDaListaDeItens + 2;

                carrinho.getItens().clear();
                carrinho.getItens().add(new ItemCarrinhoCompra(produtoTablet, quantidadeParaAdicionar));

                // Nesse caso, poderia ser com assertAll? Já que gostaria de verificar as duas condições, de qualquer forma.
                // Obs.: Antes estava usando Collections.unmodifiableList
                assertAll(
                        () ->  assertTrue(carrinho.produtoNaoExisteNoCarrinho(produtoTablet),
                                "Produto não pode ser adicionado diretamente na lista original de itens do carrinho"),
                        () -> assertEquals(tamanhoInicialDaListaDeItens, carrinho.getItens().size(),
                                "Lista de itens original do carrinho não pode ser modificada diretamente")
                );
            }

            //DONE: Aqui que mantive a idea de "deve retornar uma nova lista"
            @Test
            @DisplayName("E sempre deve retornar uma nova instância da lista de itens")
            void deveSempreRetornarNovaInstanciaDaLista() {
                List<ItemCarrinhoCompra> primeiraListaRetornada = carrinho.getItens();
                List<ItemCarrinhoCompra> segundaListaRetornada = carrinho.getItens();

                assertNotSame(primeiraListaRetornada, segundaListaRetornada);
            }

        }

        @Nested
        @DisplayName("Quando adicionar produto")
        class QuandoAdicionarProduto {

            Produto produtoParaAdicionar;
            int quantidadeParaAdicionar;

            @BeforeEach
            void beforeEach() {
                carrinho = new CarrinhoCompra(cliente);
                carrinho.adicionarProduto(produtoCelular, 1);
                carrinho.adicionarProduto(produtoFoneOuvido, 1);

                produtoParaAdicionar = produtoTablet;
                quantidadeParaAdicionar = 1;
            }

            @Test
            @DisplayName("Então deve ter sido adicionado no carrinho")
            void deveTerSidoAdicionadoNoCarrinho() {
                carrinho.adicionarProduto(produtoParaAdicionar, quantidadeParaAdicionar);

                Optional<ItemCarrinhoCompra> itemCarrinhoAdicionado = carrinho.buscarItemPeloProduto(produtoParaAdicionar);

                assertTrue(itemCarrinhoAdicionado.isPresent());
                assertEquals(quantidadeParaAdicionar, itemCarrinhoAdicionado.get().getQuantidade());
            }

            @Test
            @DisplayName("E deve aumentar valor total de produtos no carrinho")
            void deveAumentarValorTotalNoCarrinho() {
                BigDecimal valorTotalInicialCarrinho = carrinho.getValorTotal();
                BigDecimal valorTotalProduto = produtoParaAdicionar.getValor().multiply(new BigDecimal(quantidadeParaAdicionar));
                BigDecimal valorTotalEsperadoCarrinho = valorTotalInicialCarrinho.add(valorTotalProduto);

                carrinho.adicionarProduto(produtoParaAdicionar, quantidadeParaAdicionar);

                assertEquals(valorTotalEsperadoCarrinho, carrinho.getValorTotal());
            }

            @Test
            @DisplayName("E deve aumentar quantidade total de produtos no carrinho")
            void deveAumentarQuantidadeTotalNoCarrinho() {
                int quantidadeTotalEsperada = carrinho.getQuantidadeTotalDeProdutos() + quantidadeParaAdicionar;

                carrinho.adicionarProduto(produtoParaAdicionar, quantidadeParaAdicionar);

                assertEquals(quantidadeTotalEsperada, carrinho.getQuantidadeTotalDeProdutos());
            }

            @Nested
            @DisplayName("Com quantidade inválida")
            class ComQuantidadeInvalida {

                int quantidadeInvalidaParaAdicionar;

                @BeforeEach
                void beforeEach() {
                    quantidadeInvalidaParaAdicionar = -2;
                }

                @Test
                @DisplayName("Então deve lançar exception")
                void deveLancarException() {
                    assertThrows(IllegalArgumentException.class,
                            () -> carrinho.adicionarProduto(produtoParaAdicionar, quantidadeInvalidaParaAdicionar));
                }

                @Test
                @DisplayName("E não deve ter sido adicionado no carrinho")
                void naoDeveTerSidoAdicionadoNoCarrinho() {
                    assertAll(
                            () -> assertThrows(IllegalArgumentException.class,
                                    () -> carrinho.adicionarProduto(produtoParaAdicionar, quantidadeInvalidaParaAdicionar)),
                            () -> assertTrue(carrinho.produtoNaoExisteNoCarrinho(produtoParaAdicionar))
                    );
                }

                @Test
                @DisplayName("E não deve aumentar valor total de produtos no carrinho")
                void naoDeveAumentarValorTotalNoCarrinho() {
                    BigDecimal valorTotalInicialCarrinho = carrinho.getValorTotal();

                    assertAll(
                            () -> assertThrows(IllegalArgumentException.class,
                                    () -> carrinho.adicionarProduto(produtoParaAdicionar, quantidadeInvalidaParaAdicionar)),
                            () -> assertEquals(valorTotalInicialCarrinho, carrinho.getValorTotal())
                    );
                }

                @Test
                @DisplayName("E não deve aumentar quantidade total de produtos no carrinho")
                void naoDeveAumentarQuantidadeTotalNoCarrinho() {
                    int quantidadeTotalEsperada = carrinho.getQuantidadeTotalDeProdutos();

                    assertAll(
                            () -> assertThrows(IllegalArgumentException.class,
                                    () -> carrinho.adicionarProduto(produtoParaAdicionar, quantidadeInvalidaParaAdicionar)),
                            () -> assertEquals(quantidadeTotalEsperada, carrinho.getQuantidadeTotalDeProdutos())
                    );
                }
            }

            @Nested
            @DisplayName("Inválido")
            class Invalido {

                @Test
                @DisplayName("Então deve lançar exception")
                void deveLancarException() {
                    assertThrows(IllegalArgumentException.class, () -> carrinho.adicionarProduto(null, quantidadeParaAdicionar));
                }
            }
        }

        @Nested
        @DisplayName("Quando remover um produto")
        class QuandoRemoverProduto {

            Produto produtoParaRemover;
            Produto produtoParaRemoverInexistente;
            int quantidadeAdicionada = 5;

            @BeforeEach
            void beforeEach() {
                produtoParaRemover = produtoFoneOuvido;
                produtoParaRemoverInexistente = produtoTablet;
                quantidadeAdicionada = 5;

                carrinho = new CarrinhoCompra(cliente);
                carrinho.adicionarProduto(produtoFoneOuvido, quantidadeAdicionada);
            }

            @Test
            @DisplayName("Então deve ter sido removido do carrinho")
            void deveTerSidoRemovidoDoCarrinho() {
                carrinho.removerProduto(produtoParaRemover);

                assertTrue(carrinho.produtoNaoExisteNoCarrinho(produtoParaRemover));
            }

            @Test
            @DisplayName("E deve diminuir valor total de produtos no carrinho")
            void deveDiminuirValorTotalNoCarrinho() {
                BigDecimal valorTotalInicialCarrinho = carrinho.getValorTotal();
                BigDecimal valorTotalProduto = produtoParaRemover.getValor().multiply(new BigDecimal(quantidadeAdicionada));
                BigDecimal valorTotalEsperado = valorTotalInicialCarrinho.subtract(valorTotalProduto);

                carrinho.removerProduto(produtoParaRemover);

                assertEquals(valorTotalEsperado, carrinho.getValorTotal());
            }

            @Test
            @DisplayName("E deve diminuir quantidade total de produtos no carrinho")
            void deveDiminuirQuantidadeTotalNoCarrinho() {
                int quantidadeTotalEsperada = carrinho.getQuantidadeTotalDeProdutos() - quantidadeAdicionada;

                carrinho.removerProduto(produtoParaRemover);

                assertEquals(quantidadeTotalEsperada, carrinho.getQuantidadeTotalDeProdutos());
            }

            @Nested
            @DisplayName("Inexistente")
            class Inexistente {

                @Test
                @DisplayName("Então deve lançar exception")
                void deveLancarException() {
                    assertThrows(RuntimeException.class, () -> carrinho.removerProduto(produtoParaRemoverInexistente));
                }
            }

            @Nested
            @DisplayName("Inválido")
            class Invalido {

                @Test
                @DisplayName("Então deve lançar exception")
                void deveLancarException() {
                    assertThrows(IllegalArgumentException.class, () -> carrinho.removerProduto(null));
                }
            }

        }

        @Nested
        @DisplayName("Quando aumentar quantidade de um produto")
        class QuandoAumentarQuantidadeProduto {

            Produto produtoParaAumentarQuantidade;
            Produto produtoInexistenteParaAumentarQuantidade;
            int quantidadeParaAumentar;

            @BeforeEach
            void beforeEach() {
                produtoParaAumentarQuantidade = produtoTablet;
                produtoInexistenteParaAumentarQuantidade = produtoCelular;
                quantidadeParaAumentar = 1;

                carrinho = new CarrinhoCompra(cliente);
                carrinho.adicionarProduto(produtoParaAumentarQuantidade, 5);
            }

            @Test
            @DisplayName("Então deve aumentar quantidade no item")
            void deveAumentarQuantidadeItem() {
                ItemCarrinhoCompra item = carrinho.buscarItemPeloProdutoOuFalhar(produtoParaAumentarQuantidade);
                int quantidadeEsperadaItem = item.getQuantidade() + quantidadeParaAumentar;

                carrinho.aumentarQuantidadeProduto(produtoParaAumentarQuantidade);

                ItemCarrinhoCompra itemAtualizado = carrinho.buscarItemPeloProdutoOuFalhar(produtoParaAumentarQuantidade);
                int quantidadeFinalItem = itemAtualizado.getQuantidade();

                assertEquals(quantidadeEsperadaItem, quantidadeFinalItem);
            }

            @Test
            @DisplayName("E deve aumentar valor total de produtos no carrinho")
            void deveAumentarValorTotalNoCarrinho() {
                BigDecimal valorTotalInicialCarrinho = carrinho.getValorTotal();
                BigDecimal valorTotalProduto = produtoParaAumentarQuantidade.getValor().multiply(new BigDecimal(quantidadeParaAumentar));
                BigDecimal valorTotalEsperado = valorTotalInicialCarrinho.add(valorTotalProduto);

                carrinho.adicionarProduto(produtoParaAumentarQuantidade, quantidadeParaAumentar);

                assertEquals(valorTotalEsperado, carrinho.getValorTotal());
            }

            @Test
            @DisplayName("E deve aumentar quantidade total de produtos no carrinho")
            void deveAumentarQuantidadeTotalNoCarrinho() {
                int quantidadeTotalEsperada = carrinho.getQuantidadeTotalDeProdutos() + quantidadeParaAumentar;

                carrinho.adicionarProduto(produtoParaAumentarQuantidade, quantidadeParaAumentar);

                assertEquals(quantidadeTotalEsperada, carrinho.getQuantidadeTotalDeProdutos());
            }

            @Nested
            @DisplayName("Inexistente")
            class Inexistente {

                @Test
                @DisplayName("Então deve lançar exception")
                void deveLancarException() {
                    assertThrows(RuntimeException.class,
                            () -> carrinho.aumentarQuantidadeProduto(produtoInexistenteParaAumentarQuantidade));
                }
            }

            @Nested
            @DisplayName("Inválido")
            class Invalido {

                @Test
                @DisplayName("Então deve lançar exception")
                void deveLancarException() {
                    assertThrows(IllegalArgumentException.class, () -> carrinho.aumentarQuantidadeProduto(null));
                }
            }
        }

        @Nested
        @DisplayName("Quando diminuir quantidade de um produto")
        class QuandoDiminuirQuantidadeProduto {

            Produto produtoParaDiminuirQuantidade;
            Produto produtoInexistenteParaDiminuirQuantidade;
            int quantidadeParaDiminuir;

            @BeforeEach
            void beforeEach() {
                produtoParaDiminuirQuantidade = produtoTablet;
                produtoInexistenteParaDiminuirQuantidade = produtoFoneOuvido;
                quantidadeParaDiminuir = 1;

                carrinho = new CarrinhoCompra(cliente);
                carrinho.adicionarProduto(produtoParaDiminuirQuantidade, 5);
            }

            @Test
            @DisplayName("Então deve diminuir quantidade no item")
            void deveDiminuirQuantidadeItem() {
                ItemCarrinhoCompra item = carrinho.buscarItemPeloProdutoOuFalhar(produtoParaDiminuirQuantidade);
                int quantidadeInicialItem = item.getQuantidade();
                int quantidadeEsperadaItem = quantidadeInicialItem - quantidadeParaDiminuir;

                carrinho.diminuirQuantidadeProduto(produtoParaDiminuirQuantidade);

                ItemCarrinhoCompra itemAtualizado = carrinho.buscarItemPeloProdutoOuFalhar(produtoParaDiminuirQuantidade);
                int quantidadeFinalItem = itemAtualizado.getQuantidade();

                assertEquals(quantidadeEsperadaItem, quantidadeFinalItem);
            }

            @Test
            @DisplayName("E deve diminuir valor total de produtos no carrinho")
            void deveDiminuirValorTotalNoCarrinho() {
                BigDecimal valorTotalInicialCarrinho = carrinho.getValorTotal();
                BigDecimal valorTotalProduto = produtoParaDiminuirQuantidade.getValor()
                        .multiply(new BigDecimal(quantidadeParaDiminuir));
                BigDecimal valorTotalEsperado = valorTotalInicialCarrinho.subtract(valorTotalProduto);

                carrinho.diminuirQuantidadeProduto(produtoParaDiminuirQuantidade);

                assertEquals(valorTotalEsperado, carrinho.getValorTotal());
            }

            @Test
            @DisplayName("E deve diminuir quantidade total de produtos no carrinho")
            void deveDiminuirQuantidadeTotalNoCarrinho() {
                int quantidadeTotalEsperada = carrinho.getQuantidadeTotalDeProdutos() - quantidadeParaDiminuir;

                carrinho.diminuirQuantidadeProduto(produtoParaDiminuirQuantidade);

                assertEquals(quantidadeTotalEsperada, carrinho.getQuantidadeTotalDeProdutos());
            }

            @Nested
            @DisplayName("Quando quantidade do produto for igual a Um")
            class QuandoQuantidadeDoProdutoForIgualUm {

                @BeforeEach
                void beforeEach() {
                    carrinho = new CarrinhoCompra(cliente);
                    carrinho.adicionarProduto(produtoParaDiminuirQuantidade, 1);
                }

                @Test
                @DisplayName("Então deve também remover produto do carrinho")
                void deveRemoverProdutoDoCarrinho() {
                    carrinho.diminuirQuantidadeProduto(produtoParaDiminuirQuantidade);

                    assertTrue(carrinho.produtoNaoExisteNoCarrinho(produtoParaDiminuirQuantidade));
                }

            }

            @Nested
            @DisplayName("Inexistente")
            class Inexistente {

                @Test
                @DisplayName("Então deve lançar exception")
                void deveLancarException() {
                    assertThrows(RuntimeException.class,
                            () -> carrinho.diminuirQuantidadeProduto(produtoInexistenteParaDiminuirQuantidade));
                }
            }

            @Nested
            @DisplayName("Inválido")
            class Invalido {

                @Test
                @DisplayName("Então deve lançar exception")
                void deveLancarException() {
                    assertThrows(IllegalArgumentException.class, () -> carrinho.diminuirQuantidadeProduto(null));
                }
            }
        }

    }

}