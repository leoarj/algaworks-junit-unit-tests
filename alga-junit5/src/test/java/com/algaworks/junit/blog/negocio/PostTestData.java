package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.modelo.Ganhos;
import com.algaworks.junit.blog.modelo.Post;

import java.math.BigDecimal;

public class PostTestData {

    public static Post.Builder umPostNovo() {
        return Post.builder()
                .titulo("Testes unitários com Mockito")
                .conteudo("Testes unitários com Mockito")
                .autor(EditorTestData.umEditorExistente().build())
                .pago(false)
                .publicado(false);
    }

    public static Post.Builder umPostExistente() {
        return umPostNovo()
                .id(1L)
                .ganhos(new Ganhos(BigDecimal.TEN, 4, new BigDecimal("40")))
                .pago(true)
                .publicado(true);
    }

    public static Post.Builder umPostComIdInexistente() {
        return umPostNovo().id(99L);
    }

}
