package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoControllerTest {
    private PagamentoController pagamentoController;
    private ReservasSessionController reservasSessionController;
    private FormaDePagamentoController formaDePagamentoController;
   private UsuarioController usuarioController;
   private RefeicaoController refeicaoController;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String idAdministrador;
    private String idCliente;
    private String idGerente;
    private String reserva;
    private String refeicao;
    private String formaDePagamentoPix;
    private String formaDePagamentoCartao;
    private String formaDePagamentoDinheiro;

    @BeforeEach
    public void preparaPagamentoController() {
        this.pagamentoController = PagamentoController.getInstance();

        this.reservasSessionController = ReservasSessionController.getInstance();
        this.reservasSessionController.init();

        this.formaDePagamentoController = FormaDePagamentoController.getInstance();
        this.formaDePagamentoController.init();

        this.usuarioController = UsuarioController.getInstance();
        this.usuarioController.init();

        this.refeicaoController = RefeicaoController.getInstance();
        this.refeicaoController.init();

        this.dataInicio = LocalDateTime.of(2023, 11, 11, 13, 0, 0);
        this.dataFim = LocalDateTime.of(2023, 11, 13, 13, 0, 0);

        this.idAdministrador = extrairId(usuarioController.cadastrarUsuario("ADM1", "Ben 10", "ADM", 101010));
        this.idCliente = extrairId(usuarioController.cadastrarUsuario("ADM1", "Peppa Pig", "CLI", 151515));
        this.idGerente = extrairId(usuarioController.cadastrarUsuario("ADM1", "Dora Aventureira", "GER", 131313));

        this.refeicao = extrairId(refeicaoController.disponibilizarRefeicao(this.idGerente, "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13, true));
        this.reserva = extrairId(reservasSessionController.reservarRestaurante(idGerente, idCliente, dataInicio, dataFim, 13, refeicao));

        this.formaDePagamentoPix = extrairId(formaDePagamentoController.disponibilizarFormaDePagamento(idAdministrador, "PIX", 0.05));
        this.formaDePagamentoPix = extrairId(formaDePagamentoController.disponibilizarFormaDePagamento(idAdministrador, "CARTAO_DE_CREDITO", 0));
        this.formaDePagamentoPix = extrairId(formaDePagamentoController.disponibilizarFormaDePagamento(idAdministrador, "DINHEIRO", 0.1));
    }

    //Pagamento com dinheiro
    @Test
    void testRealizarPagamentoDinheiro() {
        String resultado = this.pagamentoController.realizarPagamentoDinheiro(this.idCliente, Long.parseLong(reserva), "Peppa Pig");
        String idResultado = extrairId(resultado);
        assertAll(
                () -> assertTrue(Integer.parseInt(idResultado)>0),
                () -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO")),
                () -> assertTrue(resultado.contains("Forma de pagamento: DINHEIRO (10% de desconto em pagamentos)")),
                () -> assertTrue(resultado.contains("Total Efetivamente Pago: R$456,30 em 1x de R$456,30"))
        );
    }

    @Test
    void testRealizarPagamentoDinheiroPorUsuarioNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoDinheiro("CLI0", Long.parseLong(reserva), "Peppa Pig");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }

    @Test
    void testRealizarPagamentoDinheiroReservaNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoDinheiro(this.idCliente, Long.parseLong("13"), "Peppa Pig");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    @Test
    void testRealizarPagamentoDinheiroPorUsuarioNaoCliente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoDinheiro(this.idGerente, Long.parseLong(reserva), "Peppa Pig");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E CLIENTE"));
    }

    @Test
    void testRealizarPagamentoDinheiroClienteNaoReservou() {
        String idClienteNovo = extrairId(usuarioController.cadastrarUsuario("ADM1", "Papai Pig", "CLI", 161616));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoDinheiro(idClienteNovo, Long.parseLong(reserva), "Peppa Pig");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("SOMENTE O PROPRIO CLIENTE PODERA PAGAR A SUA RESERVA"));
    }

    @Test
    void testRealizarPagamentoDinheiroDuplicado() {
        this.pagamentoController.realizarPagamentoDinheiro(idCliente, Long.parseLong(reserva), "Peppa Pig");

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoDinheiro(idCliente, Long.parseLong(reserva), "Peppa Pig");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA JA FOI PAGA"));
    }

    //Pagamento com Cartão de Crédito
    @Test
    void testRealizarPagamentoCartao() {
        String resultado = this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "013", 2);
        String idResultado = extrairId(resultado);
        assertAll(
                () -> assertTrue(Integer.parseInt(idResultado)>0),
                () -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO")),
                () -> assertTrue(resultado.contains("Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos)")),
                () -> assertTrue(resultado.contains("Total Efetivamente Pago: R$507,00 em 2x de R$253,50"))
        );
    }
    @Test
    void testRealizarPagamentoCartaoPorUsuarioNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao("CLI0", Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }

    @Test
    void testRealizarPagamentoCartaoReservaNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong("13"), "Peppa Pig", "1234987643216789", "04/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    @Test
    void testRealizarPagamentoCartaoPorUsuarioNaoCliente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idGerente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E CLIENTE"));
    }

    @Test
    void testRealizarPagamentoCartaoClienteNaoReservou() {
        String idClienteNovo = extrairId(usuarioController.cadastrarUsuario("ADM1", "Papai Pig", "CLI", 161616));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(idClienteNovo, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("SOMENTE O PROPRIO CLIENTE PODERA PAGAR A SUA RESERVA"));
    }

    @Test
    void testRealizarPagamentoCartaoDuplicado() {
        this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "013", 2);

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA JA FOI PAGA"));
    }

    @Test
    void testRealizarPagamentoCartaoNumeroCartaoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "", "04/2024", "013", 2);
        });
    }

    @Test
    void testRealizarPagamentoCartaoNomeTitularNulo() {
        assertThrows(NullPointerException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", null, "04/2024", "013", 2);
        });
    }

    @Test
    void testRealizarPagamentoCartaoDataValidadeVazia() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "", "013", 2);
        });
    }

    @Test
    void testRealizarPagamentoCartaoDataValidadeNula() {
        assertThrows(NullPointerException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", null, "013", 2);
        });
    }

    @Test
    void testRealizarPagamentoCartaoDigitoVerificadorVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "", 2);
        });
    }

    @Test
    void testRealizarPagamentoCartaoDigitoVerificadorNulo() {
        assertThrows(NullPointerException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", null, 2);
        });
    }

    @Test
    void testRealizarPagamentoCartaoNumero15Digitos() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "123498764321678", "04/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NUMERO DE CARTAO INVALIDO"));
    }
    @Test
    void testRealizarPagamentoCartaoNumero17Digitos() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "12349876432167891", "04/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NUMERO DE CARTAO INVALIDO"));
    }

    @Test
    void testRealizarPagamentoCartaoDigitoVerificador2Digitos() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "13", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("DIGITO VERIFICADOR INVALIDO"));
    }
    @Test
    void testRealizarPagamentoCartaoDigitoVerificador4Digitos() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "0130", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("DIGITO VERIFICADOR INVALIDO"));
    }

    @Test
    void testRealizarPagamentoCartaoVencido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2023", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("CARTAO VENCIDO"));
    }

    @Test
    void testRealizarPagamentoCartaoDataInvalida() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/04/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("DATA INVALIDA"));
    }

    @Test
    void testRealizarPagamentoCartaoDataInvalida1() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "13/2024", "013", 2);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("DATA INVALIDA"));
    }

    @Test
    void testRealizarPagamentoCartaoArredondamento() {
        String resultado = this.pagamentoController.realizarPagamentoCartao(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1234987643216789", "04/2024", "013", 9);
        String idResultado = extrairId(resultado);
        assertAll(
                () -> assertTrue(Integer.parseInt(idResultado)>0),
                () -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO")),
                () -> assertTrue(resultado.contains("Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos)")),
                () -> assertTrue(resultado.contains("Total Efetivamente Pago: R$507,00 em 9x de R$56,34"))
        );
    }

    //Pagamento com Pix
    @Test
    void testRealizarPagamentoPix() {
        String resultado = this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "12398745613", "Townsville Bank");
        String idResultado = extrairId(resultado);
        assertAll(
                () -> assertTrue(Integer.parseInt(idResultado)>0),
                () -> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO")),
                () -> assertTrue(resultado.contains("Forma de pagamento: PIX (5% de desconto em pagamentos)")),
                () -> assertTrue(resultado.contains("Total Efetivamente Pago: R$481,65 em 1x de R$481,65"))
        );
    }

    @Test
    void testRealizarPagamentoPixPorUsuarioNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoPix("CLI0", Long.parseLong(reserva), "Peppa Pig", "12398745613", "Townsville Bank");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }

    @Test
    void testRealizarPagamentoPixReservaNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong("13"), "Peppa Pig", "12398745613", "Townsville Bank");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA NAO ENCONTRADA"));
    }

    @Test
    void testRealizarPagamentoPixPorUsuarioNaoCliente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idGerente, Long.parseLong(reserva), "Peppa Pig", "12398745613", "Townsville Bank");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E CLIENTE"));
    }

    @Test
    void testRealizarPagamentoPixClienteNaoReservou() {
        String idClienteNovo = extrairId(usuarioController.cadastrarUsuario("ADM1", "Papai Pig", "CLI", 161616));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(idClienteNovo, Long.parseLong(reserva), "Peppa Pig", "12398745613", "Townsville Bank");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("SOMENTE O PROPRIO CLIENTE PODERA PAGAR A SUA RESERVA"));
    }

    @Test
    void testRealizarPagamentoPixDuplicado() {
        this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "12398745613", "Townsville Bank");

        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "12398745613", "Townsville Bank");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("RESERVA JA FOI PAGA"));
    }

    @Test
    void testRealizarPagamentoPixCpfVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "", "Townsville Bank");
        });
    }

    @Test
    void testRealizarPagamentoPixCpfNulo() {
        assertThrows(NullPointerException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", null, "Townsville Bank");
        });
    }

    @Test
    void testRealizarPagamentoPixBancoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "12398745613", " ");
        });
    }

    @Test
    void testRealizarPagamentoPixBancoNulo() {
        assertThrows(NullPointerException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "12398745613", null);
        });
    }

    @Test
    void testRealizarPagamentoPixCpf12Digitos() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "123987456130", "Townsville Bank");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("CPF INVALIDO"));
    }

    @Test
    void testRealizarPagamentoPixCpf10Digitos() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.pagamentoController.realizarPagamentoPix(this.idCliente, Long.parseLong(reserva), "Peppa Pig", "1239874561", "Townsville Bank");
        });
        assertTrue(hce.getMessage().toUpperCase().contains("CPF INVALIDO"));
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