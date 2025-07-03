package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.formaDePagamento.*;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.util.*;

import static br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador.*;

public class FormaDePagamentoController {
    private static FormaDePagamentoController instance;
    private UsuarioController usuarioController;
    private List<FormaDePagamento> formasDePagamento;

    public FormaDePagamentoController() {
        this.usuarioController = UsuarioController.getInstance();
        this.formasDePagamento = new ArrayList<>();
    }

    public static FormaDePagamentoController getInstance() {
        if (instance == null) {
            instance = new FormaDePagamentoController();
        }
        return instance;
    }

    public String disponibilizarFormaDePagamento(String idAutenticacao, String formaDePagamento, double percentualDesconto) {
        validarIdUsuario(idAutenticacao);
        validarUsuarioFormaDePagamento(idAutenticacao);

        if(!formaDePagamento.equals("PIX") && !formaDePagamento.equals("CARTAO_DE_CREDITO") && !formaDePagamento.equals("DINHEIRO")) {
            throw new HotelCaliforniaException("TIPO DE FORMA DE PAGAMENTO INVALIDO");
        }

        if(verificarFormaDePagamento(formaDePagamento)) {
            throw new HotelCaliforniaException("FORMA DE PAGAMENTO JA EXISTE");
        }

        this.formasDePagamento.add(new FormaDePagamento((this.formasDePagamento.size() + 1), formaDePagamento, percentualDesconto));

        return this.formasDePagamento.get(formasDePagamento.size() - 1).toString();
    }

    public String alterarFormaDePagamento(String idAutenticacao, int idFormaPagamento, String formaPagamento, double percentualDesconto) {
        validaNuloOuVazio(formaPagamento);
        validarIdUsuario(idAutenticacao);
        validarUsuarioFormaDePagamento(idAutenticacao);

        if(idFormaPagamento > (this.formasDePagamento.size()) || idFormaPagamento < 0) {
            throw new HotelCaliforniaException("FORMA DE PAGAMENTO NAO EXISTE");
        }

        if(!formaPagamento.equals("PIX") && !formaPagamento.equals("CARTAO_DE_CREDITO") && !formaPagamento.equals("DINHEIRO")) {
            throw new HotelCaliforniaException("TIPO DE FORMA DE PAGAMENTO INVALIDO");
        }

        if(!verificarFormaDePagamento(formaPagamento)) {
            throw new HotelCaliforniaException("FORMA DE PAGAMENTO NAO DISPONIBILIZADO");
        }

        if(percentualDesconto < 0 || percentualDesconto > 1) {
            throw new HotelCaliforniaException("PERCENTUAL INVÁLIDO");
        }

        this.formasDePagamento.get(idFormaPagamento - 1).setPercentualDeDesconto(percentualDesconto);

        return this.formasDePagamento.get(idFormaPagamento -1).toString();
    }

    public String exibirFormaPagamento(int idFormaPagamento) {
        if(this.formasDePagamento.size() < idFormaPagamento || idFormaPagamento <= 0) {
            throw new HotelCaliforniaException("FORMA DE PAGAMENTO NAO EXISTE");
        }

        return this.formasDePagamento.get(idFormaPagamento - 1).toString();
    }

    public String[] listarFormasPagamentos() {
        String[] out = new String[this.formasDePagamento.size()];

        for(int i = 0; i < formasDePagamento.size(); i++) {
            out[i] = formasDePagamento.get(i).toString();
        }

        return out;
    }

    public FormaDePagamento getFormaDePagamento(String forma) {
        for(FormaDePagamento f : this.formasDePagamento) {
            if(f.getTipoDePagamento().equals(forma)) {
                return f;
            }
        }

        throw new HotelCaliforniaException("FORMA DE PAGAMENTO NAO EXISTE");
    }

    private boolean verificarFormaDePagamento(String forma) {
        for(FormaDePagamento f : this.formasDePagamento) {
            if(f.getTipoDePagamento().equals(forma)) {
                return true;
            }
        }

        return false;
    }

    public void init() {
        this.formasDePagamento.clear();
    }
}
