package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

public class ReservaRestauranteTest {

    private Usuario clienteBase;
    private Refeicao refeicaoBase;
    private ReservaRestaurante reservaRestaurante;

    @BeforeEach
    public void preparaReservaRestaurante() {
        this.clienteBase = new Usuario("CLI1", "Taylor Swift", 131313, "CLI");
        this.refeicaoBase = new Refeicao(Long.parseLong("1313"), "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true);

        ArrayList<Refeicao> refeicoes = new ArrayList<>();
        refeicoes.add(refeicaoBase);

        LocalDateTime dataInicio = LocalDateTime.of(2023, Month.DECEMBER, 13, 14, 0);
        LocalDateTime dataFinal = LocalDateTime.of(2024, Month.JANUARY, 13, 12, 0);

        this.reservaRestaurante = new ReservaRestaurante(Long.parseLong("13"), clienteBase, dataInicio, dataFinal, 13, this.refeicaoBase);

    }

    @Test
    public void testCalcularTotalReserva() {
        assertEquals((13 * 32 * 13), this.reservaRestaurante.calcularTotalReserva());
    }

    @Test
    public void testCalcularDiariasReserva() {
        assertEquals(32, this.reservaRestaurante.calculaDiarias());
    }

    @Test
    public void testVerificarTipoReserva() {
        assertTrue(this.reservaRestaurante.verificarTipoReserva("RESTAURANTE"));
    }

    @Test
    public void testVerificarTipoReservaIncorreto() {
        assertFalse(this.reservaRestaurante.verificarTipoReserva("QUARTO"));
    }

    @Test
    public void testSetStatusPagamento() {
        this.reservaRestaurante.setStatusPagamento(true);
        assertTrue(this.reservaRestaurante.getStatusPagamento());
    }

    @Test
    public void testExibirReserva() {
        assertTrue(this.reservaRestaurante.toString().contains("[13] Reserva de RESTAURANTE em favor de:"));
        assertTrue(this.reservaRestaurante.toString().contains(clienteBase.toString()));
        assertTrue(this.reservaRestaurante.toString().contains("- Periodo: 13/12/2023 06:13:00 ate 13/01/2024 09:13:00"));
        assertTrue(this.reservaRestaurante.toString().contains("- Qtde. de Convidados: 13 pessoa(s)"));
        assertTrue(this.reservaRestaurante.toString().contains(refeicaoBase.toString()));
        assertTrue(this.reservaRestaurante.toString().contains("VALOR TOTAL DA RESERVA: R$169,00 x32 (diarias) => R$5.408,00"));
        assertTrue(this.reservaRestaurante.toString().contains("SITUACAO DO PAGAMENTO: PENDENTE"));
    }

    @Test
    public void testExibirReservaPaga() {
        this.reservaRestaurante.setStatusPagamento(true);
        assertTrue(this.reservaRestaurante.toString().contains("SITUACAO DO PAGAMENTO: REALIZADO"));
    }

}
