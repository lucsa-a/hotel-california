package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.pagamento.*;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.reserva.Reserva;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.usuario.Usuario;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.*;

public class PagamentoController {

    private static PagamentoController instance;
    private ReservasSessionController reservasSessionController;
    private UsuarioController usuarioController;
    private FormaDePagamentoController formaDePagamentoController;

    public PagamentoController() {
        this.formaDePagamentoController = FormaDePagamentoController.getInstance();
        this.formaDePagamentoController.init();

        this.reservasSessionController = ReservasSessionController.getInstance();
        this.reservasSessionController.init();

        this.usuarioController = UsuarioController.getInstance();
        this.usuarioController.init();
    }

    public static PagamentoController getInstance() {
        if (instance == null) {
            instance = new PagamentoController();
        }
        return instance;
    }

    public String realizarPagamentoDinheiro(String idCliente, Long idReserva, String nomeTitular) {
        validarUsuarioCliente(idCliente);
        validarReservaExistente(idReserva);

        Reserva reserva = this.reservasSessionController.getReserva(idReserva);

        Usuario usuario = this.usuarioController.getUsuario(idCliente);

        if(!reserva.getCliente().equals(usuario)) {
            throw new HotelCaliforniaException("SOMENTE O PROPRIO CLIENTE PODERA PAGAR A SUA RESERVA");
        }

        if(reserva.getStatusPagamento()) {
            throw new HotelCaliforniaException("RESERVA JA FOI PAGA");
        }

        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoController.getFormaDePagamento("DINHEIRO"), nomeTitular);
        return pagamento.toString();
    }

    public String realizarPagamentoCartao(String idCliente, Long idReserva, String nomeTitular, String numCartao, String validade, String digitoVerificador, int qtdeParcelas) {
        validaNuloOuVazio(numCartao);
        validaNuloOuVazio(validade);
        validaNuloOuVazio(digitoVerificador);
        validarUsuarioCliente(idCliente);
        validarReservaExistente(idReserva);

        Reserva reserva = this.reservasSessionController.getReserva(idReserva);

        Usuario usuario = this.usuarioController.getUsuario(idCliente);

        if(!reserva.getCliente().equals(usuario)) {
            throw new HotelCaliforniaException("SOMENTE O PROPRIO CLIENTE PODERA PAGAR A SUA RESERVA");
        }

        if(reserva.getStatusPagamento()) {
            throw new HotelCaliforniaException("RESERVA JA FOI PAGA");
        }

        if(digitoVerificador.length() != 3) {
            throw new HotelCaliforniaException("DIGITO VERIFICADOR INVALIDO");
        }

        if(numCartao.length() != 16) {
            throw new HotelCaliforniaException("NUMERO DE CARTAO INVALIDO");
        }

        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("MM/yyyy");
        YearMonth dataValidade = null;

        try {
           dataValidade = YearMonth.parse(validade, formatadorData);
        } catch (DateTimeException e) {
            throw new HotelCaliforniaException("DATA INVALIDA");
        }

        if(dataValidade.isBefore(YearMonth.now())) {
            throw new HotelCaliforniaException("CARTAO VENCIDO");
        }

        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoController.getFormaDePagamento("CARTAO_DE_CREDITO"), nomeTitular, qtdeParcelas);
        return pagamento.toString();
    }

    public String realizarPagamentoPix(String idCliente, Long idReserva, String nomeTitular, String cpf, String banco) {
        validaNuloOuVazio(nomeTitular);
        validaNuloOuVazio(cpf);
        validaNuloOuVazio(banco);
        validarUsuarioCliente(idCliente);
        validarReservaExistente(idReserva);

        Reserva reserva = this.reservasSessionController.getReserva(idReserva);

        Usuario usuario = this.usuarioController.getUsuario(idCliente);

        if(!reserva.getCliente().equals(usuario)) {
            throw new HotelCaliforniaException("SOMENTE O PROPRIO CLIENTE PODERA PAGAR A SUA RESERVA");
        }

        if(cpf.length() != 11) {
            throw new HotelCaliforniaException("CPF INVALIDO");
        }

        if(reserva.getStatusPagamento()) {
            throw new HotelCaliforniaException("RESERVA JA FOI PAGA");
        }

        Pagamento pagamento = new Pagamento(reserva, formaDePagamentoController.getFormaDePagamento("PIX"), nomeTitular);
        return pagamento.toString();
    }
}
