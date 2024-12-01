package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.modelo.Editor;
import com.algaworks.junit.blog.modelo.Ganhos;
import com.algaworks.junit.blog.modelo.Post;
import com.algaworks.junit.blog.utilidade.ProcessadorTextoSimples;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CalculadoraGanhosTest {

    static CalculadoraGanhos calculadora;
    Editor autor;
    Post post;

    /**
     * Anotação @BeforeAll define um método de classe que será executado antes de todos os testes.
     * Ideal para instanciar recursos estáticos e/ou que não mudam o estado, compartilhados entre diferentes testes,
     * centralizando código comum referente às dependências dos testes.
     * */
    @BeforeAll
    static void beforeAll() {
        calculadora = new CalculadoraGanhos(new ProcessadorTextoSimples(), BigDecimal.TEN);
    }

    /**
     * Anotação @BeforeEach define um método de instância que será executado a cada teste.
     * Ideal para instânciar novos objetos para cada teste, desse modo um cenário não afeta outro,
     * centralizando código referente às dependências dos testes.
     * */
    @BeforeEach
    void beforeEach() {
        autor = new Editor(1L, "Leandro", "leandro@mail.com", new BigDecimal(5), true);

        post = new Post(1L, "Ecossistema Java", "O ecossistema do Java é muito maduro", autor,
                "ecossistema-java-abc123", null, false, false);
    }

    @Test
    public void Dado_um_post_e_autor_premium_Quando_calcular_ganhos_Entao_deve_retornar_valor_com_bonus() {
        Ganhos ganhos = calculadora.calcular(post);
        assertEquals(new BigDecimal("45"), ganhos.getTotalGanho());
    }

    @Test
    public void Dado_um_post_e_autor_Quando_calcular_ganhos_Entao_deve_retornar_quantidade_de_palavras_no_post() {
        Ganhos ganhos = calculadora.calcular(post);
        assertEquals(7, ganhos.getQuantidadePalavras());
    }

    @Test
    public void Dado_um_post_e_autor_Quando_calcular_ganhos_Entao_deve_retornar_valor_pago_por_palavra_do_autor() {
        Ganhos ganhos = calculadora.calcular(post);
        assertEquals(autor.getValorPagoPorPalavra(), ganhos.getValorPagoPorPalavra());
    }

    @Test
    public void Dado_um_post_e_autor_nao_premium_Quando_calcular_ganhos_Entao_deve_retornar_valor_sem_bonus() {
        autor.setPremium(false); // porque é instância no beforeEach com premium = true
        Ganhos ganhos = calculadora.calcular(post);
        assertEquals(new BigDecimal("35"), ganhos.getTotalGanho());
    }

}