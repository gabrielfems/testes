package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void calculaTotalEDisponibilidade() {
        var i = new ItemPedido("1478", 2_500, 3, 3, 100, false);
        assertEquals(7_500, i.totalCentavos());
        assertTrue(i.disponivel());
    }

    @Test
    void rejeitaPesoForaDoIntervalo() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("930", 2_000, 1, 1, 0, false));
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("471", 2_000, 1, 1, 100_001, false));
    }

    @Test
    void rejeitaEstoqueNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("218", 2_000, 1, -3, 100, false));
    }

    @Test
    void rejeitaQuantidadeForaDoIntervalo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("745", 2_000, -2, 1, 100, false));
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("389", 2_000, 105, 1, 100, false));
    }

    @Test
    void rejeitaPrecoForaDoIntervalo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("607", 0, 1, 1, 100, false));
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("933", 1_000_001, 1, 1, 100, false));
    }

    @Test
    void rejeitaSkuNuloOuVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido(null, 2_000, 1, 1, 100, false));
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPedido("", 2_000, 1, 1, 100, false));
    }

    @Test
    void aceitaLimitesValidos() {
        var i = new ItemPedido("672", 1_000_000, 100, 100, 100_000, true);
        assertEquals(100_000_000L, i.totalCentavos());
        assertTrue(i.disponivel());
        assertTrue(i.fragil());
    }

    @Test
    void quantidadeZeroTemTotalZeroEContinuaDisponivel() {
        var i = new ItemPedido("841", 2_000, 0, 0, 100, false);
        assertEquals(0, i.totalCentavos());
        assertTrue(i.disponivel());
    }

    @Test
    void quantidadeMaiorQueEstoqueIndisponivel() {
        var i = new ItemPedido("506", 2_000, 5, 4, 100, false);
        assertFalse(i.disponivel());
    }
}