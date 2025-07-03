package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Refeicao {
    private Long id;
    private String titulo;
    private String tipo;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private double valor;
    private boolean isDisponivel;

    public Refeicao(Long id, String tipo, String titulo, LocalTime horaInicio, LocalTime horaFim, double valor, boolean isDisponivel) {
        if(id == null || tipo == null || titulo == null || horaInicio == null || horaFim == null) {
            throw new NullPointerException();
        }

        if (titulo.trim().equals("") || tipo.trim().equals("")) {
            throw new IllegalArgumentException();
        }

        Validador.validarValor(valor);

        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.horarioInicio = horaInicio;
        this.horarioFim = horaFim;
        this.valor = valor;
        this.isDisponivel = isDisponivel;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public double getValor() {
        return valor;
    }

    public boolean isDisponivel() {
        return isDisponivel;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setDisponivel(boolean disponivel) {
        isDisponivel = disponivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Refeicao refeicao = (Refeicao) o;
        return Objects.equals(titulo, refeicao.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }
    private String formataTipoRefeicao() {
        if (this.tipo.equals("CAFE_DA_MANHA")) {
            return "Cafe-da-manha";
        } else if (this.tipo.equals("ALMOCO")) {
            return "Almoco";
        } else if (this.tipo.equals("JANTAR")) {
            return "Jantar";
        }
        throw new HotelCaliforniaException("TIPO DE REFEICAO INVALIDO");
    }
    @Override
    public String toString() {
        return "[" + this.id + "] " + this.formataTipoRefeicao() + ": " + this.titulo
                + " (" + Formatador.formataHorarioHorasMinutos(this.horarioInicio) + " as " + Formatador.formataHorarioHorasMinutos(this.horarioFim) + "). Valor por pessoa: "
                + Formatador.formataPreco(this.valor) + ". " + Formatador.formataTextoDisponibilidade(this.isDisponivel);
    }
}
