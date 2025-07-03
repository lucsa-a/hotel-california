package br.edu.ufcg.computacao.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.entity.refeicao.Refeicao;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.computacao.p2lp2.hotelcalifornia.util.Validador;

import java.time.LocalTime;
import java.util.*;

public class RefeicaoController {
    private static RefeicaoController instance;
    private UsuarioController usuarioController;
    private Map<Long, Refeicao> refeicoes;

    private RefeicaoController() {
        this.refeicoes = new HashMap<>();

        this.usuarioController = UsuarioController.getInstance();
        this.usuarioController.init();
    }

    public static RefeicaoController getInstance() {
        if (instance == null) {
            instance = new RefeicaoController();
        }
        return instance;
    }

    public Refeicao getRefeicao(Long idRefeicao) {
        return this.refeicoes.get(idRefeicao);
    }

    public Map<Long, Refeicao> getRefeicoes() {
        return refeicoes;
    }

    public String disponibilizarRefeicao(String idAutenticacao, String tipoRefeicao, String titulo, LocalTime horarioInicio, LocalTime horarioFim, double valor, boolean isDisponivel) {
        Validador.validaNuloOuVazio(idAutenticacao);
        Validador.validarIdUsuario(idAutenticacao);
        Validador.validarTipoUsuarioRefeicao(idAutenticacao, new String[]{"GER", "FUN"});
        Validador.validarHorarioFimPosteriorHorarioInicio(horarioInicio, horarioFim);

        Refeicao refeicao = new Refeicao(Long.valueOf(refeicoes.size()), tipoRefeicao, titulo, horarioInicio, horarioFim, valor, isDisponivel);

        if (this.refeicoes.containsValue(refeicao)) {
            throw new HotelCaliforniaException("REFEICAO JA EXISTE");
        } else {
            refeicoes.put(Long.valueOf(refeicoes.size()), refeicao);
        }

        return refeicao.toString();
    }
    public String alterarRefeicao(Long idRefeicao, LocalTime horarioInicio, LocalTime horarioFim, double valorPorPessoa, boolean isDisponivel) {
        if (!this.refeicoes.containsKey(idRefeicao)) {
            throw new HotelCaliforniaException("REFEICAO NAO EXISTE");
        }
        Validador.validarHorarioFimPosteriorHorarioInicio(horarioInicio, horarioFim);
        Validador.validarValor(valorPorPessoa);

        this.refeicoes.get(idRefeicao).setHorarioInicio(horarioInicio);
        this.refeicoes.get(idRefeicao).setHorarioFim(horarioFim);
        this.refeicoes.get(idRefeicao).setValor(valorPorPessoa);
        this.refeicoes.get(idRefeicao).setDisponivel(isDisponivel);

        return refeicoes.get(idRefeicao).toString();
    }

    public String exibirRefeicao(Long idRefeicao) {
        if (this.refeicoes.containsKey(idRefeicao)) {
            return this.refeicoes.get(idRefeicao).toString();
        } else {
            throw new HotelCaliforniaException("REFEICAO NAO EXISTE");
        }
    }

    public String[] listarRefeicoes() {
        String[] out = new String[this.refeicoes.size()];

        int i = 0;
        for (Refeicao refeicao : this.refeicoes.values()) out[i++] = refeicao.toString();

        return out;
    }

    public void init() {
        this.refeicoes.clear();
    }
}