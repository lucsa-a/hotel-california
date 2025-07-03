package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto.Quarto;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReservaQuarto extends Reserva {
    private static final String TIPO_RESERVA = "QUARTO";

    private Quarto quarto;
    private int qtdeHospedes;
    private List<Refeicao> refeicoes;
    private String[] pedidos;

    public ReservaQuarto(Long id, Usuario cliente, Quarto quarto, int qtdeHospedes, LocalDateTime dataInicio, LocalDateTime dataFim, List<Refeicao> refeicoes) {
        super(id, cliente, dataInicio, dataFim, TIPO_RESERVA);
        this.quarto = quarto;
        this.qtdeHospedes = qtdeHospedes;
        this.refeicoes = refeicoes;
        this.pedidos = null;
    }

    public ReservaQuarto(Long id, Usuario cliente, Quarto quarto, int qtdeHospedes, LocalDateTime dataInicio, LocalDateTime dataFim, List<Refeicao> refeicoes, String[] pedidos) {
        super(id, cliente, dataInicio, dataFim, TIPO_RESERVA);
        this.quarto = quarto;
        this.qtdeHospedes = qtdeHospedes;
        this.refeicoes = refeicoes;
        this.pedidos = pedidos;
    }

    public Quarto getQuarto() {
        return this.quarto;
    }

    public int getQtdeHospedes() {
        return this.qtdeHospedes;
    }

    public List<Refeicao> getRefeicoes() {
        return this.refeicoes;
    }

    public boolean getStatusPagamento() {
        return super.getStatusPagamento();
    }

    public double calcularTotalReserva() {
        return super.calculaNumeroDiarias() * this.calcularValorParcial();
    }

    private double calcularValorParcial() {
        return this.quarto.calcularValorDiaria() + this.qtdeHospedes * this.calcularTotalRefeicoes();
    }

    private double calcularTotalRefeicoes() {
        double totalRefeicoes = 0.0;
        for (Refeicao refeicao : this.refeicoes) {
            totalRefeicoes += refeicao.getValor();
        }

        return totalRefeicoes;
    }

    private String getTextoStatusPagamento() {
        if (this.getStatusPagamento()) {
            return "REALIZADO";
        }

        return "PENDENTE";
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();

        out.append("[").append(this.getId()).append("] Reserva de quarto em Favor de:")
                .append("\n- ").append(this.getCliente()).append("\nDetalhes da instalação:")
                .append("\n- ").append(this.quarto).append("\nDetalhes da reserva:")
                .append("\n- Período: ").append(Formatador.formatarPeriodo(super.getDataInicio(), super.getDataFim()))
                .append("\n- No. Hóspedes: ").append(this.qtdeHospedes).append(" pessoa(s)")
                .append("\n- Refeições incluidas: [");

        for (Refeicao refeicao : this.refeicoes) {
            out.append("\n").append(refeicao);
        }

        if (this.pedidos != null && this.pedidos.length > 0) {
            out.append("]\n").append("Pedidos: [");

            for (int i = 0; i < this.pedidos.length; i++) {
                if (i == 0) {
                    out.append(this.pedidos[i]);
                } else {
                    out.append(", ").append(this.pedidos[i]);
                }
            }

            out.append("]");
        } else {
            out.append("]\n").append("Pedidos: (nenhum)");
        }

        out.append("\nVALOR TOTAL DA RESERVA: ").append(Formatador.formataPreco(this.calcularValorParcial()))
                .append(" x").append(super.calculaNumeroDiarias()).append(" (diarias) => ").append(Formatador.formataPreco(this.calcularTotalReserva()))
                .append("\nSITUACAO DO PAGAMENTO: ").append(this.getTextoStatusPagamento()).append(".");

        return out.toString();
    }

    public boolean verificarIdQuarto(int id) {
        return this.getQuarto().getId() == id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservaQuarto reservaQuarto = (ReservaQuarto) o;
        return this.getId() == reservaQuarto.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
