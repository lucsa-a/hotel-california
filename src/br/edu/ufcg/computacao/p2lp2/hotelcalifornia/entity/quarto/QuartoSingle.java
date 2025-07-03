package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador;

public class QuartoSingle extends Quarto {
    private static final int QTD_HOSPEDES = 1;

    public QuartoSingle(int id, double valorBasico, double valorPorPessoa) {
        super(id, QTD_HOSPEDES, valorBasico, valorPorPessoa);
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("[").append(super.getId()).append("] Quarto Single (custo basico: ")
                .append(Formatador.formataPreco(super.getValorBasico())).append("; por pessoa: ")
                .append(Formatador.formataPreco(super.getValorPorPessoa())).append(" >>> ")
                .append(Formatador.formataPreco(super.calcularValorDiaria())).append(" diária)");

        return out.toString();
    }
}
