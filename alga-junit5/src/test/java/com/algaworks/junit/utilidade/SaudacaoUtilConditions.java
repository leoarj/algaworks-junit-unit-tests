package com.algaworks.junit.utilidade;

import org.assertj.core.api.Condition;

public class SaudacaoUtilConditions {

    private SaudacaoUtilConditions () {

    }

    public static Condition<String> igualBomDia() {
        return igual("Bom dia");
    }

    /**
     * Encapsula lógica de teste, usando a interface {@link Condition},
     * com o objetivo de reusar código de teste e melhorar a legibilidade dos testes.
     * */
    public static Condition<String> igual(String saudacaoCorreta) {
        return new Condition<>(s -> s.equals(saudacaoCorreta), "equals to %s", saudacaoCorreta);
    }

}
