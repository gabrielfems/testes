package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {

    private final PoliticaDesconto politica = new PoliticaDesconto();

    private static Cliente cliente(boolean vip, int compras) {
        return new Cliente(vip, false, compras);
    }

    @Test
    void subtotalNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> politica.calcular(cliente(false, 2), -50, null));
    }

    @Test
    void descontoNaoPassaDoLimite() {
        assertEquals(2_400,
                politica.calcular(cliente(false, 0), 24_000, "EXTRA10"));

        assertEquals(4_800,
                politica.calcular(cliente(true, 2), 24_000, "EXTRA10"));
    }

    @Test
    void cupomDesconhecido() {
        assertThrows(IllegalArgumentException.class,
                () -> politica.calcular(cliente(false, 2), 24_000, "INVALIDO"));
    }

    @Test
    void extra10NaoAplica() {
        assertEquals(0,
                politica.calcular(cliente(false, 2), 19_999, "EXTRA10"));
    }

    @Test
    void extra10() {
        assertEquals(2_400,
                politica.calcular(cliente(false, 2), 24_000, "EXTRA10"));
    }

    @Test
    void bemVindoNaoAplica() {
        assertEquals(3_000,
                politica.calcular(cliente(false, 2), 60_000, "BEMVINDO"));

        assertEquals(0,
                politica.calcular(cliente(false, 0), 9_999, "BEMVINDO"));
    }

    @Test
    void bemVindoComEspacos() {
        assertEquals(2_000,
                politica.calcular(cliente(false, 0), 20_000, " bemvindo "));
    }

    @Test
    void bemVindo() {
        assertEquals(2_400,
                politica.calcular(cliente(false, 0), 12_000, "BEMVINDO"));
    }

    @Test
    void cupomVazio() {
        assertEquals(3_000,
                politica.calcular(cliente(false, 2), 60_000, "  "));
    }

    @Test
    void clienteComum() {
        assertEquals(3_000,
                politica.calcular(cliente(false, 2), 60_000, null));

        assertEquals(0,
                politica.calcular(cliente(false, 2), 49_999, null));
    }

    @Test
    void clienteVip() {
        assertEquals(1_200,
                politica.calcular(cliente(true, 2), 12_000, null));
    }

    @Test
    void semDesconto() {
        assertEquals(0,
                politica.calcular(cliente(false, 2), 12_000, null));
    }
}