package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioControllerTest {

    private UsuarioController usuarioController;

    @BeforeEach
    void preparaUsuarioController() {
        this.usuarioController = UsuarioController.getInstance();
        this.usuarioController.init();
    }

    @Test
    public void testCadastroDeAdministradorPadrao() {
        String resultado = this.usuarioController.getUsuario("ADM1").toString();
        assertAll(
                () -> assertTrue(resultado.contains("ADM")),
                () -> assertTrue(resultado.contains("Joao Costa")),
                () -> assertTrue(resultado.contains("123456"))
        );
    }

    @Test
    public void testCadastroDeAdministrador() {
        String resultado = this.usuarioController.cadastrarUsuario("ADM1", "Luiz Gonzaga", "ADM", 999999);
        assertAll(
                () -> assertTrue(resultado.contains("ADM")),
                () -> assertTrue(resultado.contains("Luiz Gonzaga")),
                () -> assertTrue(resultado.contains("999999"))
        );
    }

    @Test
    public void testCadastroDeGerente() {
        String resultado = this.usuarioController.cadastrarUsuario("ADM1", "Alceu Valença", "GER", 888888);
        assertAll(
                () -> assertTrue(resultado.contains("GER")),
                () -> assertTrue(resultado.contains("Alceu Valença")),
                () -> assertTrue(resultado.contains("888888"))
        );
    }

    @Test
    public void testCadastroDeOutroGerente() {
        this.usuarioController.cadastrarUsuario("ADM1", "Alceu Valença", "GER", 888888);
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.cadastrarUsuario("ADM1", "Valença Alceu", "GER", 111111);
        });
        assertEquals(hce.getMessage().toUpperCase(), "SO DEVE HAVER UM GERENTE NO HOTEL");
    }

    @Test
    public void testCadastroDeFuncionarioPorAdministrador() {
        String resultado = this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777);
        assertAll(
                () -> assertTrue(resultado.contains("FUN")),
                () -> assertTrue(resultado.contains("Raul Seixas")),
                () -> assertTrue(resultado.contains("777777"))
        );
    }

    @Test
    public void testCadastroDeFuncionarioPorGerente() {
        String gerenteId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Alceu Valença", "GER", 888888));
        String resultado = this.usuarioController.cadastrarUsuario(gerenteId, "Raul Seixas", "FUN", 777777);
        assertAll(
                () -> assertTrue(resultado.contains("FUN")),
                () -> assertTrue(resultado.contains("Raul Seixas")),
                () -> assertTrue(resultado.contains("777777"))
        );
    }

    @Test
    public void testCadastroNegadoPorFuncionario() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.cadastrarUsuario(funcionarioId, "Raul Seixas", "FUN", 777777);
        });
        assertEquals(hce.getMessage().toUpperCase(), "NAO E POSSIVEL PARA USUARIO " + funcionarioId + " CADASTRAR UM NOVO USUARIO DO TIPO FUN");
    }

    @Test
    public void testCadastroDeClientePorAdministrador() {
        String resultado = this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313);
        assertAll(
                () -> assertTrue(resultado.contains("CLI")),
                () -> assertTrue(resultado.contains("Taylor Swift")),
                () -> assertTrue(resultado.contains("131313"))
        );
    }

    @Test
    public void testCadastroDeClientePorGerente() {
        String gerenteId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Alceu Valença", "GER", 888888));
        String resultado = this.usuarioController.cadastrarUsuario(gerenteId, "Taylor Swift", "CLI", 131313);
        assertAll(
                () -> assertTrue(resultado.contains("CLI")),
                () -> assertTrue(resultado.contains("Taylor Swift")),
                () -> assertTrue(resultado.contains("131313"))
        );
    }

    @Test
    public void testCadastroDeClientePorFuncionario() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        String resultado = this.usuarioController.cadastrarUsuario(funcionarioId, "Taylor Swift", "CLI", 131313);
        assertAll(
                () -> assertTrue(resultado.contains("CLI")),
                () -> assertTrue(resultado.contains("Taylor Swift")),
                () -> assertTrue(resultado.contains("131313"))
        );
    }

    @Test
    public void testCadastroNegadoPorCliente() {
        String clienteId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.cadastrarUsuario(clienteId, "Swift Taylor", "CLI", 131313);
        });
        assertEquals(hce.getMessage().toUpperCase(), "NAO E POSSIVEL PARA USUARIO " + clienteId + " CADASTRAR UM NOVO USUARIO DO TIPO CLI");
    }

    @Test
    public void testCadastroDeUsuarioComIdAutenticacaoNulo() {
       assertThrows(NullPointerException.class, () -> {
           this.usuarioController.cadastrarUsuario(null, "Luiz Gonzaga", "ADM", 999999);
       });
    }

    @Test
    public void testCadastroDeUsuarioComIdAutenticacaoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.usuarioController.cadastrarUsuario(" ", "Luiz Gonzaga", "ADM", 999999);
        });
    }

    @Test
    public void testCadastroDeUsuarioComIdAutenticacaoInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.cadastrarUsuario("ADM0", "Luiz Gonzaga", "ADM", 999999);
        });
        assertEquals(hce.getMessage().toUpperCase(), "USUARIO NAO EXISTE");
    }

    @Test
    public void testCadastroDeUsuarioIdAutenticacaoComTipoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.cadastrarUsuario("ADM1", "Luiz Gonzaga", "EMP", 999999);
        });
        assertEquals(hce.getMessage().toUpperCase(), "TIPO INVÁLIDO");
    }

     @Test
    public void testAtualizacaoDeUsuario() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        String resultado = this.usuarioController.atualizarUsuario("ADM1", funcionarioId, "ADM");
        assertTrue(resultado.contains("ADM"));
     }

    @Test
    public void testAtualizacaoDeGerente() {
        String gerenteId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Alceu Valença", "GER", 888888));
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        String resultado = this.usuarioController.atualizarUsuario("ADM1", funcionarioId, "GER");
        assertAll(
                () -> assertTrue(resultado.contains("GER")),
                () -> assertNotEquals(gerenteId, extrairId(resultado))
        );
    }

    @Test
    public void testAtualizacaoDeUsuarioPorNaoAdministrador() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        String clienteId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.atualizarUsuario(clienteId, funcionarioId, "ADM");
        });
        assertEquals(hce.getMessage().toUpperCase(), "APENAS O ADMINISTRADOR PODE ATUALIZAR OS USUARIOS");
    }

    @Test
    public void testAtualizacaoDeCliente() {
        String clienteId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.atualizarUsuario("ADM1", clienteId, "ADM");
        });
        assertEquals(hce.getMessage().toUpperCase(), "CLIENTE NÃO PODE TER SEU TIPO ATUALIZADO");
    }

    @Test
    public void testAtualizacaoDeUsuarioComIdAutenticacaoNulo() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        assertThrows(NullPointerException.class, () -> {
            this.usuarioController.atualizarUsuario(null, funcionarioId, "ADM");
         });
    }

    @Test
    public void testAtualizacaoDeUsuarioComIdUsuarioNulo() {
        assertThrows(NullPointerException.class, () -> {
            this.usuarioController.atualizarUsuario("ADM1", null, "ADM");
        });
    }

    @Test
    public void testAtualizacaoDeUsuarioComTipoUsuarioNulo() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        assertThrows(NullPointerException.class, () -> {
            this.usuarioController.atualizarUsuario("ADM1", funcionarioId, null);
        });
    }

    @Test
    public void testAtualizacaoDeUsuarioComIdAutenticacaoVazio() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        assertThrows(IllegalArgumentException.class, () -> {
            this.usuarioController.atualizarUsuario(" ", funcionarioId, "ADM");
        });
    }

    @Test
    public void testAtualizacaoDeUsuarioComIdUsuarioVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.usuarioController.atualizarUsuario("ADM1", " ", "ADM");
        });
    }

    @Test
    public void testAtualizacaoDeUsuarioComTipoUsuarioVazio() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        assertThrows(IllegalArgumentException.class, () -> {
            this.usuarioController.atualizarUsuario("ADM1", funcionarioId, " ");
        });
    }

    @Test
    public void testAtualizacaoDeUsuarioComTipoInvalido() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.atualizarUsuario("ADM1", "Luiz Gonzaga", "EMP");
        });
        assertEquals(hce.getMessage().toUpperCase(), "USUARIO NAO EXISTE");
    }

    @Test
    public void testAtualizacaoDeUsuarioComIdAutenticacaoInexistente() {
        String funcionarioId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777));
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.atualizarUsuario("ADM0", funcionarioId, "ADM");
        });
        assertEquals(hce.getMessage().toUpperCase(), "USUARIO NAO EXISTE");
    }

    @Test
    public void testAtualizacaoDeUsuarioComIdUsuarioInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.atualizarUsuario("ADM0", "FUN0", "ADM");
        });
        assertEquals(hce.getMessage().toUpperCase(), "USUARIO NAO EXISTE");
    }

    @Test
    public void testExibicaoDeUsuario() {
        String administradorId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Luiz Gonzaga", "ADM", 999999));
        String resultado = this.usuarioController.exibirUsuario(administradorId);
                assertAll(
                () -> assertTrue(resultado.contains("ADM")),
                () -> assertTrue(resultado.contains("Luiz Gonzaga")),
                () -> assertTrue(resultado.contains("999999"))
        );
    }

    @Test
    public void testExibicaoDeUsuarioComIdInexistente() {
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class, () -> {
            this.usuarioController.exibirUsuario("ADM0");
        });
        assertEquals(hce.getMessage().toUpperCase(), "ID DO USUÁRIO NÃO EXISTE");
    }

    @Test
    public void testListagemDeUsuarios() {
        this.usuarioController.cadastrarUsuario("ADM1", "Luiz Gonzaga", "ADM", 999999);
        this.usuarioController.cadastrarUsuario("ADM1", "Alceu Valença", "GER", 888888);
        this.usuarioController.cadastrarUsuario("ADM1", "Raul Seixas", "FUN", 777777);
        this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313);
        String[] resultados = this.usuarioController.listarUsuarios();
        Arrays.sort(resultados);
        assertAll(
                () -> assertEquals(5, resultados.length),
                () -> assertTrue(resultados[0].contains("ADM")),
                () -> assertTrue(resultados[0].contains("Joao Costa")),
                () -> assertTrue(resultados[0].contains("123456")),
                () -> assertTrue(resultados[1].contains("ADM")),
                () -> assertTrue(resultados[1].contains("Luiz Gonzaga")),
                () -> assertTrue(resultados[1].contains("999999")),
                () -> assertTrue(resultados[2].contains("CLI")),
                () -> assertTrue(resultados[2].contains("Taylor Swift")),
                () -> assertTrue(resultados[2].contains("131313")),
                () -> assertTrue(resultados[3].contains("FUN")),
                () -> assertTrue(resultados[3].contains("Raul Seixas")),
                () -> assertTrue(resultados[3].contains("777777")),
                () -> assertTrue(resultados[4].contains("GER")),
                () -> assertTrue(resultados[4].contains("Alceu Valença")),
                () -> assertTrue(resultados[4].contains("888888"))
        );
    }

    @Test
    public void verificacaoDeIdValido() {
        String clienteId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313));
        assertTrue(this.usuarioController.verificarIdValido("ADM1"));
        assertTrue(this.usuarioController.verificarIdValido(clienteId));
        assertFalse(this.usuarioController.verificarIdValido("GER1"));
    }

    @Test
    public void verificacaoDeTipoUsuario() {
        String clienteId = extrairId(this.usuarioController.cadastrarUsuario("ADM1", "Taylor Swift", "CLI", 131313));
        assertTrue(this.usuarioController.verificarTipoUsuario("ADM1", "ADM"));
        assertFalse(this.usuarioController.verificarTipoUsuario("ADM1", "FUN"));
        assertTrue(this.usuarioController.verificarTipoUsuario(clienteId, "CLI"));
        assertFalse(this.usuarioController.verificarTipoUsuario(clienteId, "GER"));
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
