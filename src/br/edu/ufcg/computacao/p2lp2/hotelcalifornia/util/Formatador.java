package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;

import javax.swing.plaf.PanelUI;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatador {

    public static String formataPreco(double preco) {
        Locale localeBR = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBR);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.CEILING);


        StringBuffer out = new StringBuffer();
        out.append("R$").append(numberFormat.format(preco));

        return out.toString();
    }

    public static String formataPrecoAreaComum(double valorPessoa) {
        if(valorPessoa == 0) {
            return "Grátis";
        }

        return formataPreco(valorPessoa);
    }

    public static String formataInteiro(int numero) {
        DecimalFormat formato = new DecimalFormat("00");
        return formato.format(numero);
    }

    public static String formatarPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuffer out = new StringBuffer();

        out.append(dataInicio.format(formatter)).append(" ate ").append(dataFim.format(formatter));
        return out.toString();
    }

    public static String formataData(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter);
    }

    public static String formataHorario(LocalTime hora) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return hora.format(formatter);
    }

    public static String formataHorarioHorasMinutos(LocalTime hora) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH'h'mm");

        return hora.format(dateTimeFormatter);
    }

    public static String formataTextoDisponibilidade(boolean disponivel) {
        if(disponivel) {
            return "VIGENTE";
        }

        return "INDISPONIVEL";
    }

    public static String formataStatusPagamento(boolean status) {
        if (status) {
            return "REALIZADO";
        }
        return "PENDENTE";
    }

}
