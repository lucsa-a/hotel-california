package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.areaComum;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.time.LocalTime;
import java.util.Objects;

import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador.*;
import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.*;

public class AreaComum {
    private long id;
    private String tipo;
    private String titulo;
    private LocalTime horarioInicio;
    private LocalTime horarioFinal;
    private double valorPessoa;
    private boolean disponivel;
    private int qtdMaxPesoas;

    public AreaComum(long id, String tipo, String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, boolean disponivel, int qtdMaxPesoas) {
        validaNuloOuVazio(titulo);
        validaNuloOuVazio(tipo);
        validarHorarioFimPosteriorHorarioInicio(horarioInicio, horarioFinal);
        validarValor(qtdMaxPesoas);

        if(!tipo.equals("AUDITORIO") && !tipo.equals("CINEMA") && !tipo.equals("PISCINA") && !tipo.equals("SALAO_DE_FESTA")) {
            throw new HotelCaliforniaException("TIPO INVALIDO");
        }

        if(valorPessoa < 0) {
            throw new HotelCaliforniaException("VALOR POR PESSOA INVALIDO");
        }

        this.id = id;
        this.tipo = tipo;
        this.titulo = titulo;
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
        this.valorPessoa = valorPessoa;
        this.disponivel = disponivel;
        this.qtdMaxPesoas = qtdMaxPesoas;
    }

    public long getId() {
        return id;
    }

    public int getQtdMaxPesoas() {
        return qtdMaxPesoas;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getValorPessoa() {
        return valorPessoa;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public LocalTime getHorarioFinal() {
        return horarioFinal;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void alterarDados(LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, int qtdMaxPesoas, boolean disponivel) {
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
        this.valorPessoa = valorPessoa;
        this.qtdMaxPesoas = qtdMaxPesoas;
        this.disponivel = disponivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaComum areaComum = (AreaComum) o;
        return id == areaComum.id && Objects.equals(titulo, areaComum.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo);
    }

    @Override
    public String toString() {
        return "[" + id + "] " + tipo + ": " + titulo.replace("_", " ") + " (" + formataHorarioHorasMinutos(horarioInicio) + " as " +
                formataHorarioHorasMinutos(horarioFinal) + "). Valor por pessoa: " + formataPrecoAreaComum(valorPessoa) +
                ". Capacidade: " + qtdMaxPesoas + " pessoa(s). " + formataTextoDisponibilidade(disponivel) + ".";

    }
}
