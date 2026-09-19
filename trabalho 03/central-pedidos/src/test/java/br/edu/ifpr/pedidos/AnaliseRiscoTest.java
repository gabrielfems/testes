package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {

    private final AnaliseRisco risco = new AnaliseRisco();

    @Test
    void aprovaVipComPedidoExpresso() {
        assertEquals("APROVADO",
                risco.avaliar(cliente(true, false, 15), 950_000, true));
    }

    @Test
    void aprovaClienteVipIndependenteDoValor() {
        assertEquals("APROVADO",
                risco.avaliar(cliente(true, false, 3), 650_000, false));
    }

    @Test
    void aprovaClienteHistoricoNoLimiteExato() {
        assertEquals("APROVADO",
                risco.avaliar(cliente(false, false, 2), 500_000, false));
    }

    @Test
    void enviaParaRevisaoClienteComHistoricoEValorAlto() {
        assertEquals("REVISAO",
                risco.avaliar(cliente(false, false, 1), 650_000, false));
    }

    @Test
    void enviaParaRevisaoPrimeiraCompraExpressa() {
        assertEquals("REVISAO",
                risco.avaliar(cliente(false, false, 0), 20_000, true));
    }

    @Test
    void aprovaPrimeiraCompraNoLimiteExato() {
        assertEquals("APROVADO",
                risco.avaliar(cliente(false, false, 0), 100_000, false));
    }

    @Test
    void enviaParaRevisaoPrimeiraCompraValorAlto() {
        assertEquals("REVISAO",
                risco.avaliar(cliente(false, false, 0), 250_000, false));
    }

    @Test
    void recusaClienteBloqueado() {
        assertEquals("RECUSADO",
                risco.avaliar(cliente(false, true, 5), 750_000, true));
    }

    @Test
    void rejeitaTotalComValorNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> risco.avaliar(cliente(false, false, 1), -100, false));
    }

    private static Cliente cliente(boolean vip, boolean bloqueado, int compras) {
        return new Cliente(vip, bloqueado, compras);
    }
}