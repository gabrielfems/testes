package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private static ItemPedido item(String sku, int qtd, int estoque, int peso, boolean fragil) {
        return new ItemPedido(sku, 1_500, qtd, estoque, peso, fragil);
    }

    @Test
    void calculaSubtotal() {
        var pedido = new Pedido(List.of(
                item("A", 3, 6, 150, false),
                item("B", 0, 6, 250, true)
        ), "PR", false, null);

        assertEquals(4_500, pedido.subtotalCentavos());
    }

    @Test
    void aceitaUfValida() {
        var pedido = new Pedido(
                List.of(),
                "SP",
                true,
                "PROMO10"
        );

        assertEquals("SP", pedido.uf());
        assertTrue(pedido.expresso());
        assertEquals("PROMO10", pedido.cupom());
    }

    @Test
    void rejeitaUfInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(List.of(), null, false, null));

        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(List.of(), "sp", false, null));

        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(List.of(), "SP1", false, null));
    }

    @Test
    void aceitaCemItens() {
        var itens = new ArrayList<ItemPedido>();

        for (int i = 0; i < 100; i++) {
            itens.add(item("Y" + i, 1, 1, 1, false));
        }

        var pedido = new Pedido(itens, "PR", false, null);

        assertEquals(150_000, pedido.subtotalCentavos());
    }

    @Test
    void rejeitaListaInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(null, "PR", false, null));

        var itens = new ArrayList<ItemPedido>();

        for (int i = 0; i < 101; i++) {
            itens.add(item("Y" + i, 1, 1, 1, false));
        }

        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(itens, "PR", false, null));
    }

    @Test
    void aceitaListaVazia() {
        var pedido = new Pedido(List.of(), "PR", false, null);

        assertEquals(0, pedido.subtotalCentavos());
        assertEquals(0, pedido.pesoGramas());
        assertFalse(pedido.temFragil());
        assertTrue(pedido.estoqueSuficiente());
    }

    @Test
    void estoqueSuficiente() {
        var pedido = new Pedido(List.of(
                item("A", 1, 3, 150, false),
                item("B", 2, 2, 150, false)
        ), "PR", false, null);

        assertTrue(pedido.estoqueSuficiente());
    }

    @Test
    void verificaEstoque() {
        var pedido = new Pedido(List.of(
                item("A", 2, 1, 150, false),
                item("B", 3, 1, 150, false)
        ), "PR", false, null);

        assertFalse(pedido.estoqueSuficiente());
    }

    @Test
    void ignoraFragilComQuantidadeZero() {
        var pedido = new Pedido(List.of(
                item("A", 0, 6, 150, true)
        ), "PR", false, null);

        assertFalse(pedido.temFragil());
    }

    @Test
    void verificaFragil() {
        var pedido = new Pedido(List.of(
                item("A", 2, 6, 150, true)
        ), "PR", false, null);

        assertTrue(pedido.temFragil());
    }

    @Test
    void calculaPeso() {
        var pedido = new Pedido(List.of(
                item("A", 3, 6, 150, false),
                item("B", 4, 6, 250, false)
        ), "SP", false, null);

        assertEquals(1_450, pedido.pesoGramas());
    }

    @Test
    void naoAceitaItemNulo() {
        assertThrows(NullPointerException.class,
                () -> new Pedido(List.of(
                        item("A", 2, 2, 150, false),
                        null
                ), "PR", false, null));
    }

    @Test
    void copiaALista() {
        var lista = new ArrayList<>(List.of(
                item("A", 2, 6, 150, false)
        ));

        var pedido = new Pedido(lista, "PR", false, null);
        lista.clear();

        assertEquals(3_000, pedido.subtotalCentavos());
    }
}