package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador;

import java.time.*;

public class ReservaRestaurante extends Reserva {

    private static final String TIPO_RESERVA = "RESTAURANTE";
    private int qtdPessoas;
    private Refeicao refeicao;

    public ReservaRestaurante(Long id, Usuario cliente, LocalDateTime dataInicio, LocalDateTime dataFinal, int qtdPessoas, Refeicao refeicao) {
        super(id, cliente, dataInicio, dataFinal, TIPO_RESERVA);

        if(id == null || cliente == null || dataInicio == null || dataFinal == null || refeicao == null) {
            throw new NullPointerException();
        }

        this.qtdPessoas = qtdPessoas;
        this.refeicao = refeicao;
    }

    private double calcularValorParcial() {
        return this.qtdPessoas * refeicao.getValor();
    }

    public int calculaDiarias() {
        LocalDateTime inicio = super.getDataInicio().toLocalDate().atTime(this.refeicao.getHorarioInicio());
        LocalDateTime fim = super.getDataFim().toLocalDate().atTime(this.refeicao.getHorarioFim());

        Duration duration = Duration.between(inicio, fim);

        long horas = duration.toHours();

        if (horas % 24 != 0) {
            return (int) duration.toDays() + 1;
        }

        return (int) duration.toDays();
    }
    @Override
    public double calcularTotalReserva() {
        return this.qtdPessoas * this.calculaDiarias() * refeicao.getValor();
    }

    private String formataPeriodoReserva(LocalDateTime dataInicio, LocalDateTime dataFim, Refeicao refeicao) {
        return Formatador.formataData(dataInicio.toLocalDate()) + " " + Formatador.formataHorario(refeicao.getHorarioInicio())
                + " ate " + Formatador.formataData(dataFim.toLocalDate()) + " " + Formatador.formataHorario(refeicao.getHorarioFim());
    }

    @Override
    public String toString() {
        return "[" + super.getId() + "] Reserva de RESTAURANTE em favor de:\n- " + super.getCliente().toString()
                + "\nDetalhes da reserva: \n- Periodo: " + this.formataPeriodoReserva(this.getDataInicio(), this.getDataFim(), this.refeicao) + "\n- Qtde. de Convidados: "
                + this.qtdPessoas + " pessoa(s)\n- Refeicao incluida: " + this.refeicao.toString()
                + ".\nVALOR TOTAL DA RESERVA: " + Formatador.formataPreco(this.calcularValorParcial()) + " x" + this.calculaDiarias() + " (diarias) => "
                + Formatador.formataPreco(this.calcularTotalReserva()) + "\nSITUACAO DO PAGAMENTO: " + Formatador.formataStatusPagamento(super.getStatusPagamento()) + ".";
    }

}