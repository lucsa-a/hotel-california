package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuartoFamilyTest {

    @Test
    public void testValorBasicoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new QuartoFamily(Integer.parseInt("123"), 10,0, 13, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("VALOR INVÁLIDO"));
    }
    @Test
    public void testValorPorPessoaInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new QuartoFamily(Integer.parseInt("123"), 10,131, 0, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("VALOR INVÁLIDO"));
    }

    @Test
    void testQuantidadeDeHospedesAcimaDoMaximo() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new QuartoFamily(Integer.parseInt("123"), 11, 313, 13, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("QUANTIDADE DE HOSPÉDES DEVE SER NO MÁXIMO 10"));
    }

    @Test
    void testQuantidadeDeHospedesInferiorAoMinimo() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new QuartoFamily(Integer.parseInt("123"), 0, 313, 13, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("QUANTIDADE DE HOSPÉDES DEVE SER NO MÍNIMO 1"));
    }

    @Test
    public void testcalcularValorDiaria() {
        Quarto quartoFamily = new QuartoFamily(Integer.parseInt("123"), 5,313, 13, null);
        assertEquals(378.00, quartoFamily.calcularValorDiaria());
    }
    @Test
    void testToStringSemPedidosDeMelhoria() {
        Quarto quartoFamily = new QuartoFamily(Integer.parseInt("123"), 10, 313, 13, null);
        assertEquals("[123] Quarto Family (custo basico: R$313,00; por pessoa: R$13,00 >>> R$443,00 diária). Capacidade: 10 pessoa(s). Pedidos: (nenhum)", quartoFamily.toString());
    }
    @Test
    void testToString() {
        Quarto quartoFamily = new QuartoFamily(Integer.parseInt("123"), 10, 313, 13, new String[]{"Disponibilizar rede na varanda", "Televisão com acesso à Netflix"});
        assertEquals("[123] Quarto Family (custo basico: R$313,00; por pessoa: R$13,00 >>> R$443,00 diária). Capacidade: 10 pessoa(s). Pedidos: [Disponibilizar rede na varanda, Televisão com acesso à Netflix]", quartoFamily.toString());
    }
}