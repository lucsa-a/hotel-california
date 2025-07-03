package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuartoSingleTest {
    @Test
    public void testValorBasicoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new QuartoSingle(Integer.parseInt("123"), 0, 13);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("VALOR INVÁLIDO"));
    }
    @Test
    public void testValorPorPessoaInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new QuartoSingle(Integer.parseInt("123"), 113, 0);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("VALOR INVÁLIDO"));
    }

    @Test
    public void testcalcularValorDiaria() {
        Quarto quartoSingle = new QuartoSingle(Integer.parseInt("123"), 113, 13);
        assertEquals(126.00, quartoSingle.calcularValorDiaria());
    }
    @Test
    public void testToString() {
        Quarto quartoSingle = new QuartoSingle(Integer.parseInt("123"), 113, 13);
        assertEquals("[123] Quarto Single (custo basico: R$113,00; por pessoa: R$13,00 >>> R$126,00 diária)", quartoSingle.toString());
    }

}