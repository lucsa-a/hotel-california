package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Reserva {
    private static final String TIPO_QUARTO = "QUARTO";

    private long id;
    private Usuario cliente;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private boolean statusPagamento;
    private String tipo;

    public Reserva(long id, Usuario cliente, LocalDateTime dataInicio, LocalDateTime dataFim, String tipo) {
        this.id = id;
        this.cliente = cliente;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.statusPagamento = false;
        this.tipo = tipo;

        if (this.tipo.equals(TIPO_QUARTO)) {
            this.calcularPeriodoMinimoReservaQuarto();
        }
    }

    public long getId() {
        return this.id;
    }

    public Usuario getCliente() {
        return this.cliente;
    }

    public LocalDateTime getDataInicio() {
        return this.dataInicio;
    }

    public LocalDateTime getDataFim() {
        return this.dataFim;
    }

    public boolean getStatusPagamento() {
        return this.statusPagamento;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setStatusPagamento(boolean statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public abstract double calcularTotalReserva();

    protected int calculaNumeroDiarias() {
        Duration duration = Duration.between(this.dataInicio, this.dataFim);
        if (duration.toHours() % 24 != 0)
            return (int) duration.toDays() + 1;

        return (int) duration.toDays();
    }

    public boolean verificarTipoReserva(String tipo) {
        return this.tipo.equalsIgnoreCase(tipo);
    }

    public void calcularPeriodoMinimoReservaQuarto() {
        this.dataInicio = this.dataInicio.withHour(14).withMinute(0);

        if (this.dataInicio.toLocalDate().equals(this.dataFim.toLocalDate())) {
            this.dataFim = this.dataFim.plusDays(1);
        }
        this.dataFim = dataFim.withHour(12).withMinute(0);
    }
}
