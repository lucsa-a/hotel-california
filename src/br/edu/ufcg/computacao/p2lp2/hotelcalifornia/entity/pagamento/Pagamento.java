package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.pagamento;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.formaDePagamento.*;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.Reserva;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador;

import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador.formataPreco;
import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.validaNuloOuVazio;

public class Pagamento {
    private Reserva reserva;
    private FormaDePagamento formaDePagamento;
    private String nomeTitular;
    private double valorEfetivamentePago;
    private int qtdeParcelas;
    private double valorParcelas;


    public Pagamento(Reserva reserva, FormaDePagamento formaDePagamento, String nomeTitular) {
        this(reserva, formaDePagamento, nomeTitular, 1);
        this.valorEfetivamentePago = calculaValorEfetivo(this.reserva.calcularTotalReserva());
        this.valorParcelas = valorEfetivamentePago / qtdeParcelas;

        this.reserva.setStatusPagamento(true);
    }

    public Pagamento(Reserva reserva, FormaDePagamento formaDePagamento, String nomeTitular, int qtdeParcelas) {
        validaNuloOuVazio(nomeTitular);

        if(qtdeParcelas > 12 || qtdeParcelas <= 0) {
            throw new HotelCaliforniaException("QUANTIDADE DE PARCELAS INVALIDA");
        }

        this.reserva = reserva;
        this.formaDePagamento = formaDePagamento;
        this.nomeTitular = nomeTitular;
        this.qtdeParcelas = qtdeParcelas;
        this.valorEfetivamentePago = calculaValorEfetivo(this.reserva.calcularTotalReserva());
        this.valorParcelas = valorEfetivamentePago / qtdeParcelas;

        this.reserva.setStatusPagamento(true);
    }

    public double calculaValorEfetivo(double valor) {
        if (this.formaDePagamento.getPercentualDeDesconto() == 0) {
            return valor;
        }

        return valor - (valor * this.formaDePagamento.getPercentualDeDesconto());
    }

    @Override
    public String toString() {
        return "SITUACAO DO PAGAMENTO: REALIZADO" +
        "\n" + this.formaDePagamento.toString() +
        "\nTotal Efetivamente Pago: " + Formatador.formataPreco(valorEfetivamentePago) + " em " + qtdeParcelas + "x de " + Formatador.formataPreco(valorParcelas);
    }
}
