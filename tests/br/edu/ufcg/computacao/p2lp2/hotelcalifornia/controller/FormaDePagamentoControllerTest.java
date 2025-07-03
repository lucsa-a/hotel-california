package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class FormaDePagamentoControllerTest {

    private FormaDePagamentoController formaDePagamentoController;
    private String clienteId;

    @BeforeEach
    void preparaFormaDePagamentoController() {
        this.formaDePagamentoController = FormaDePagamentoController.getInstance();
        this.formaDePagamentoController.init();

        UsuarioController usuarioController = UsuarioController.getInstance();
        usuarioController.init();
        this.clienteId = extrairId(usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313));
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoCartaoDeCredito() {
        String resultado = this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0);
        String resultadoId = extrairId(resultado);
        assertAll(
                () -> {
                    assert resultadoId != null;
                    assertTrue(resultado.contains(resultadoId));
                },
                () -> assertTrue(resultado.contains("Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos)"))
        );
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoPix() {
        String resultado = this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "PIX", 0.05);
        String resultadoId = extrairId(resultado);
        assertAll(
                () -> {
                    assert resultadoId != null;
                    assertTrue(resultado.contains(resultadoId));
                },
                () -> assertTrue(resultado.contains("Forma de pagamento: PIX (5% de desconto em pagamentos)"))
        );
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoDinheiro() {
        String resultado = this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "DINHEIRO", 0.05);
        String resultadoId = extrairId(resultado);
        assertAll(
                () -> {
                    assert resultadoId != null;
                    assertTrue(resultado.contains(resultadoId));
                },
                () -> assertTrue(resultado.contains("Forma de pagamento: DINHEIRO (5% de desconto em pagamentos)"))
        );
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoComIdAutenticacaoNulo() {
        assertThrows(NullPointerException.class, () -> {
            this.formaDePagamentoController.disponibilizarFormaDePagamento(null, "DINHEIRO", 0.0);
        });
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoComIdAutenticacaoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.formaDePagamentoController.disponibilizarFormaDePagamento(" ", "DINHEIRO", 0.0);
        });
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoComIdAutenticacaoInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM0", "DINHEIRO", 0.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "USUARIO NAO EXISTE");
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoPorNaoAdministrador() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.disponibilizarFormaDePagamento(this.clienteId, "DINHEIRO", 0.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "NAO E POSSIVEL PARA USUARIO CADASTRAR UMA FORMA DE PAGAMENTO");
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoInvalida() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CHEQUE", 0.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "TIPO DE FORMA DE PAGAMENTO INVALIDO");
    }

    @Test
    public void testDisponibilizacaoDeFormaDePagamentoJaExistente() {
        this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.05);
        });
        assertEquals(hce.getMessage().toUpperCase(), "FORMA DE PAGAMENTO JA EXISTE");
    }

    @Test
    public void testAlteracaoDeFormaDePagamento() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        String resultado = this.formaDePagamentoController.alterarFormaDePagamento("ADM1", Integer.parseInt(formaDePagamentoId), "CARTAO_DE_CREDITO", 0.05);
        String resultadoId = extrairId(resultado);
        assertAll(
                () -> {
                    assert resultadoId != null;
                    assertTrue(resultado.contains(resultadoId));
                },
                () -> assertTrue(resultado.contains("Forma de pagamento: CARTAO DE CREDITO (5% de desconto em pagamentos)"))
        );
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoComIdInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento("ADM1", 1313, "CARTAO_DE_CREDITO", 0.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "FORMA DE PAGAMENTO NAO EXISTE");
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoNula() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        assertThrows(NullPointerException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento("ADM1", Integer.parseInt(formaDePagamentoId), null, 0.0);
        });
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoVazia() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        assertThrows(IllegalArgumentException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento("ADM1", Integer.parseInt(formaDePagamentoId), " ", 0.0);
        });
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoInvalida() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento("ADM1", Integer.parseInt(formaDePagamentoId), "CHEQUE", 0.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "TIPO DE FORMA DE PAGAMENTO INVALIDO");
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoNaoDisponibilizado() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento("ADM1", Integer.parseInt(formaDePagamentoId), "PIX", 0.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "FORMA DE PAGAMENTO NAO DISPONIBILIZADO");
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoComIdAutenticacaoNulo() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        assertThrows(NullPointerException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento(null, Integer.parseInt(formaDePagamentoId), "CARTAO_DE_CREDITO", 0.0);
        });
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoComIdAutenticacaoVazio() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        assertThrows(IllegalArgumentException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento(" ", Integer.parseInt(formaDePagamentoId), "CARTAO_DE_CREDITO", 0.0);
        });
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoComIdAutenticacaoInexistente() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento("ADM0", Integer.parseInt(formaDePagamentoId), "CARTAO_DE_CREDITO", 0.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "USUARIO NAO EXISTE");
    }

    @Test
    public void testAlteracaoDeFormaDePagamentoPorNaoAdministrador() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.alterarFormaDePagamento(this.clienteId, Integer.parseInt(formaDePagamentoId), "CARTAO_DE_CREDITO", 0.0);
        });
        assertEquals(hce.getMessage().toUpperCase(), "NAO E POSSIVEL PARA USUARIO CADASTRAR UMA FORMA DE PAGAMENTO");
    }

    @Test
    public void testExibicaoDeFormaDePagamento() {
        String formaDePagamentoId = extrairId(this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0));
        String resultado = this.formaDePagamentoController.exibirFormaPagamento(Integer.parseInt(formaDePagamentoId));
        assertTrue(resultado.contains("Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos)"));
    }

    @Test
    public void testExibicaoDeFormaDePagamentoComIdInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.formaDePagamentoController.exibirFormaPagamento(1313);
        });
        assertEquals(hce.getMessage().toUpperCase(), "FORMA DE PAGAMENTO NAO EXISTE");
    }

    @Test
    public void testListagemDeUsuarios() {
        this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "CARTAO_DE_CREDITO", 0.0);
        this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "PIX", 0.05);
        this.formaDePagamentoController.disponibilizarFormaDePagamento("ADM1", "DINHEIRO", 0.05);
        String[] resultados = this.formaDePagamentoController.listarFormasPagamentos();
        Arrays.sort(resultados);
        assertAll(
                () -> assertTrue(resultados[0].contains("Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos)")),
                () -> assertTrue(resultados[1].contains("Forma de pagamento: PIX (5% de desconto em pagamentos)")),
                () -> assertTrue(resultados[2].contains("Forma de pagamento: DINHEIRO (5% de desconto em pagamentos)"))
        );
    }

    @Test
    private String extrairId(String input) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
