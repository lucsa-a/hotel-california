package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.areaComum.AreaComum;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AreaComumControllerTest {
    private AreaComumController areaComumController;
    private UsuarioController usuarioController;
    private LocalTime horaInicio;
    private LocalTime horaFinal;

    @BeforeEach
    public void preparaAreaComumController() {
        //Inicialização dos controllers
        this.areaComumController = AreaComumController.getInstance();
        this.areaComumController.init();

        this.usuarioController = UsuarioController.getInstance();
        this.usuarioController.init();

        //Inicialização de horas padrão
        horaInicio = LocalTime.of(17, 0);
        horaFinal = LocalTime.of(20, 15);
    }

    @Test
    public void testDisponibilizarAreaComum() {
       this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);

       assertEquals(1, areaComumController.qtdAreasComuns());
       assertTrue(areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testDisponibilizarAreaComumUsuarioNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.disponibilizarAreaComum("ADM13", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        });

        assertTrue(hce.getMessage().contains("USUARIO NAO EXISTE"));

        assertEquals(0, areaComumController.qtdAreasComuns());
        assertFalse(areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testClienteNaoPodeDisponibilizarAreaComum() {
        this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.disponibilizarAreaComum("CLI2", "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 150);
        });

        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA AREA COMUM"));

        assertEquals(0, areaComumController.qtdAreasComuns());
        assertFalse(areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testGerenteNaoPodeDisponibilizarAreaComum() {
        this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "GER", 131313);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.disponibilizarAreaComum("GER2", "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 150);
        });

        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA AREA COMUM"));

        assertEquals(0, areaComumController.qtdAreasComuns());
        assertFalse(areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testFuncionarioNaoPodeDisponibilizarAreaComum() {
        this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "FUN", 131313);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.disponibilizarAreaComum("FUN2", "AUDITORIO", "ALLIANZ PARK", horaInicio, horaFinal, 130, true, 150);
        });

        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA AREA COMUM"));

        assertEquals(0, this.areaComumController.qtdAreasComuns());
        assertFalse(this.areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testDisponibilizarAreaComumJaExiste() {
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 130, true, 150);
        });
        assertTrue(hce.getMessage().contains("AREA COMUM JA EXISTE"));

        assertEquals(1, this.areaComumController.qtdAreasComuns());
        assertTrue(this.areaComumController.verificarAreaComum(Long.parseLong("1")));
        assertFalse(this.areaComumController.verificarAreaComum(Long.parseLong("2")));
    }

    @Test
    public void testAlterarAreaComum() {
        String areaComum = this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        String areaComumAlterada = this.areaComumController.alterarAreaComum("ADM1", Long.parseLong("1"), LocalTime.of(12, 0), LocalTime.of(17, 0), 55.2, 150, false);

        assertNotEquals(areaComum, areaComumAlterada);
        assertEquals(1, areaComumController.qtdAreasComuns());
        assertTrue(areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testAlterarAreaComumUsuarioNaoExiste() {
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.alterarAreaComum("ADM13", Long.parseLong("1"), LocalTime.of(12, 0), LocalTime.of(17, 0), 55.2, 150, false);
        });

        assertTrue(hce.getMessage().contains("USUARIO NAO EXISTE"));

        assertEquals(1, areaComumController.qtdAreasComuns());
        assertTrue(areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testClienteNaoPodeAlterarAreaComum() {
        this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313);
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.alterarAreaComum("CLI2", Long.parseLong("1"), LocalTime.of(12, 0), LocalTime.of(17, 0), 55.2, 150, false);
        });

        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO ALTERAR UMA AREA COMUM"));

        assertEquals(1, areaComumController.qtdAreasComuns());
        assertTrue(areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testGerenteNaoPodeAlterarAreaComum() {
        this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "GER", 131313);
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.alterarAreaComum("GER2", Long.parseLong("1"), LocalTime.of(12, 0), LocalTime.of(17, 0), 55.2, 150, false);
        });

        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO ALTERAR UMA AREA COMUM"));

        assertEquals(1, areaComumController.qtdAreasComuns());
        assertTrue(areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testFuncionarioNaoPodeAlterarAreaComum() {
        this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "FUN", 131313);
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.alterarAreaComum("FUN2", Long.parseLong("1"), LocalTime.of(12, 0), LocalTime.of(17, 0), 55.2, 150, false);
        });

        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO ALTERAR UMA AREA COMUM"));

        assertEquals(1, this.areaComumController.qtdAreasComuns());
        assertTrue(this.areaComumController.verificarAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testAlterarAreaComumNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.alterarAreaComum("ADM1", Long.parseLong("1"), LocalTime.of(12, 0), LocalTime.of(17, 0), 55.2, 120, false);
        });

        assertTrue(hce.getMessage().contains("AREA COMUM NAO EXISTE"));
    }

    @Test
    public void testAlterarAreaComumHorarioInvertido() {
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
           this.areaComumController.alterarAreaComum("ADM1", Long.parseLong("1"), horaFinal, horaInicio, 55.2, 150, false);
                });

        assertTrue(hce.getMessage().contains("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO"));
    }

    @Test
    public void testAlterarAreaComumValorNegativo() {
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.alterarAreaComum("ADM1", Long.parseLong("1"), horaInicio, horaFinal, -200, 150, false);
        });

        assertTrue(hce.getMessage().contains("VALOR POR PESSOA INVALIDO"));
    }

    @Test
    public void testAlterarAreaComumZeroPessoas() {
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.alterarAreaComum("ADM1", Long.parseLong("1"), horaInicio, horaFinal, 55.2, 0, false);
        });

        assertTrue(hce.getMessage().contains("VALOR INVÁLIDO"));
    }

    @Test
    public void testAlterarAreaComumPessoasNegativo() {
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.alterarAreaComum("ADM1", Long.parseLong("1"), horaInicio, horaFinal, 55.2, -50, false);
        });

        assertTrue(hce.getMessage().contains("VALOR INVÁLIDO"));
    }

    @Test
    public void testExibirAreaComum() {
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        assertEquals("[1] AUDITORIO: ALLIANZ PARQUE (17h00 as 20h15). Valor por pessoa: R$150,00. Capacidade: " +
                "150 pessoa(s). VIGENTE.", this.areaComumController.exibirAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testExibirAreaComumGratis() {
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 0, true, 150);
        assertEquals("[1] AUDITORIO: ALLIANZ PARQUE (17h00 as 20h15). Valor por pessoa: Grátis. Capacidade: " +
                "150 pessoa(s). VIGENTE.", this.areaComumController.exibirAreaComum(Long.parseLong("1")));
    }

    @Test
    public void testExibirAreaComumNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.areaComumController.exibirAreaComum(Long.parseLong("1"));
        });

        assertTrue(hce.getMessage().contains("AREA COMUM NAO EXISTE"));
    }

    @Test
    public void testListarAreasComunsSemNenhuma() {
        String[] resultado = this.areaComumController.listarAreasComuns();

        assertEquals(0, resultado.length);
    }

    @Test
    public void testListarAreasComuns() {
        String auditorio = this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 150, true, 150);
        String cinema = this.areaComumController.disponibilizarAreaComum("ADM1", "CINEMA", "FILME DA THE ERAS TOUR", horaInicio, horaFinal, 150, true, 150);
        String piscina =this.areaComumController.disponibilizarAreaComum("ADM1", "PISCINA", "PISCINA DE YNTCD", horaInicio, horaFinal, 150, true, 150);
        String salaoDeFesta = this.areaComumController.disponibilizarAreaComum("ADM1", "SALAO_DE_FESTA", "LOVER HOUSE", horaInicio, horaFinal, 150, true, 150);

        String[] resultado = this.areaComumController.listarAreasComuns();

        ArrayList<String> resultadoArray = new ArrayList(Arrays.asList(resultado));

        assertTrue(resultadoArray.contains(auditorio));
        assertTrue(resultadoArray.contains(cinema));
        assertTrue(resultadoArray.contains(piscina));
        assertTrue(resultadoArray.contains(salaoDeFesta));

        assertEquals(4, resultado.length);
    }

}
