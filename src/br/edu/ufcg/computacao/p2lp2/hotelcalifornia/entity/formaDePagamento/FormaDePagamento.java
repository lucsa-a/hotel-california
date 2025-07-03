package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.formaDePagamento;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.util.Objects;

import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.validaNuloOuVazio;
import static java.lang.Math.round;

public class FormaDePagamento {
    private int id;
    private String tipoDePagamento;

    private double percentualDeDesconto;

    public FormaDePagamento(int id, String tipo, double percentualDeDesconto) {
        validaNuloOuVazio(tipo);

        if(percentualDeDesconto < 0 || percentualDeDesconto > 1) {
            throw new HotelCaliforniaException("PERCENTUAL INVÁLIDO");
        }

        this.id = id;
        this.tipoDePagamento = tipo;
        this.percentualDeDesconto = percentualDeDesconto;
    }

    public String getTipoDePagamento() {
        return tipoDePagamento;
    }

    public Double getPercentualDeDesconto() {
        return percentualDeDesconto;
    }

    public void setPercentualDeDesconto(Double percentualDeDesconto) {
        this.percentualDeDesconto = percentualDeDesconto;
    }

    @Override
    public String toString() {
        return "[" + this.id + "] Forma de pagamento: " + this.tipoDePagamento.replace("_", " ") + " (" + round(this.percentualDeDesconto * 100)  + "% de desconto em pagamentos)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormaDePagamento that = (FormaDePagamento) o;
        return id == that.id && Objects.equals(tipoDePagamento, that.tipoDePagamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoDePagamento);
    }
}
