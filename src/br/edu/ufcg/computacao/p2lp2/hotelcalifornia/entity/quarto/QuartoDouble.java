package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador;

public class QuartoDouble extends Quarto {
    private static final int QTD_HOSPEDES = 2;

    private String[] melhorias;

    public QuartoDouble(int id, double valorBasico, double valorPorPessoa, String[] melhorias) {
        super(id, QTD_HOSPEDES, valorBasico, valorPorPessoa);
        this.melhorias = melhorias;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("[").append(super.getId()).append("] Quarto Double (custo basico: ")
                .append(Formatador.formataPreco(super.getValorBasico())).append("; por pessoa: ")
                .append(Formatador.formataPreco(super.getValorPorPessoa())).append(" >>> ")
                .append(Formatador.formataPreco(super.calcularValorDiaria())).append(" diária)");

        if (this.melhorias != null && this.melhorias.length > 0) {
            out.append(". Pedidos: [");

            for (int i = 0; i < this.melhorias.length; i++) {
                if (i == 0) {
                    out.append(this.melhorias[i]);
                } else {
                    out.append(", ").append(this.melhorias[i]);
                }
            }

            out.append("]");
        } else {
            out.append(". Pedidos: (nenhum)");
        }

        return out.toString();
    }
}
