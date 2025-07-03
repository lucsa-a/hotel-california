package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class RefeicaoTest {

    @Test
    public void testRefeicaoComIdNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Refeicao(null, "Café da Secret Session", "CAFE_DA_MANHA", LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true);
        });
    }

    @Test
    public void testRefeicaoComTituloNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Refeicao(Long.parseLong("1313"), null, "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true);
        });
    }

    @Test
    public void testRefeicaoComTipoNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Refeicao(Long.parseLong("1313"), "CAFE_DA_MANHA", null, LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true);
        });
    }

    @Test
    public void testRefeicaoComHorarioInicioNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Refeicao(Long.parseLong("1313"), "Café da Secret Session", "CAFE_DA_MANHA", null, LocalTime.of(9, 13), 13, true);
        });
    }

    @Test
    public void testRefeicaoComHorarioFimNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Refeicao(Long.parseLong("1313"), "Café da Secret Session", "CAFE_DA_MANHA", LocalTime.of(6, 13), null, 13, true);
        });
    }

    @Test
    public void testRefeicaoComValorInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new Refeicao(Long.parseLong("1313"), "Café da Secret Session", "CAFE_DA_MANHA", LocalTime.of(6, 13), LocalTime.of(9, 13), 0.0, true);
        });
        assertEquals(hce.getMessage().toUpperCase(), "VALOR INVÁLIDO");
    }

    @Test
    public void testRefeicaoComTituloVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Refeicao(Long.parseLong("1313"), " ", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true);
        });
    }

    @Test
    public void testRefeicaoComTipoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Refeicao(Long.parseLong("1313"), "CAFE_DA_MANHA", " ", LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true);
        });
    }

    @Test
    public void testToString() {
        Refeicao refeicao = new Refeicao(Long.parseLong("1313"), "CAFE_DA_MANHA","Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true);
        assertEquals("[1313] Cafe-da-manha: Café da Secret Session (06h13 as 09h13). Valor por pessoa: R$13,00. VIGENTE", refeicao.toString());
    }
}
