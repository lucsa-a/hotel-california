package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.areaComum;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AreaComumTest {
    private LocalTime horaInicio;
    private LocalTime horaFinal;
    private AreaComum auditorio;
    private AreaComum cinema;
    private AreaComum piscina;
    private AreaComum salaoDeFesta;

    @BeforeEach
    public void preparaAreaComum() {
        //Inicialização de horas padrão
        horaInicio = LocalTime.of(17, 0);
        horaFinal = LocalTime.of(20, 15);

    }

    @Test
    public void testCriarAuditorio() {
        this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 130);
        assertFalse(this.auditorio == null);
    }

    @Test
    public void testCriarCinema() {
        this.cinema = new AreaComum(Long.parseLong("1"), "CINEMA", "FILME DA THE ERAS TOUR", horaInicio, horaFinal, 130, true, 130);
        assertFalse(this.cinema == null);
    }

    @Test
    public void testCriarPiscina() {
        this.piscina = new AreaComum(Long.parseLong("1"), "PISCINA", "PISCINA DE YNTCD", horaInicio, horaFinal, 130, true, 130);
        assertFalse(this.piscina == null);
    }

    @Test
    public void testCriarSalaoDeFesta() {
        this.salaoDeFesta = new AreaComum(Long.parseLong("1"), "SALAO_DE_FESTA", "LOVER HOUSE", horaInicio, horaFinal, 130, true, 130);
        assertFalse(this.salaoDeFesta == null);
    }

    @Test
    public void testTipoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "QUADRA_DE_ESPORTES", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 130);
        });

        assertTrue(hce.getMessage().contains("TIPO INVALIDO"));
    }

    @Test
    public void testCapacidadeZero() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 0);
        });

        assertTrue(hce.getMessage().contains("VALOR INVÁLIDO"));
    }

    @Test
    public void testCapacidadeNegativa() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, -20);
        });

        assertTrue(hce.getMessage().contains("VALOR INVÁLIDO"));
    }

    @Test
    public void testAlterarDados() {
        this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 130);
        this.auditorio.alterarDados(LocalTime.of(12, 0), LocalTime.of(17, 0), 55.2, 150, false);

        assertEquals(LocalTime.of(12, 0), this.auditorio.getHorarioInicio());
        assertEquals(LocalTime.of(17, 0), this.auditorio.getHorarioFinal());
        assertEquals(55.2, this.auditorio.getValorPessoa());
        assertEquals(150, this.auditorio.getQtdMaxPesoas());
        assertFalse(this.auditorio.isDisponivel());
    }

    @Test
    public void testValorNegativo() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, -50, true, 130);
        });

        assertTrue(hce.getMessage().contains("VALOR POR PESSOA INVALIDO"));
    }

    @Test
    public void testHorarioInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "QUADRA_DE_ESPORTES", "ALLIANZ PARK", horaFinal, horaInicio, 130, true, 130);
        });

        assertTrue(hce.getMessage().contains("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO"));
    }

    @Test
    public void testAreaComumParametrosNulos() {
        assertThrows(NullPointerException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), null, "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 130);
        });

        assertThrows(NullPointerException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", null, horaInicio, horaFinal, 130, true, 130);
        });

        assertThrows(NullPointerException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), null, null, horaInicio, horaFinal, 130, true, 130);
        });
    }

    @Test
    public void testAreaComumParametrosVazios() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 130);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "", horaInicio, horaFinal, 130, true, 130);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.auditorio = new AreaComum(Long.parseLong("1"), "", "", horaInicio, horaFinal, 130, true, 130);
        });
    }

    @Test
    public void testTostring() {
        this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 130);
        assertEquals("[1] AUDITORIO: ALLIANZ PARK (17h00 as 20h15). Valor por pessoa: R$130,00. Capacidade: " +
                "130 pessoa(s). VIGENTE.",this.auditorio.toString());
    }

    @Test
    public void testTostringIndisponivel() {
        this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, false, 130);
        assertEquals("[1] AUDITORIO: ALLIANZ PARK (17h00 as 20h15). Valor por pessoa: R$130,00. Capacidade: " +
                "130 pessoa(s). INDISPONIVEL.",this.auditorio.toString());
    }

    @Test
    public void testTostringGratis() {
        this.auditorio = new AreaComum(Long.parseLong("1"), "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 0, true, 130);
        assertEquals("[1] AUDITORIO: ALLIANZ PARK (17h00 as 20h15). Valor por pessoa: Grátis. Capacidade: " +
                "130 pessoa(s). VIGENTE.",this.auditorio.toString());
    }

}
