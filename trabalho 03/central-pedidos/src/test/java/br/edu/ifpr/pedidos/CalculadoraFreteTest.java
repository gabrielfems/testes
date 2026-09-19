package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {

    private final CalculadoraFrete frete = new CalculadoraFrete();

    @Test
    void expressoIgnoraFreteGratis() {
        assertEquals(3_200,
                frete.calcular(pedido("PR", true, true, 1_500), cliente(false), 30_000));
    }

    @Test
    void vipComExpressoEProdutoFragil() {
        assertEquals(2_600,
                frete.calcular(pedido("PR", true, true, 1_500), cliente(true), 12_000));
    }

    @Test
    void aplicaTaxaExtraParaProdutoFragil() {
        assertEquals(1_700,
                frete.calcular(pedido("PR", false, true, 1_500), cliente(false), 12_000));
    }

    @Test
    void clienteVipComExcessoDePeso() {
        assertEquals(750,
                frete.calcular(pedido("PR", false, false, 2_500), cliente(true), 12_000));
    }

    @Test
    void clienteVipPagaMetadeDoFrete() {
        assertEquals(600,
                frete.calcular(pedido("PR", false, false, 1_500), cliente(true), 12_000));
    }

    @Test
    void modalidadeExpressaAdicionaTaxa() {
        assertEquals(2_700,
                frete.calcular(pedido("PR", true, false, 1_500), cliente(false), 30_000));
    }

    @Test
    void cobraFreteAbaixoDoLimiteDeIsencao() {
        assertEquals(1_200,
                frete.calcular(pedido("PR", false, false, 1_000), cliente(false), 29_999));
    }

    @Test
    void isentaFreteAcimaDoLimite() {
        assertEquals(0,
                frete.calcular(pedido("PR", false, false, 3_800), cliente(false), 30_000));
    }

    @Test
    void acumulaTaxaEmMultiplasFaixasDePeso() {
        assertEquals(1_800,
                frete.calcular(pedido("PR", false, false, 3_800), cliente(false), 12_000));
    }

    @Test
    void adicionaTaxaPorExcessoDePeso() {
        assertEquals(1_500,
                frete.calcular(pedido("PR", false, false, 2_500), cliente(false), 12_000));
    }

    @Test
    void naoAplicaTaxaAteLimiteDePeso() {
        assertEquals(1_200,
                frete.calcular(pedido("PR", false, false, 2_000), cliente(false), 12_000));
    }

    @Test
    void calculaFreteParaOutrosEstados() {
        assertEquals(3_000,
                frete.calcular(pedido("BA", false, false, 1_500), cliente(false), 15_000));
    }

    @Test
    void calculaFreteParaSpERj() {
        assertEquals(2_000,
                frete.calcular(pedido("SP", false, false, 1_500), cliente(false), 15_000));

        assertEquals(2_000,
                frete.calcular(pedido("RJ", false, false, 1_500), cliente(false), 15_000));
    }

    @Test
    void calculaFreteParaParana() {
        assertEquals(1_200,
                frete.calcular(pedido("PR", false, false, 1_500), cliente(false), 12_000));
    }

    @Test
    void rejeitaValorLiquidoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> frete.calcular(pedido("PR", false, false, 1_000), cliente(false), -50));
    }

    private static Pedido pedido(String uf, boolean expresso, boolean fragil, int peso) {
        return new Pedido(
                List.of(new ItemPedido("2078", 1_500, 2, 4, peso, fragil)),
                uf,
                expresso,
                null
        );
    }

    private static Cliente cliente(boolean vip) {
        return new Cliente(vip, false, 1);
    }
}