package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.quarto;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Formatador;

public class QuartoFamily extends Quarto {
    private String[] melhorias;

    public QuartoFamily(int id, int qtdeHospedes, double valorBasico, double valorPorPessoa, String[] melhorias) {
        super(id, qtdeHospedes, valorBasico, valorPorPessoa);
        this.melhorias = melhorias;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("[").append(super.getId()).append("] Quarto Family (custo basico: ")
                .append(Formatador.formataPreco(super.getValorBasico())).append("; por pessoa: ")
                .append(Formatador.formataPreco(super.getValorPorPessoa())).append(" >>> ")
                .append(Formatador.formataPreco(super.calcularValorDiaria())).append(" diária). Capacidade: ")
                .append(Formatador.formataInteiro(super.getQtdeHospedes())).append(" pessoa(s)");

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
