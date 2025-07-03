package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    public void testUsuarioComIdNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Usuario(null, "Taylor Swift", 131313, "CLI");
        });
    }

    @Test
    public void testUsuarioComNomeNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Usuario("CLI1", null, 131313, "CLI");
        });
    }

    @Test
    public void testUsuarioComTipoNulo() {
        assertThrows(NullPointerException.class, () -> {
            new Usuario("CLI1", "Taylor Swift", 131313, null);
        });
    }

    @Test
    public void testUsuarioComIdVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("", "Taylor Swift", 131313, "CLI");
        });
    }

    @Test
    public void testUsuarioComNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("CLI1", "", 131313, "CLI");
        });
    }

    @Test
    public void testUsuarioComTipoVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("CLI1", "Taylor Swift", 131313, "");
        });
    }

    @Test
    public void testToString() {
        Usuario usuario = new Usuario("CLI1", "Taylor Swift", 131313, "CLI");
        assertEquals("[CLI1] Taylor Swift (No. Doc. 131313)", usuario.toString());
    }
}
