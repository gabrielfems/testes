package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {

    @Test
    void fechaPedido() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO", 12_000, 1, 5, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        List<Long> cobrancas = new ArrayList<>();

        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals("PAGO", resultado.status());
        assertEquals(12_000, resultado.subtotalCentavos());
        assertEquals(1_200, resultado.freteCentavos());
        assertEquals(13_200, resultado.totalCentavos());
        assertEquals(List.of(13_200L), cobrancas);
    }

    @Test
    void pedidoVaiParaRevisao() {
        Cliente cliente = new Cliente(false, false, 0);
        ItemPedido item = new ItemPedido("CANETA", 12_000, 1, 5, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", true, null);

        PedidoService service = new PedidoService(total -> {
            fail("Não deveria chegar no pagamento");
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals("REVISAO", resultado.status());
    }

    @Test
    void clienteNuloDaErro() {
        ItemPedido item = new ItemPedido("CANETA", 12_000, 1, 5, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        PedidoService service = new PedidoService(total -> true);

        assertThrows(NullPointerException.class,
                () -> service.fechar(pedido, null));
    }

    @Test
    void pedidoNuloDaErro() {
        Cliente cliente = new Cliente(false, false, 1);
        PedidoService service = new PedidoService(total -> true);

        assertThrows(NullPointerException.class,
                () -> service.fechar(null, cliente));
    }

    @Test
    void semEstoqueNaoFazCobranca() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("CANETA", 12_000, 3, 2, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        PedidoService service = new PedidoService(total -> {
            fail("Não deveria cobrar");
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals("SEM_ESTOQUE", resultado.status());
        assertEquals(0, resultado.totalCentavos());
    }

    @Test
    void pedidoSemItensDaErro() {
        Cliente cliente = new Cliente(false, false, 1);
        Pedido pedido = new Pedido(
                List.of(new ItemPedido("CANETA", 12_000, 0, 5, 1_500, false)),
                "PR",
                false,
                null
        );

        PedidoService service = new PedidoService(total -> true);

        assertThrows(IllegalArgumentException.class,
                () -> service.fechar(pedido, cliente));
    }

    @Test
    void clienteBloqueadoNaoFazCobranca() {
        Cliente cliente = new Cliente(false, true, 1);
        ItemPedido item = new ItemPedido("CANETA", 12_000, 1, 5, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        PedidoService service = new PedidoService(total -> {
            fail("Não deveria cobrar");
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals("BLOQUEADO", resultado.status());
        assertEquals(0, resultado.totalCentavos());
    }

    @Test
    void totalFechaComOsValoresDoPedido() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("CANETA", 12_000, 1, 5, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        PedidoService service = new PedidoService(total -> true);

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals(
                resultado.subtotalCentavos()
                        - resultado.descontoCentavos()
                        + resultado.freteCentavos(),
                resultado.totalCentavos()
        );
    }

    @Test
    void naoCobraDuasVezes() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("CANETA", 12_000, 1, 5, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        List<Long> cobrancas = new ArrayList<>();

        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        service.fechar(pedido, cliente);

        assertEquals(1, cobrancas.size());
    }

    @Test
    void somaOsItens() {
        Cliente cliente = new Cliente(false, false, 1);

        ItemPedido item1 = new ItemPedido("CANETA", 12_000, 2, 5, 1_500, false);
        ItemPedido item2 = new ItemPedido("D", 6_000, 3, 5, 700, false);

        Pedido pedido = new Pedido(
                List.of(item1, item2),
                "PR",
                false,
                null
        );

        PedidoService service = new PedidoService(total -> true);

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals(42_000, resultado.subtotalCentavos());
        assertEquals("PAGO", resultado.status());
    }

    @Test
    void pedidoVipTemDesconto() {
        Cliente cliente = new Cliente(true, false, 1);
        ItemPedido item = new ItemPedido("CANETA", 12_000, 1, 5, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        PedidoService service = new PedidoService(total -> true);

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals("PAGO", resultado.status());
        assertTrue(resultado.descontoCentavos() > 0);
    }

    @Test
    void pagamentoRecusado() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("CANETA", 12_000, 1, 5, 1_500, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        PedidoService service = new PedidoService(total -> false);

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertEquals("PAGAMENTO_RECUSADO", resultado.status());
    }
}