package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.formaDePagamento;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FormaDePagamentoTest {

    @Test
    public void testFormaDePagamentoComTipoNulo() {
        assertThrows(NullPointerException.class, () -> {
            new FormaDePagamento(1, null, 0.0);
        });
    }

    @Test
    public void testFormaDePagamentoComTipoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FormaDePagamento(1, " ", 0.0);
        });
    }

    @Test
    public void testFormaDePagamentoComPercentualInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new FormaDePagamento(1, "CARTAO_DE_CREDITO", -1.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "PERCENTUAL INVÁLIDO");
    }

    @Test
    public void testToString() {
        FormaDePagamento formaDePagamento = new FormaDePagamento(1, "CARTAO_DE_CREDITO", 0.0);
        assertEquals(formaDePagamento.toString(), "[1] Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos)");
    }
}
