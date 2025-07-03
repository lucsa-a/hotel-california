package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.pagamento;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.formaDePagamento.FormaDePagamento;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.Reserva;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.ReservaRestaurante;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoTest {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Refeicao refeicao;
    private Reserva reserva;
    private FormaDePagamento formaDePagamentoPix;
    private FormaDePagamento formaDePagamentoCartao;
    private FormaDePagamento formaDePagamentoDinheiro;
    private Usuario usuarioCliente;
    @BeforeEach
    public void preparaPagamentoController() {

        this.dataInicio = LocalDateTime.of(2023, 12, 11, 13, 0, 0);
        this.dataFim = LocalDateTime.of(2023, 12, 13, 13, 0, 0);

        this.usuarioCliente = new Usuario("CLI1", "Peppa Pig",  151515, "CLI");

        this.refeicao = new Refeicao((Long.parseLong("1212")), "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true);
        this.reserva = new ReservaRestaurante(Long.parseLong("1313"), this.usuarioCliente, dataInicio, dataFim, 13, refeicao);

        this.formaDePagamentoPix = new FormaDePagamento(1, "PIX", 0.05);
        this.formaDePagamentoCartao = new FormaDePagamento(2, "CARTAO", 0);
        this.formaDePagamentoDinheiro = new FormaDePagamento(3, "DINHEIRO", 0.1);

    }


    @Test
    void testPagamentoComReservaNula() {
        assertThrows(NullPointerException.class, () -> {
            new Pagamento(null, formaDePagamentoPix, usuarioCliente.getNome());
        });
    }
    @Test
    void testPagamentoComFormaDePagamentoNula() {
        assertThrows(NullPointerException.class, () -> {
            new Pagamento(reserva, null, usuarioCliente.getNome());
        });
    }
    @Test
    void testPagamentoComNomeNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Pagamento(reserva, formaDePagamentoCartao, null);
        });
    }

    @Test
    void testPagamentoComNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pagamento(reserva, formaDePagamentoDinheiro, "");
        });
    }

    @Test
    void testPagamentoComParcelaAbaixoDoPermitido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new Pagamento(reserva, formaDePagamentoCartao, usuarioCliente.getNome(), 0);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("QUANTIDADE DE PARCELAS INVALIDA"));
    }

    @Test
    void testPagamentoComParcelaAcimaDoPermitido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            new Pagamento(reserva, formaDePagamentoCartao, usuarioCliente.getNome(), 13);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("QUANTIDADE DE PARCELAS INVALIDA"));
    }

    @Test
    void calculaValorEfetivoPix() {
        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoPix, usuarioCliente.getNome());
        assertEquals(481.65, pagamento.calculaValorEfetivo(507));
    }
    @Test
    void calculaValorEfetivoDinheiro() {
        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoDinheiro, usuarioCliente.getNome());
        assertEquals(456.3, pagamento.calculaValorEfetivo(507));
    }
    @Test
    void calculaValorEfetivoCartao() {
        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoCartao, usuarioCliente.getNome());
        assertEquals(507, pagamento.calculaValorEfetivo(507));
    }

    @Test
    void testExibirPagamentoPix() {
        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoPix, usuarioCliente.getNome());
        assertEquals("SITUACAO DO PAGAMENTO: REALIZADO\n" +
                "[1] Forma de pagamento: PIX (5% de desconto em pagamentos)\n" +
                "Total Efetivamente Pago: R$481,65 em 1x de R$481,65", pagamento.toString());
    }

    @Test
    void testExibirPagamentoDinheiro() {
        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoDinheiro, usuarioCliente.getNome());
        assertEquals("SITUACAO DO PAGAMENTO: REALIZADO\n" +
                "[3] Forma de pagamento: DINHEIRO (10% de desconto em pagamentos)\n" +
                "Total Efetivamente Pago: R$456,30 em 1x de R$456,30", pagamento.toString());
    }

    @Test
    void testExibirPagamentoCartaoSemParcelas() {
        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoCartao, usuarioCliente.getNome());
        assertEquals("SITUACAO DO PAGAMENTO: REALIZADO\n" +
                "[2] Forma de pagamento: CARTAO (0% de desconto em pagamentos)\n" +
                "Total Efetivamente Pago: R$507,00 em 1x de R$507,00", pagamento.toString());
    }



    @Test
    void testExibirPagamentoCartaoComParcelas() {
        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoCartao, usuarioCliente.getNome(), 3);
        assertEquals("SITUACAO DO PAGAMENTO: REALIZADO\n" +
                "[2] Forma de pagamento: CARTAO (0% de desconto em pagamentos)\n" +
                "Total Efetivamente Pago: R$507,00 em 3x de R$169,00", pagamento.toString());
    }


}