package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario;

import java.util.*;

import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.validaNuloOuVazio;

public class Usuario {
    private String id;
    private String nome;
    private long documento;
    private String tipo;

    public Usuario(String id, String nome, long documento, String tipo) {
        validaNuloOuVazio(id);
        validaNuloOuVazio(nome);
        validaNuloOuVazio(tipo);

        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public long getDocumento() {
        return documento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " (No. Doc. " + documento + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
