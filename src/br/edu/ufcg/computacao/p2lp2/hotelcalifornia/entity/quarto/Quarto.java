package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador;

import java.util.Objects;

public abstract class Quarto {

    private int id;
    private int qtdeHospedes;
    private double valorBasico;
    private double valorPorPessoa;

    public Quarto(int id, int qtdeHospedes, double valorBasico, double valorPorPessoa) {
        Validador.validarQtdeHospedesQuarto(qtdeHospedes);
        Validador.validarValor(valorBasico);
        Validador.validarValor(valorPorPessoa);

        this.id = id;
        this.qtdeHospedes = qtdeHospedes;
        this.valorBasico = valorBasico;
        this.valorPorPessoa = valorPorPessoa;
    }

    public int getId() {
        return this.id;
    }

    public int getQtdeHospedes() {
        return this.qtdeHospedes;
    }

    public double getValorBasico() {
        return this.valorBasico;
    }

    public double getValorPorPessoa() {
        return this.valorPorPessoa;
    }

    public double calcularValorDiaria() {
        return this.valorBasico + (this.valorPorPessoa * this.qtdeHospedes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quarto quarto = (Quarto) o;
        return id == quarto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
