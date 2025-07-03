package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.Quarto;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.QuartoSingle;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class QuartoControllerTest {

    private QuartoController quartoController;
    private String idAdministrador;
    private String idGerente;
    private String idCliente;

    @BeforeEach
    void preparaQuartoController() {
        this.quartoController = QuartoController.getInstance();
        this.quartoController.init();

        UsuarioController usuarioController = UsuarioController.getInstance();
        usuarioController.init();

        this.idAdministrador = extrairId(usuarioController.cadastrarUsuario("ADM1", "Gal Costa", "ADM", 101010));
        this.idGerente = extrairId(usuarioController.cadastrarUsuario("ADM1", "Seu Pereira", "GER", 292929));
        this.idCliente = extrairId(usuarioController.cadastrarUsuario("ADM1", "Rosalía", "CLI", 383838));
    }


    @Test
    void testQuartoSingleDisponibilizadoPorAdministrador() {
        String resultado = this.quartoController.disponibilizarQuartoSingle(this.idAdministrador, 101, 120.0, 80.0);
        String idResultado = extrairId(resultado);
        assertAll(
                () -> assertTrue(Integer.parseInt(idResultado)>0),
                () -> assertTrue(resultado.contains("Quarto Single")),
                () -> assertTrue(resultado.contains("R$120,00")), // Valor Base Quarto
                () -> assertTrue(resultado.contains("R$80,00")) // Valor Pessoa
        );
    }
    @Test
    void testQuartoSingleDisponibilizadoPorGerente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoSingle(this.idGerente, 101, 120.0, 80.0);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E ADMINISTRADOR"));
    }

    @Test
    void testQuartoSingleDisponibilizadoPorAdministradorNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoSingle("ADM0", 101, 120.0, 80.0);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }

    @Test
    void testQuartoSingleDisponibilizadoJaExiste() {
        this.quartoController.disponibilizarQuartoSingle(this.idAdministrador, 101, 120.0, 50.0);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoSingle(this.idAdministrador, 101, 80.0, 20.0);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("QUARTO JA EXISTE"));
    }

    @Test
    void testQuartoDoubleDisponibilizadoPorAdministrador() {
        String resultado = this.quartoController.disponibilizarQuartoDouble(this.idAdministrador, 102, 140.0, 80.0, null);
        String idResultado = extrairId(resultado);
        assertAll(
                () -> assertTrue(Integer.parseInt(idResultado)>0),
                () -> assertTrue(resultado.contains("Quarto Double")),
                () -> assertTrue(resultado.contains("R$140,00")), // Valor Base Quarto
                () -> assertTrue(resultado.contains("R$80,00")) // Valor Pessoa
        );
    }

    @Test
    void testQuartoDoubleDisponibilizadoPorGerente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoDouble(this.idGerente, 102, 140.0, 80.0, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E ADMINISTRADOR"));
    }

    @Test
    void testQuartoDoubleDisponibilizadoPorAdministradorNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoDouble("ADM0", 102, 140.0, 80.0, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }

    @Test
    void testQuartoDoubleDisponibilizadoJaExiste() {
        this.quartoController.disponibilizarQuartoDouble(this.idAdministrador, 102, 180.0, 50.0, null);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoDouble(this.idAdministrador, 102, 140.0, 80.0, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("QUARTO JA EXISTE"));
    }

    @Test
    void testQuartoFamilyDisponibilizadoPorAdministrador() {
        String resultado = this.quartoController.disponibilizarQuartoFamily(this.idAdministrador, 103, 8,140.0, 80.0, null);
        String idResultado = extrairId(resultado);

        assertAll(
                () -> assertTrue(Integer.parseInt(idResultado)>0),
                () -> assertTrue(resultado.contains("Quarto Family")),
                () -> assertTrue(resultado.contains("R$140,00")), // Valor Base Quarto
                () -> assertTrue(resultado.contains("R$80,00")) // Valor Pessoa
        );
    }

    @Test
    void testQuartoFamilyDisponibilizadoPorGerente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoFamily(this.idGerente, 103, 8,140.0, 80.0, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO E ADMINISTRADOR"));
    }

    @Test
    void testQuartoFamilyDisponibilizadoPorAdministradorNaoExistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoFamily("AMD0", 103, 8,140.0, 80.0, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }

    @Test
    void testQuartoFamilyDisponibilizadoJaExiste() {
        this.quartoController.disponibilizarQuartoFamily(this.idAdministrador, 103, 8,120.0, 50.0, null);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.disponibilizarQuartoFamily(this.idAdministrador, 103, 9,140.0, 80.0, null);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("QUARTO JA EXISTE"));
    }

    @Test
    void testExibirQuartoSingle() {
        this.quartoController.disponibilizarQuartoSingle(this.idAdministrador, 101, 120.0, 50.0);
        assertEquals("[101] Quarto Single (custo basico: R$120,00; por pessoa: R$50,00 >>> R$170,00 diária)", this.quartoController.exibirQuarto(101));
    }

    @Test
    void testExibirQuartoDouble() {
        this.quartoController.disponibilizarQuartoDouble(this.idAdministrador, 102, 120.0, 50.0, null);
        assertEquals("[102] Quarto Double (custo basico: R$120,00; por pessoa: R$50,00 >>> R$220,00 diária). Pedidos: (nenhum)", this.quartoController.exibirQuarto(102));
    }
    @Test
    void testExibirQuartoFamily() {
        this.quartoController.disponibilizarQuartoFamily(this.idAdministrador, 103, 8,120.0, 50.0, null);
        assertEquals("[103] Quarto Family (custo basico: R$120,00; por pessoa: R$50,00 >>> R$520,00 diária). Capacidade: 08 pessoa(s). Pedidos: (nenhum)", this.quartoController.exibirQuarto(103));
    }
    @Test
    void testExibirQuartoComNumeroInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.quartoController.exibirQuarto(101);
        });
        assertTrue(hce.getMessage().toUpperCase().contains("NÚMERO DE QUARTO INVÁLIDO"));
    }
    @Test
    void listarQuartos() {
        String quartoSingle = this.quartoController.disponibilizarQuartoSingle(this.idAdministrador, 101, 120.0, 50.0);
        String quartoDouble = this.quartoController.disponibilizarQuartoDouble(this.idAdministrador, 102, 140.0, 60.0, new String[]{"Disponibilizar rede na varanda", "Televisão com acesso à Netflix"});
        String quartoFamily = this.quartoController.disponibilizarQuartoFamily(this.idAdministrador, 103, 8,180.0, 80.0, new String[]{"Trocar a cuscuzeira para uma maior"});

        String[] resultado = this.quartoController.listarQuartos();

        assertAll(
                () -> assertEquals(3, resultado.length),
                ()-> assertTrue(resultado[0].contains(quartoSingle)),
                ()-> assertTrue(resultado[1].contains(quartoDouble)),
                ()-> assertTrue(resultado[2].contains(quartoFamily))
        );
    }

    @Test
    void verificarQuartoExistenteTrue() {
        this.quartoController.disponibilizarQuartoSingle(this.idAdministrador, 101, 120.0, 50.0);
        assertTrue(this.quartoController.verificarQuartoExistente(101));
    }

    @Test
    void verificarQuartoExistenteFalse() {
        assertFalse(this.quartoController.verificarQuartoExistente(101));
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