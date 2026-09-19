package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {

    @Test
    void processadorNulo() {
        assertThrows(NullPointerException.class,
                () -> new PagamentoService(null));
    }

    @Test
    void outraExcecao() {
        var p = new PagamentoService(total -> {
            throw new IllegalArgumentException();
        });
        assertThrows(IllegalArgumentException.class,
                () -> p.pagar(200, 2));
    }

    @Test
    void acabaAsTentativas() {
        AtomicInteger chamadas = new AtomicInteger();

        var p = new PagamentoService(total -> {
            chamadas.incrementAndGet();
            throw new IllegalStateException();
        });
        assertFalse(p.pagar(200, 4));
        assertEquals(4, chamadas.get());
    }

    @Test
    void tentaDeNovo() {
        AtomicInteger chamadas = new AtomicInteger();

        var p = new PagamentoService(total -> {
            chamadas.incrementAndGet();

            if (chamadas.get() == 1) {
                throw new IllegalStateException();
            }

            return true;
        });
        assertTrue(p.pagar(200, 4));
        assertEquals(2, chamadas.get());
    }

    @Test
    void pagamentoRecusado() {
        var p = new PagamentoService(total -> false);
        assertFalse(p.pagar(200, 2));
    }

    @Test
    void pagamentoAprovado() {
        var p = new PagamentoService(total -> true);
        assertTrue(p.pagar(200, 2));
    }

    @Test
    void tentativasInvalidas() {
        var p = new PagamentoService(total -> true);

        assertThrows(IllegalArgumentException.class, () -> p.pagar(200, 0));
        assertThrows(IllegalArgumentException.class, () -> p.pagar(200, 5));
    }

    @Test
    void totalInvalido() {
        var p = new PagamentoService(total -> true);

        assertThrows(IllegalArgumentException.class, () -> p.pagar(0, 2));
        assertThrows(IllegalArgumentException.class, () -> p.pagar(-50, 2));
    }
}