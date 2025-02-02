package com.algaworks.junit.utilidade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class SimuladorEsperaTest {

    /**
     * Realiza teste de timeout, esperando o tempo total definido na execução,
     * comparando ao final com o limite esperado, mostrando a diferença caso o teste falhar.
     *
     * org.opentest4j.AssertionFailedError: execution exceeded timeout of 1000 ms by 9001 ms
     * */
    @Test
//    @Disabled("Não é mais aplicável") // exemplo de desativação de um teste
    @EnabledIfEnvironmentVariable(named = "ENV", matches = "ENV") // exemplo de ativação condicional de teste
    void deveEsperarENaoDarTimeout() {

        /*
        * Ativação condicional de teste.
        * Aborta o teste caso a suposição seja verdadeira, e executa um Supplier para uma mensagem especificada.
        * */
//        Assumptions.assumeTrue("PROD".equals(System.getenv("ENV")), () -> "Abortando teste: Não deve ser executado em PROD");

        Assertions.assertTimeout(Duration.ofSeconds(2),
                () -> SimuladorEspera.esperar(Duration.ofSeconds(1)));
    }

    /**
     * Realiza teste de timeout, aguardando o limite esperado,
     * comparando ao final com o tempo total que a execução real levaria,
     * mostrando a diferença caso o teste falhar.
     * Em outras palavras, falha se a execução real não alcançar o limite definido a tempo
     * e para a execução do teste antes, em vez de esperar até o final.
     *
     * org.opentest4j.AssertionFailedError: execution timed out after 1000 ms
     * */
    @Test
    void deveEsperarENaoDarTimeoutPreemptively() {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1),
                () -> SimuladorEspera.esperar(Duration.ofMillis(10)));
    }

}