package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class RefeicaoControllerTest {

    private RefeicaoController refeicaoController;
    private String funcionarioId;
    private String clienteId;

    @BeforeEach
    void preparaRefeicaoController() {
        this.refeicaoController = RefeicaoController.getInstance();
        this.refeicaoController.init();

        UsuarioController usuarioController = UsuarioController.getInstance();
        usuarioController.init();

        this.funcionarioId = extrairId(usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        this.clienteId = extrairId(usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313));
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoCafeDaManha() {
        String resultado = this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "CAFE_DA_MANHA","Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true);
        String resultadoId = extrairId(resultado);
        assertAll(
                () -> {
                    assert resultadoId != null;
                    assertTrue(resultado.contains(resultadoId));
                },
                () -> assertTrue(resultado.contains("Cafe-da-manha")),
                () -> assertTrue(resultado.contains("Café da Secret Session")),
                () -> assertTrue(resultado.contains("(06h13 as 09h13)")),
                () -> assertTrue(resultado.contains("Valor por pessoa: R$13,00")),
                () -> assertTrue(resultado.contains("VIGENTE"))
        );
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoAlmoco() {
        String resultado = this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "ALMOCO", "Almoço da Secret Session", LocalTime.of(12, 13), LocalTime.of(14, 13), 50.0, false);
        String resultadoId = extrairId(resultado);
        assertAll(
                () -> {
                    assert resultadoId != null;
                    assertTrue(resultado.contains(resultadoId));
                },
                () -> assertTrue(resultado.contains("Almoco")),
                () -> assertTrue(resultado.contains("Almoço da Secret Session")),
                () -> assertTrue(resultado.contains("(12h13 as 14h13)")),
                () -> assertTrue(resultado.contains("Valor por pessoa: R$50,00")),
                () -> assertTrue(resultado.contains("INDISPONIVEL"))
        );
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoJantar() {
        String resultado = this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "JANTAR", "Jantar da Secret Session", LocalTime.of(19, 13), LocalTime.of(20, 13), 30.0, false);
        String resultadoId = extrairId(resultado);
        assertAll(
                () -> {
                    assert resultadoId != null;
                    assertTrue(resultado.contains(resultadoId));
                },
                () -> assertTrue(resultado.contains("Jantar")),
                () -> assertTrue(resultado.contains("Jantar da Secret Session")),
                () -> assertTrue(resultado.contains("(19h13 as 20h13)")),
                () -> assertTrue(resultado.contains("Valor por pessoa: R$30,00")),
                () -> assertTrue(resultado.contains("INDISPONIVEL"))
        );
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoComIdAutenticacaoNulo() {
        assertThrows(NullPointerException.class, () -> {
            this.refeicaoController.disponibilizarRefeicao(null, "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true);
        });
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoComIdAutenticacaoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.refeicaoController.disponibilizarRefeicao(" ", "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true);
        });
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoComIdInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.refeicaoController.disponibilizarRefeicao("ADM0", "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true);
        });
        assertEquals(hce.getMessage().toUpperCase(), "USUARIO NAO EXISTE");
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoPorNaoGerenteOuNaoFuncionario() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.refeicaoController.disponibilizarRefeicao(this.clienteId, "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true);
        });
        assertEquals(hce.getMessage().toUpperCase(), "NAO E POSSIVEL PARA USUARIO CADASTRAR UMA REFEICAO");
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoDataInicioPosteriorDataFim() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(9, 13), LocalTime.of(6, 13), 13.0, true);
        });
        assertEquals(hce.getMessage().toUpperCase(), "HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO");
    }

    @Test
    public void testDisponibilizacaoComTipoDeRefeicaoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "LANCHE", "Lanche da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true);
        });
        assertEquals(hce.getMessage().toUpperCase(), "TIPO DE REFEICAO INVALIDO");
    }

    @Test
    public void testDisponibilizacaoDeRefeicaoJaExistente() {
        this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "CAFE_DA_MANHA","Café da Secret Session",  LocalTime.of(5, 13), LocalTime.of(9, 13), 13.0, false);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true);
        });
        assertEquals(hce.getMessage().toUpperCase(), "REFEICAO JA EXISTE");
    }

    @Test
    public void testAlteracaoDeRefeicao() {
        String refeicaoId = extrairId(this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "CAFE_DA_MANHA", "Café da Secret Session",  LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true));
        String resultado = this.refeicaoController.alterarRefeicao(Long.valueOf(refeicaoId), LocalTime.of(5, 13), LocalTime.of(10, 13), 15.0, false);
        assertAll(
                () -> assertTrue(resultado.contains("Cafe-da-manha")),
                () -> assertTrue(resultado.contains("Café da Secret Session")),
                () -> assertTrue(resultado.contains("(05h13 as 10h13)")),
                () -> assertTrue(resultado.contains("Valor por pessoa: R$15,00")),
                () -> assertTrue(resultado.contains("INDISPONIVEL"))
        );
    }

    @Test
    public void testAlteracaoDeRefeicaoComIdInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.refeicaoController.alterarRefeicao(Long.valueOf(131313), LocalTime.of(5, 13), LocalTime.of(10, 13), 15.0, false);
        });
        assertEquals(hce.getMessage().toUpperCase(), "REFEICAO NAO EXISTE");
    }

    @Test
    public void testAlteracaoDeRefeicaoComDataInicioPosteriorDataFim() {
        String refeicaoId = extrairId(this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.refeicaoController.alterarRefeicao(Long.valueOf(refeicaoId), LocalTime.of(10, 13), LocalTime.of(5, 13), 15.0, false);
        });
        assertEquals(hce.getMessage().toUpperCase(), "HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO");
    }

    @Test
    public void testExibicaoDeRefeicao() {
        String refeicaoId = extrairId(this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true));
        String resultado = this.refeicaoController.exibirRefeicao(Long.valueOf(refeicaoId));
        assertAll(
                () -> assertTrue(resultado.contains(refeicaoId)),
                () -> assertTrue(resultado.contains("Cafe-da-manha")),
                () -> assertTrue(resultado.contains("Café da Secret Session")),
                () -> assertTrue(resultado.contains("(06h13 as 09h13)")),
                () -> assertTrue(resultado.contains("Valor por pessoa: R$13,00")),
                () -> assertTrue(resultado.contains("VIGENTE"))
        );
    }

    @Test
    public void testExibicaoDeRefeicaoComIdInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.refeicaoController.exibirRefeicao(Long.valueOf("131313"));
        });
        assertEquals(hce.getMessage().toUpperCase(), "REFEICAO NAO EXISTE");
    }

    @Test
    public void testListagemDeRefeicoes() {
        this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "CAFE_DA_MANHA", "Café da Secret Session", LocalTime.of(6, 13), LocalTime.of(9, 13), 13.0, true);
        this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "ALMOCO", "Almoço da Secret Session", LocalTime.of(12, 13), LocalTime.of(14, 13), 50.0, false);
        this.refeicaoController.disponibilizarRefeicao(this.funcionarioId, "JANTAR", "Jantar da Secret Session", LocalTime.of(19, 13), LocalTime.of(20, 13), 30.0, false);
        String[] resultados = this.refeicaoController.listarRefeicoes();
        Arrays.sort(resultados);
        assertAll(
                () -> assertEquals(3, resultados.length),
                () -> assertTrue(resultados[0].contains("Cafe-da-manha")),
                () -> assertTrue(resultados[0].contains("Café da Secret Session")),
                () -> assertTrue(resultados[0].contains("(06h13 as 09h13)")),
                () -> assertTrue(resultados[0].contains("Valor por pessoa: R$13,00")),
                () -> assertTrue(resultados[0].contains("VIGENTE")),
                () -> assertTrue(resultados[1].contains("Almoco")),
                () -> assertTrue(resultados[1].contains("Almoço da Secret Session")),
                () -> assertTrue(resultados[1].contains("(12h13 as 14h13)")),
                () -> assertTrue(resultados[1].contains("Valor por pessoa: R$50,00")),
                () -> assertTrue(resultados[1].contains("INDISPONIVEL")),
                () -> assertTrue(resultados[2].contains("Jantar")),
                () -> assertTrue(resultados[2].contains("Jantar da Secret Session")),
                () -> assertTrue(resultados[2].contains("(19h13 as 20h13)")),
                () -> assertTrue(resultados[2].contains("Valor por pessoa: R$30,00")),
                () -> assertTrue(resultados[2].contains("INDISPONIVEL"))
        );
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