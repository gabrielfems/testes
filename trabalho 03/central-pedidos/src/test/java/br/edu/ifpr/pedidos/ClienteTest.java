package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void aceitaHistoricoZero() {
        var c = new Cliente(true, false, 0);
        assertEquals(0, c.comprasAnteriores());
    }

    @Test
    void aceitaDadosValidos() {
        var c = new Cliente(false, true, 7);
        assertFalse(c.vip());
        assertTrue(c.bloqueado());
        assertEquals(7, c.comprasAnteriores());
    }

    @Test
    void rejeitaHistoricoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente(true, false, -5));
    }
}