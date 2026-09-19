package br.edu.ifpr.boletim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticipacaoTest {

    @Test
    void deveCalcularTresPontos() {
        Participacao participacao = new Participacao();
        int resultado = participacao.calcularPontos(true, true);
        assertEquals(3, resultado);
    }

    @Test
    void deveCalcularZerosPontos(){
        Participacao participacao = new Participacao();
        int resultado = participacao.calcularPontos(false, false);
        assertEquals(0, resultado);
    }
}
