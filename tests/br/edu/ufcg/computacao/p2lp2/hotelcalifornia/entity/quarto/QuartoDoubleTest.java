package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuartoDoubleTest {
     @Test
    public void testValorBasicoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new QuartoDouble(Integer.parseInt("123"), 0, 13, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("VALOR INVÁLIDO"));
    }
    @Test
    public void testValorPorPessoaInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new QuartoDouble(Integer.parseInt("123"), 131, 0, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("VALOR INVÁLIDO"));
    }

    @Test
    public void testcalcularValorDiaria() {
        Quarto quartoDouble = new QuartoDouble(Integer.parseInt("123"), 131, 13, null);
        assertEquals(157.00, quartoDouble.calcularValorDiaria());
    }
    @Test
    void testToStringSemPedidosDeMelhoria() {
        Quarto quartoDouble = new QuartoDouble(Integer.parseInt("123"), 131, 13, null);
        assertEquals("[123] Quarto Double (custo basico: R$131,00; por pessoa: R$13,00 >>> R$157,00 diária). Pedidos: (nenhum)", quartoDouble.toString());
    }
    @Test
    void testToString() {
        Quarto quartoDouble = new QuartoDouble(Integer.parseInt("123"), 131, 13, new String[]{"Disponibilizar rede na varanda", "Televisão com acesso à Netflix"});
        assertEquals("[123] Quarto Double (custo basico: R$131,00; por pessoa: R$13,00 >>> R$157,00 diária). Pedidos: [Disponibilizar rede na varanda, Televisão com acesso à Netflix]", quartoDouble.toString());
    }
}