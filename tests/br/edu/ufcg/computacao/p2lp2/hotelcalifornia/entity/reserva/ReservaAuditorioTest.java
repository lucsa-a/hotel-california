package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.areaComum.AreaComum;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.QuartoSingle;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaAuditorioTest {
    private Usuario clienteBase;
    private AreaComum auditorio;
    private AreaComum auditorioGratis;
    private ReservaAuditorio reservaAuditorio;
    private ReservaAuditorio reservaAuditorioGratis;

    @BeforeEach
    public void preparaReservaAuditorio() {
        LocalTime horaInicio = LocalTime.of(17, 0);
        LocalTime horaFinal = LocalTime.of(20, 15);

        this.clienteBase = new Usuario("CLI1", "Taylor Swift", 131313, "CLI");
        this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 500, true, 1000);
        this.auditorioGratis = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 0, true, 1000);

        ArrayList<Refeicao> refeicoes = new ArrayList<>();

        LocalDateTime dataInicio = LocalDateTime.of(2023, Month.DECEMBER, 24, 17, 0);
        LocalDateTime dataFinal = LocalDateTime.of(2023, Month.DECEMBER, 26, 20, 15);

        this.reservaAuditorio = new ReservaAuditorio(Long.parseLong("1"), this.clienteBase, this.auditorio, dataInicio, dataFinal, 430);
        this.reservaAuditorioGratis = new ReservaAuditorio(Long.parseLong("1"), this.clienteBase, this.auditorioGratis, dataInicio, dataFinal, 430);

    }

    @Test
    public void testSetStatusPagamento() {
        this.reservaAuditorio.setStatusPagamento(true);
        assertTrue(this.reservaAuditorio.getStatusPagamento());
    }

    @Test
    public void testCalcularDiariasReserva() {
        assertEquals(3, this.reservaAuditorio.calculaNumeroDiarias());
    }

    @Test
    public void testVerificarTipoReserva() {
        assertTrue(this.reservaAuditorio.verificarTipoReserva("AUDITORIO"));
    }

    @Test
    public void testVerificarTipoReservaIncorreto() {
        assertFalse(this.reservaAuditorio.verificarTipoReserva("RESTAURANTE"));
    }

    @Test
    public void testSetQtdPessoas() {
        this.reservaAuditorio.setQtdPessoas(500);
        assertEquals(500, this.reservaAuditorio.getQtdPessoas());
    }

    @Test
    public void testCalculaValor() {
        assertEquals((430 * 3 * 500), this.reservaAuditorio.calcularTotalReserva());
    }

    @Test
    public void testToString() {
        assertTrue(this.reservaAuditorio.toString().contains("[1] Reserva de AUDITORIO em favor de:"));
        assertTrue(this.reservaAuditorio.toString().contains(this.clienteBase.toString()));
        assertTrue(this.reservaAuditorio.toString().contains("- Periodo: 24/12/2023 17:00:00 ate 26/12/2023 20:15:00"));
        assertTrue(this.reservaAuditorio.toString().contains("- Qtde. de Convidados: 430 pessoa(s)"));
        assertTrue(this.reservaAuditorio.toString().contains("- Valor por pessoa: R$500,00"));
        assertTrue(this.reservaAuditorio.toString().contains("VALOR TOTAL DA RESERVA: R$215.000,00 x3 (diarias) => R$645.000,00"));
        assertTrue(this.reservaAuditorio.toString().contains("SITUACAO DO PAGAMENTO: PENDENTE."));
    }

    @Test
    public void testToStringReservaPaga() {
        this.reservaAuditorio.setStatusPagamento(true);
        assertTrue(this.reservaAuditorio.toString().contains("[1] Reserva de AUDITORIO em favor de:"));
        assertTrue(this.reservaAuditorio.toString().contains(this.clienteBase.toString()));
        assertTrue(this.reservaAuditorio.toString().contains("- Periodo: 24/12/2023 17:00:00 ate 26/12/2023 20:15:00"));
        assertTrue(this.reservaAuditorio.toString().contains("- Qtde. de Convidados: 430 pessoa(s)"));
        assertTrue(this.reservaAuditorio.toString().contains("- Valor por pessoa: R$500,00"));
        assertTrue(this.reservaAuditorio.toString().contains("VALOR TOTAL DA RESERVA: R$215.000,00 x3 (diarias) => R$645.000,00"));
        assertTrue(this.reservaAuditorio.toString().contains("SITUACAO DO PAGAMENTO: REALIZADO."));
    }

    @Test
    public void testToStringReservaGratis() {
        assertTrue(this.reservaAuditorioGratis.toString().contains("- Valor por pessoa: Grátis"));
        assertTrue(this.reservaAuditorioGratis.toString().contains("VALOR TOTAL DA RESERVA: Grátis"));
        assertTrue(this.reservaAuditorioGratis.toString().contains("SITUACAO DO PAGAMENTO: REALIZADO."));
    }

}
