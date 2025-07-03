package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.*;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

public class ReservaQuartoTest {

    private Usuario clienteBase;
    private QuartoSingle quartoQuartoSingleBase;
    private ReservaQuarto reservaQuartoBase;
    private Refeicao refeicaoBase;

    @BeforeEach
    public void preparaReservaQuarto() {
        this.clienteBase = new Usuario("CLI1", "Taylor Swift", 131313, "CLI");
        this.quartoQuartoSingleBase = new QuartoSingle(013, 130.00, 13.00);
        this.refeicaoBase = new Refeicao(Long.parseLong("1313"), "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.3, true);

        ArrayList<Refeicao> refeicoes = new ArrayList<>();
        refeicoes.add(refeicaoBase);

        LocalDateTime dataInicio = LocalDateTime.of(2023, Month.DECEMBER, 13, 14, 0);
        LocalDateTime dataFinal = LocalDateTime.of(2024, Month.JANUARY, 13, 12, 0);

        this.reservaQuartoBase = new ReservaQuarto(Long.parseLong("13"), clienteBase, quartoQuartoSingleBase, 1, dataInicio, dataFinal, refeicoes);

    }

    @Test
    public void testCalcularTotalReserva() {
        assertEquals((31 * (130 + 1 * 13) + 31 * 1 * 13.3), this.reservaQuartoBase.calcularTotalReserva());
    }

    @Test
    public void testCalcularDiariasReserva() {
        assertEquals(31, this.reservaQuartoBase.calculaNumeroDiarias());
    }

    @Test
    public void testVerificarTipoReserva() {
        assertTrue(this.reservaQuartoBase.verificarTipoReserva("QUARTO"));
    }

    @Test
    public void testVerificarTipoReservaIncorreto() {
        assertFalse(this.reservaQuartoBase.verificarTipoReserva("RESTAURANTE"));
    }

    @Test
    public void testSetStatusPagamento() {
        this.reservaQuartoBase.setStatusPagamento(true);
        assertTrue(this.reservaQuartoBase.getStatusPagamento());
    }

    @Test
    public void testExibirReserva() {
        assertTrue(this.reservaQuartoBase.toString().contains("[13] Reserva de quarto em Favor de:"));
        assertTrue(this.reservaQuartoBase.toString().contains(clienteBase.toString()));
        assertTrue(this.reservaQuartoBase.toString().contains(quartoQuartoSingleBase.toString()));
        assertTrue(this.reservaQuartoBase.toString().contains("- Período: 13/12/2023 14:00:00 ate 13/01/2024 12:00:00"));
        assertTrue(this.reservaQuartoBase.toString().contains("- No. Hóspedes: 1 pessoa(s)"));
        assertTrue(this.reservaQuartoBase.toString().contains(refeicaoBase.toString()));
        assertTrue(this.reservaQuartoBase.toString().contains("VALOR TOTAL DA RESERVA: R$156,30 x31 (diarias) => R$4.845,30"));
        assertTrue(this.reservaQuartoBase.toString().contains("SITUACAO DO PAGAMENTO: PENDENTE"));
    }

    @Test
    public void testExibirReservaPaga() {
        this.reservaQuartoBase.setStatusPagamento(true);
        assertTrue(this.reservaQuartoBase.toString().contains("SITUACAO DO PAGAMENTO: REALIZADO"));
    }

}
