package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.areaComum.AreaComum;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservaAuditorio extends Reserva{

    private static final String TIPO_RESERVA = "AUDITORIO";
    private int qtdPessoas;
    private AreaComum auditorio;

    public ReservaAuditorio(long id, Usuario cliente, AreaComum auditorio, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdPessoas) {
        super(id, cliente, dataInicio, dataFim, TIPO_RESERVA);
        this.qtdPessoas = qtdPessoas;
        this.auditorio = auditorio;
    }

    public int getQtdPessoas() {
        return qtdPessoas;
    }

    public void setQtdPessoas(int qtdPessoas) {
        this.qtdPessoas = qtdPessoas;
    }

    @Override
    public double calcularTotalReserva() {
        return this.qtdPessoas * super.calculaNumeroDiarias() * auditorio.getValorPessoa();
    }

    private boolean verificaGratis() {
        if (auditorio.getValorPessoa() == 0) {
            return true;
        }
        return super.getStatusPagamento();
    }

    private String formataPeriodoReserva(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return Formatador.formataData(dataInicio.toLocalDate()) + " " + Formatador.formataHorario(auditorio.getHorarioInicio())
                + " ate " + Formatador.formataData(dataFim.toLocalDate()) + " " + Formatador.formataHorario(auditorio.getHorarioFinal());
    }

    private String verificaDiarias() {
        if (Formatador.formataPrecoAreaComum(auditorio.getValorPessoa()).equals("Grátis")) {
            return "";
        } else {
            return " x" + super.calculaNumeroDiarias() + " (diarias) => " + Formatador.formataPreco(this.calcularTotalReserva());
        }
    }
    @Override
    public String toString() {
        return "[" + super.getId() + "] Reserva de AUDITORIO em favor de:\n" + super.getCliente().toString()
                + "\nDetalhes da reserva: \n- Periodo: " + this.formataPeriodoReserva(super.getDataInicio(), super.getDataFim())
                + "\n- Qtde. de Convidados: " + this.qtdPessoas + " pessoa(s)\n- Valor por pessoa: " + Formatador.formataPrecoAreaComum(auditorio.getValorPessoa())
                + "\nVALOR TOTAL DA RESERVA: " + Formatador.formataPrecoAreaComum(this.calcularTotalReserva()/super.calculaNumeroDiarias()) + this.verificaDiarias()
                + "\nSITUACAO DO PAGAMENTO: " + Formatador.formataStatusPagamento(this.verificaGratis()) + ".";
    }
}
