package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.ReservaAuditorio;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.*;

import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReservasSessionControllerTest {
    private AreaComumController areaComumController;
    private ReservasSessionController reservasSessionController;
    private UsuarioController usuarioController;
    private QuartoController quartoController;
    private RefeicaoController refeicaoController;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;
    private LocalTime horaInicio;
    private LocalTime horaFinal;
    private String[] refeicoes;

    @BeforeEach
    public void preparaReservasSession() {
        //Inicialização de Controllers
        this.reservasSessionController = ReservasSessionController.getInstance();
        this.reservasSessionController.init();

        this.usuarioController = UsuarioController.getInstance();
        this.usuarioController.init();

        this.quartoController = QuartoController.getInstance();
        this.quartoController.init();

        this.refeicaoController = RefeicaoController.getInstance();
        this.refeicaoController.init();

        this.areaComumController = AreaComumController.getInstance();
        this.areaComumController.init();

        //Inicialização de Usuários
        this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313);
        this.usuarioController.cadastrarUsuario("ADM1", "Beyonce Knowles", "GER", 123456);
        this.usuarioController.cadastrarUsuario("ADM1", "Caroline Polachek", "FUN", 123456);
        this.usuarioController.cadastrarUsuario("ADM1", "Paula Fernandes", "CLI", 141414);

        //Inicialização de Quartos
        this.quartoController.disponibilizarQuartoSingle("ADM1", 113, 130.0, 13.00);
        this.quartoController.disponibilizarQuartoDouble("ADM1", 133, 130.0, 13.00, new String[]{"Quarto acessível para gatos"});
        this.quartoController.disponibilizarQuartoFamily("ADM1", 130, 10, 130.0, 13.00, new String[]{"Quarto acessível para gatos"});

        //Inicialização de Refeições
        this.refeicaoController.disponibilizarRefeicao("FUN4", "CAFE_DA_MANHA", "Café da manhã das secret sessions", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.3, true);
        this.refeicoes = new String[]{"0"};

        //Inicialização de datas padrão
        dataInicio = LocalDateTime.of(2023, Month.DECEMBER, 13, 14, 0);
        dataFinal = LocalDateTime.of(2024, Month.JANUARY, 13, 12, 0);

        //Inicialização de horas padrão
        horaInicio = LocalTime.of(17, 0);
        horaFinal = LocalTime.of(20, 15);

        //Inicialização de Áreas Comuns
        this.areaComumController.disponibilizarAreaComum("ADM1", "AUDITORIO", "ALLIANZ PARQUE", horaInicio, horaFinal, 100, true, 150);
    }

    //Testes de Reserva de Quarto
    @Test
    public void testCriarReservaQuartoSingle() {
        assertEquals(0, this.reservasSessionController.getReservas().size());

        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);

        assertEquals(1, this.reservasSessionController.getReservas().size());
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));

    }

    @Test
    public void testCriarReservaQuartoFamily() {
        assertEquals(0, this.reservasSessionController.getReservas().size());

        this.reservasSessionController.reservarQuartoFamily("GER3", "CLI2", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});

        assertEquals(1, this.reservasSessionController.getReservas().size());
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));

    }

    @Test
    public void testCriarVariasReservas() {
        assertEquals(0, this.reservasSessionController.getReservas().size());

        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        this.reservasSessionController.reservarQuartoFamily("FUN4", "CLI2", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});

        assertEquals(2, this.reservasSessionController.getReservas().size());
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("2")));

    }

    @Test
    public void testClienteNaoPodeCriarReservaDeQuato() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoSingle("CLI2", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));

        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testAdministradorNaoPodeCriarReservaDeQuarto() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("ADM1", "CLI2", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));

        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testFuncionarioNaoPodeReservarQuarto() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoDouble("GER3", "FUN4", 130, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E CLIENTE"));

        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testGerenteNaoPodeReservarQuarto() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("GER3", "GER3", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E CLIENTE"));

        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testAdministradorNaoPodeReservarQuarto() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoSingle("FUN4", "ADM1", 130, dataInicio, dataFinal, refeicoes);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E CLIENTE"));

        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testUsuarioNaoExisteReservaQuarto() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("FUN43", "CLI2", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));

        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservarQuartoIdClienteNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("FUN4", "CLI25", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });
        assertTrue(hce.getMessage().contains("USUARIO NAO EXISTE"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservaQuartoComIdNuloEVazio() {
        assertThrows(NullPointerException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily(null, "CLI2", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("", "CLI2", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservaQuartoComIdClienteNuloEVazio() {
        assertThrows(NullPointerException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("FUN4", null, 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("FUN4", "", 130, 2, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testCriarReservaComHoraIncorreta() {
        assertEquals(0, this.reservasSessionController.getReservas().size());

        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, LocalDateTime.of(2023, Month.DECEMBER, 13, 18, 15), LocalDateTime.of(2024, Month.JANUARY, 13, 22, 13), refeicoes);

        assertEquals(1, this.reservasSessionController.getReservas().size());
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertTrue(reservasSessionController.exibirReserva("CLI2", Long.parseLong("1")).contains("Período: 13/12/2023 14:00:00 ate 13/01/2024 12:00:00"));

    }

    @Test
    public void testNaoPodeReservarQuartoNaMesmaData() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("JA EXISTE RESERVA PARA ESTA DATA"));

        assertEquals(1, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testNaoPodeReservarQuartoComDataOcupada() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, LocalDateTime.of(2023, Month.DECEMBER, 23, 14, 0),  LocalDateTime.of(2024, Month.JANUARY, 23, 12, 0), refeicoes);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("JA EXISTE RESERVA PARA ESTA DATA"));

        assertEquals(1, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaDeQuartoComDataFinalAntesDaInicial() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataFinal, dataInicio, refeicoes);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("DATA FINAL DEVE SER POSTERIOR A DATA INICIAL"));

        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testCriarReservaComDataIgual() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataInicio, refeicoes);

        assertEquals(1, this.reservasSessionController.getReservas().size());
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertTrue(reservasSessionController.exibirReserva("CLI2", Long.parseLong("1")).contains("Período: 13/12/2023 14:00:00 ate 14/12/2023 12:00:00"));
    }

    @Test
    public void testReservaDeQuartoSemAntecedencia() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, LocalDateTime.now(), dataFinal, refeicoes);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NECESSARIO ANTECEDENCIA MINIMA DE 01 (UM) DIA"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaDeQuartoExcedeCapacidade() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("GER3", "CLI2", 130, 11, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });

        assertTrue(hce.getMessage().toUpperCase().contains("QUANTIDADE DE HOSPÉDES DEVE SER NO MÁXIMO 10"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaComZeroHospedes() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("GER3", "CLI2", 130, 0, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });

        assertTrue(hce.getMessage().toUpperCase().contains("QUANTIDADE DE HOSPÉDES DEVE SER NO MÍNIMO 1"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaComHospedesNegativos() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarQuartoFamily("GER3", "CLI2", 130, -3, dataInicio, dataFinal, refeicoes, new String[]{"Quarto acessível para gatos"});
        });

        assertTrue(hce.getMessage().toUpperCase().contains("QUANTIDADE DE HOSPÉDES DEVE SER NO MÍNIMO 1"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    //Testes de Reserva de Restaurant
    @Test
    public void testReservarRestaurante() {
        assertEquals(0, this.reservasSessionController.getReservas().size());

        this.reservasSessionController.reservarRestaurante("GER3", "CLI2", dataInicio, dataFinal, 13, "0");

        assertEquals(1, this.reservasSessionController.getReservas().size());
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));

    }

    @Test
    public void testVariasRevervasDeRestaurante() {
        assertEquals(0, this.reservasSessionController.getReservas().size());

        this.reservasSessionController.reservarRestaurante("GER3", "CLI2", dataInicio, dataFinal, 13, "0");
        this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", LocalDateTime.of(2024, Month.FEBRUARY, 22, 14, 0), LocalDateTime.of(2024, Month.FEBRUARY, 27, 14, 0), 31, "0");

        assertEquals(2, this.reservasSessionController.getReservas().size());
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("2")));

    }

    @Test
    public void testAdministradorNaoPodeGerarReservaDeRestaurante() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("ADM1", "CLI2", dataInicio, dataFinal, 13, "0");
        });

        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA RESERVA"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testClienteNaoPodeGerarReservaDeRestaurante() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("CLI2", "CLI2", dataInicio, dataFinal, 13, "0");
        });

        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA RESERVA"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testFuncionarioNaoPodeReservarRestaurante() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("GER3", "FUN4", dataInicio, dataFinal, 13, "0");
        });

        assertTrue(hce.getMessage().contains("USUARIO NAO E CLIENTE"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testGerenteNaoPodeReservarRestaurante() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "GER3", dataInicio, dataFinal, 13, "0");
        });

        assertTrue(hce.getMessage().contains("USUARIO NAO E CLIENTE"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testAdministradorNaoPodeReservarRestaurante() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "ADM1", dataInicio, dataFinal, 13, "0");
        });

        assertTrue(hce.getMessage().contains("USUARIO NAO E CLIENTE"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaRestauranteComIdNuloEVazio() {
        assertThrows(NullPointerException.class, () -> {
            this.reservasSessionController.reservarRestaurante(null, "CLI2", dataInicio, dataFinal, 13, "0");;
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.reservasSessionController.reservarRestaurante("", "CLI2", dataInicio, dataFinal, 13, "0");
        });

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservaRestauranteComIdClienteNuloEVazio() {
        assertThrows(NullPointerException.class, () -> {
            this.reservasSessionController.reservarRestaurante("GER3", null, dataInicio, dataFinal, 13, "0");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.reservasSessionController.reservarRestaurante("GER3", "", dataInicio, dataFinal, 13, "0");
        });

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarRestauranteIdNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("GER34", "CLI2", dataInicio, dataFinal, 13, "0");
        });
        assertTrue(hce.getMessage().contains("USUARIO NAO EXISTE"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarRestauranteIdClienteNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("GER3", "CLI25", dataInicio, dataFinal, 13, "0");
        });
        assertTrue(hce.getMessage().contains("USUARIO NAO EXISTE"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservaRestauranteExcedeCapacidade() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", dataInicio, dataFinal, 55, "0");
        });

        assertTrue(hce.getMessage().contains("CAPACIDADE EXCEDIDA"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaComZeroConvidados() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", dataInicio, dataFinal, 0, "0");
        });

        assertTrue(hce.getMessage().contains("VALOR INVÁLIDO"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaComConvidadosNegativos() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", dataInicio, dataFinal, -52, "0");
        });

        assertTrue(hce.getMessage().contains("VALOR INVÁLIDO"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaRestauranteSemAntecedencia() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", LocalDateTime.now(), dataFinal, 13, "0");
        });

        assertTrue(hce.getMessage().contains("NECESSARIO ANTECEDENCIA MINIMA DE 01 (UM) DIA"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaRestauranteComDataFinalAntesDaInicial() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", dataFinal, dataInicio, 13, "0");
        });

        assertTrue(hce.getMessage().contains("DATA FINAL DEVE SER POSTERIOR A DATA INICIAL"));
        assertEquals(0, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaRestauranteComMesmaData() {
        this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", dataInicio, dataFinal, 13, "0");

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", dataInicio, dataFinal, 13, "0");
        });

        assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));
        assertEquals(1, this.reservasSessionController.getReservas().size());

    }

    @Test
    public void testReservaRestauranteComDataOcupada() {
        this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", dataInicio, dataFinal, 13, "0");

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarRestaurante("FUN4", "CLI2", LocalDateTime.of(2023, Month.DECEMBER, 23, 14, 0), LocalDateTime.of(2024, Month.JANUARY, 23, 12, 0), 13, "0");
        });

        assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));
        assertEquals(1, this.reservasSessionController.getReservas().size());

    }

    //Testes de Reserva de Auditório
    @Test
    public void testGerenteCadastraReservaAuditorio() {
        this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 100);
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(1, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testFuncionarioCadastraReservaAuditorio() {
        this.reservasSessionController.reservarAuditorio("FUN4", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 100);
        assertTrue(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(1, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testAdministradorNaoPodeCadastrarReservaAuditorio() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("ADM1", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });
        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testClienteNaoPodeCadastrarReservaAuditorio() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("CLI2", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });
        assertTrue(hce.getMessage().contains("NAO E POSSIVEL PARA USUARIO"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testGerenteNaoPodeReservarAuditorio() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("FUN4", "GER3", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });
        assertTrue(hce.getMessage().contains("USUARIO NAO E CLIENTE"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testFuncionarioNaoPodeReservarAuditorio() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "FUN4", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });
        assertTrue(hce.getMessage().contains("USUARIO NAO E CLIENTE"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testAdministradorNaoPodeReservarAuditorio() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "ADM1", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });
        assertTrue(hce.getMessage().contains("USUARIO NAO E CLIENTE"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservaAuditorioComIdNuloEVazio() {
        assertThrows(NullPointerException.class, () -> {
            this.reservasSessionController.reservarAuditorio(null, "ADM1", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.reservasSessionController.reservarAuditorio("", "ADM1", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservaAuditorioComIdClienteNuloEVazio() {
        assertThrows(NullPointerException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", null, Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });

        assertThrows(IllegalArgumentException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioIdNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER34", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });
        assertTrue(hce.getMessage().contains("USUARIO NAO EXISTE"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioIdClienteNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI25", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });
        assertTrue(hce.getMessage().contains("USUARIO NAO EXISTE"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioCapacidadeExcedida() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 200);;
        });
        assertTrue(hce.getMessage().contains("CAPACIDADE EXCEDIDA"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioZeroPessoas() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 0);;
        });
        assertTrue(hce.getMessage().contains("VALOR INVÁLIDO"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioPessoasNegativas() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, -200);;
        });
        assertTrue(hce.getMessage().contains("VALOR INVÁLIDO"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioSemAntecedencia() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), LocalDateTime.now(), dataFinal, 100);;
        });
        assertTrue(hce.getMessage().contains("NECESSARIO ANTECEDENCIA MINIMA DE 01 (UM) DIA"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioComDataInvertida() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), dataFinal, dataInicio, 100);;
        });
        assertTrue(hce.getMessage().contains("DATA FINAL DEVE SER POSTERIOR A DATA INICIAL"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioComMesmaData() {
        this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 100);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 100);;
        });

        assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("2")));
        assertEquals(1, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testReservarAuditorioComDataOcupada() {
        this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), dataInicio, dataFinal, 100);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), LocalDateTime.of(2023, Month.DECEMBER, 23, 14, 0), dataFinal, 100);;
        });

        assertTrue(hce.getMessage().contains("JA EXISTE RESERVA PARA ESTA DATA"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("2")));
        assertEquals(1, this.reservasSessionController.getReservas().size());
    }

    @Test
    public void testNaoPodeReservarAuditorioIndisponivel() {
        this.areaComumController.alterarAreaComum("ADM1", Long.parseLong("1"), horaInicio, horaFinal, 100, 150, false);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), LocalDateTime.of(2023, Month.DECEMBER, 23, 14, 0), dataFinal, 100);;
        });

        assertTrue(hce.getMessage().contains("AUDITORIO INDISPONIVEL"));

        assertFalse(this.reservasSessionController.getReservas().containsKey(Long.parseLong("1")));
        assertEquals(0, this.reservasSessionController.getReservas().size());
    }

    //Testes de cancelar Reserva
    @Test
    public void testClienteCancelaReserva() {
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2", dataInicio, dataFinal, 13, "0");

        String resultado = this.reservasSessionController.cancelarReserva("CLI2", "1");
        assertAll(
                ()-> assertTrue(resultado.contains("[CANCELADA]"))
        );
    }

    @Test
    public void testClienteCancelaReservaNaoEncontrada() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.cancelarReserva("CLI2", "1");
        });

        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    @Test
    public void testClienteNaoExistenteCancelaReserva() {
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2", dataInicio, dataFinal, 13, "0");

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.cancelarReserva("CLI4", "1");
        });

        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }

    @Test
    public void testClienteNaoProprietarioCancelaReserva() {
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2", dataInicio, dataFinal, 13, "0");
        String usuarioTeste = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Paula Fernandes", "CLI", 121212));

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.cancelarReserva(usuarioTeste, "1");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("SOMENTE O PROPRIO CLIENTE PODERA CANCELAR A SUA RESERVA"));
    }

    @Test
    public void testFuncionarioCancelaReserva() {
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2", dataInicio, dataFinal, 13, "0");

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.cancelarReserva("GER3", "1");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO CANCELAR UMA RESERVA"));
    }

    //Testes de Exibir Reserva
    @Test
    void testExibirReservaCliente() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);

        String resultado = reservasSessionController.exibirReserva("CLI2", Long.parseLong("1"));
        assertAll(
                () -> assertTrue(resultado.contains("[1] Reserva de quarto em Favor de:")),
                () -> assertTrue(resultado.contains("- [CLI2] Taylor Swift (No. Doc. 131313)")),
                () -> assertTrue(resultado.contains("- [113] Quarto Single (custo basico: R$130,00; por pessoa: R$13,00 >>> R$143,00 diária)")),
                () -> assertTrue(resultado.contains("- Período: 13/12/2023 14:00:00 ate 13/01/2024 12:00:00")),
                () -> assertTrue(resultado.contains("- No. Hóspedes: 1 pessoa(s)")),
                () -> assertTrue(resultado.contains("[0] Cafe-da-manha: Café da manhã das secret sessions (06h13 as 09h13). Valor por pessoa: R$13,30. VIGENTE]")),
                () -> assertTrue(resultado.contains("Pedidos: (nenhum)")),
                () -> assertTrue(resultado.contains("VALOR TOTAL DA RESERVA: R$156,30 x31 (diarias) => R$4.845,30")),
                () -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: PENDENTE."))
        );
    }

    @Test
    void testExibirReservaGerente() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);

        String resultado = reservasSessionController.exibirReserva("GER3", Long.parseLong("1"));
        assertAll(
                () -> assertTrue(resultado.contains("[1] Reserva de quarto em Favor de:")),
                () -> assertTrue(resultado.contains("- [CLI2] Taylor Swift (No. Doc. 131313)")),
                () -> assertTrue(resultado.contains("- [113] Quarto Single (custo basico: R$130,00; por pessoa: R$13,00 >>> R$143,00 diária)")),
                () -> assertTrue(resultado.contains("- Período: 13/12/2023 14:00:00 ate 13/01/2024 12:00:00")), //adicional
                () -> assertTrue(resultado.contains("- No. Hóspedes: 1 pessoa(s)")), //cafe da manha
                () -> assertTrue(resultado.contains("[0] Cafe-da-manha: Café da manhã das secret sessions (06h13 as 09h13). Valor por pessoa: R$13,30. VIGENTE]")),
                () -> assertTrue(resultado.contains("Pedidos: (nenhum)")),
                () -> assertTrue(resultado.contains("VALOR TOTAL DA RESERVA: R$156,30 x31 (diarias) => R$4.845,30")),
                () -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: PENDENTE."))
        );
    }

    @Test
    void testExibirReservaFuincionario() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);

        String resultado = reservasSessionController.exibirReserva("FUN4", Long.parseLong("1"));
        assertAll(
                () -> assertTrue(resultado.contains("[1] Reserva de quarto em Favor de:")),
                () -> assertTrue(resultado.contains("- [CLI2] Taylor Swift (No. Doc. 131313)")),
                () -> assertTrue(resultado.contains("- [113] Quarto Single (custo basico: R$130,00; por pessoa: R$13,00 >>> R$143,00 diária)")),
                () -> assertTrue(resultado.contains("- Período: 13/12/2023 14:00:00 ate 13/01/2024 12:00:00")), //adicional
                () -> assertTrue(resultado.contains("- No. Hóspedes: 1 pessoa(s)")), //cafe da manha
                () -> assertTrue(resultado.contains("[0] Cafe-da-manha: Café da manhã das secret sessions (06h13 as 09h13). Valor por pessoa: R$13,30. VIGENTE]")),
                () -> assertTrue(resultado.contains("Pedidos: (nenhum)")),
                () -> assertTrue(resultado.contains("VALOR TOTAL DA RESERVA: R$156,30 x31 (diarias) => R$4.845,30")),
                () -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: PENDENTE."))
        );
    }

    @Test
    void testExibirReservaAdministrador() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.exibirReserva("ADM1", Long.valueOf(1));
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
        assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
    }

    @Test
    void testExibirReservaNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.exibirReserva("CLI2", Long.valueOf(13));
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    //Testes de Listar Reservas Cliente
    @Test
    void testListarReservasClienteProprioCliente() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2",  dataInicio, dataFinal, 45, "0");

        String[] resultado = reservasSessionController.listarReservasCliente("CLI2", "CLI2");
        assertAll(
                () -> assertEquals(2, resultado.length)
        );
    }

    @Test
    void testListarReservasClientePorGerente() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2",  dataInicio, dataFinal, 45, "0");

        String[] resultado = reservasSessionController.listarReservasCliente("GER3", "CLI2");
        assertAll(
                () -> assertEquals(2, resultado.length)
        );
    }

    @Test
    void testListarReservasClientePorFuncionario() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2",  dataInicio, dataFinal, 45, "0");

        String[] resultado = reservasSessionController.listarReservasCliente("FUN4", "CLI2");
        assertAll(
                () -> assertEquals(2, resultado.length)
        );
    }
    @Test
    void testListarReservasClientePorAdministrador() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasCliente("ADM1", "CLI2");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
        assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
    }

    @Test
    void testListarReservasClientePorOutroCliente() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2",  dataInicio, dataFinal, 45, "0");

        String[] resultado = reservasSessionController.listarReservasCliente("CLI5", "CLI2");
        assertAll(
                () -> assertEquals(2, resultado.length)
        );
    }
    @Test
    void testListarReservasUsuarioNaoCliente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasCliente("GER3", "FUN4");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E CLIENTE"));
    }

    @Test
    void testListarReservasClienteNaoExiste() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasCliente("CLI5", "CLI13");
        });
        System.out.println(hce);
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }
    @Test
    void testListarReservasUsuarioNaoTemReservas() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasCliente("GER3", "CLI5");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    //Testes de Listar Reservas Cliente Tipo

    @Test
    void testlistarReservasClienteTipoPorGerente() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        this.reservasSessionController.reservarQuartoDouble("GER3", "CLI2",  133, dataInicio, dataFinal,  refeicoes, new String[]{""});
        this.reservasSessionController.reservarQuartoFamily("GER3", "CLI5",  130, 10, dataInicio, dataFinal,  refeicoes, new String[]{""});

        String[] resultado = reservasSessionController.listarReservasClienteTipo("GER3", "CLI2", "QUARTO");
        assertAll(
                () -> assertEquals(2, resultado.length)
        );
    }

    @Test
    void testlistarReservasClienteTipoPorCliente() {
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2",  dataInicio, dataFinal, 45, "0");

        String[] resultado = reservasSessionController.listarReservasClienteTipo("CLI2", "CLI2", "RESTAURANTE");
        assertAll(
                () -> assertEquals(1, resultado.length)
        );
    }

    @Test
    void testlistarReservasClienteTipoPorFuncionario() {
        this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), LocalDateTime.of(2023, Month.DECEMBER, 23, 14, 0), dataFinal, 100);

        String[] resultado = reservasSessionController.listarReservasClienteTipo("FUN4", "CLI2", "AUDITORIO");
        assertAll(
                () -> assertEquals(1, resultado.length)
        );
    }

    @Test
    void testlistarReservasTipoUsuarioNaoCliente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasClienteTipo("GER3", "FUN4", "AUDITORIO");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E CLIENTE"));
    }

    @Test
    void testlistarReservasTipoUsuarioSemPermissao() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasClienteTipo("ADM1", "CLI2", "AUDITORIO");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
        assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
    }

    @Test
    void testlistarReservasClienteTipoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasClienteTipo("CLI2", "CLI2", "BEYONCE");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("TIPO INVÁLIDO DE RESERVA"));
    }

    @Test
    void testlistarReservasClienteInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasClienteTipo("CLI2", "CLI2", "QUARTO");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    //Testes de Listar Reservas Tipo

    @Test
    void testListarReservasTipoPorGerente() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        this.reservasSessionController.reservarQuartoDouble("GER3", "CLI2",  133, dataInicio, dataFinal,  refeicoes, new String[]{""});
        this.reservasSessionController.reservarQuartoFamily("GER3", "CLI5",  130, 10, dataInicio, dataFinal,  refeicoes, new String[]{""});

        String[] resultado = reservasSessionController.listarReservasTipo("GER3", "QUARTO");
        assertAll(
                () -> assertEquals(3, resultado.length)
        );
    }

    @Test
    void testListarReservasTipoPorCliente() {
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2",  dataInicio, dataFinal, 45, "0");
        this.reservasSessionController.reservarRestaurante("GER3", "CLI5",  dataFinal.plusDays(1), dataFinal.plusDays(3), 10, "0");

        String[] resultado = reservasSessionController.listarReservasTipo("CLI2", "RESTAURANTE");
        assertAll(
                () -> assertEquals(2, resultado.length)
        );
    }

    @Test
    void testlistarReservasTipoPorFuncionario() {
        this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), LocalDateTime.of(2023, Month.DECEMBER, 23, 14, 0), dataFinal, 100);

        String[] resultado = reservasSessionController.listarReservasTipo("FUN4",  "AUDITORIO");
        assertAll(
                () -> assertEquals(1, resultado.length)
        );
    }

    @Test
    void testlistarReservasTipoPorUsuarioSemPermissao() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasTipo("ADM1",  "AUDITORIO");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
        assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
    }
    @Test
    void testlistarReservasTipoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasTipo("CLI2", "BEYONCE");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("TIPO INVÁLIDO DE RESERVA"));
    }

    @Test
    void testlistarReservaInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasTipo("CLI2", "QUARTO");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    @Test
    void testListarReservasAtivasPorGerente() {
        this.reservasSessionController.reservarQuartoSingle("GER3", "CLI2", 113, dataInicio, dataFinal, refeicoes);
        this.reservasSessionController.reservarQuartoDouble("GER3", "CLI2",  133, dataInicio, dataFinal,  refeicoes, new String[]{""});
        this.reservasSessionController.reservarQuartoFamily("GER3", "CLI5",  130, 10, dataInicio, dataFinal,  refeicoes, new String[]{""});

        String[] resultado = reservasSessionController.listarReservasAtivas("GER3");
        assertAll(
                () -> assertEquals(3, resultado.length)
        );
    }

    @Test
    void testListarReservasAtivasPorCliente() {
        this.reservasSessionController.reservarRestaurante("GER3", "CLI2",  dataInicio, dataFinal, 45, "0");
        this.reservasSessionController.reservarRestaurante("GER3", "CLI5",  dataFinal.plusDays(1), dataFinal.plusDays(3), 10, "0");

        String[] resultado = reservasSessionController.listarReservasAtivas("CLI2");
        assertAll(
                () -> assertEquals(2, resultado.length)
        );
    }

    @Test
    void testListarReservasAtivasPorFuncionario() {
        this.reservasSessionController.reservarAuditorio("GER3", "CLI2", Long.parseLong("1"), LocalDateTime.of(2023, Month.DECEMBER, 23, 14, 0), dataFinal, 100);

        String[] resultado = reservasSessionController.listarReservasAtivas("FUN4");
        assertAll(
                () -> assertEquals(1, resultado.length)
        );
    }

    @Test
    void testListarReservasAtivasPorUsuarioSemPermissao() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasAtivas("ADM1");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO"));
        assertTrue(hce.getMessage().toUpperCase().contains("EXIBIR/LISTAR RESERVA(S) DO CLIENTE"));
    }

    @Test
    void testListarReservasAtivasNaoEncontrada() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.reservasSessionController.listarReservasAtivas("CLI2");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    private String extrairId(String input) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
